package com.coship.game.crawler.apk.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.coship.game.crawler.apk.processor.SFProcessor;
import com.coship.game.crawler.utils.ConfigFactory;
import com.coship.game.crawler.utils.Constants;
import com.coship.game.crawler.utils.Constants.APKSource;
import com.coship.game.crawler.utils.DateUtil;
import com.coship.game.crawler.utils.JsoupUtil;
import com.coship.game.crawler.utils.NHttpUtil;

/**
 * 沙发管家执行类
 * @author 907708
 *
 */
@Component("SFExecutor")
public class SFExecutor extends BaseExecutor {

    Logger loger = Logger.getLogger(SFExecutor.class);
    
	private final String shafaAPI ="http://app.shafa.com/list/game?output=json&page=%d";

	private final String referer = "http://app.shafa.com/list/game";
	int totalPage = 0;
	
	int stopPage = 0;
	
	public String host = "http://app.shafa.com";
	
	public synchronized void executeIncrement(){
		loger.info("增量爬取开始");
		
		List<String> detailUrls = new ArrayList<String>();
		int page = 1;
		try {
			detailUrls.addAll(fetchPageDoc(false,page));
			while(page<=totalPage){
				detailUrls.addAll(fetchPageDoc(false,page));
				if(stopPage==page)
					break;
				page++;
			}
		} catch (JSONException e) {
			loger.error(e.getMessage());
		}
		totalPage = 0;
		stopPage = 0;
		postProcess(new SFWorker(), detailUrls);
		loger.info("增量爬取结束");
	}
	
	public synchronized void executeAll(){
		loger.info("全量爬取开始");
		
			List<String> detailUrls = new ArrayList<String>();
			int page = 1;
			try {
				detailUrls.addAll(fetchPageDoc(true,page));
				while(page<=totalPage){
					List<String> urls = fetchPageDoc(true,page);
					for(String url:urls){
						if(!detailUrls.contains(url)){
							detailUrls.add(url);
						}
					}
					++page;
				}
			} catch (JSONException e) {
				loger.error(e.getMessage());
			}
			totalPage = 0;
			stopPage = 0;
			loger.info("本次需执行下载apk数量:"+detailUrls.size());
			postProcess(new SFProcessor(APKSource.SHAFA.getName()), detailUrls);
			//postProcess(new SFWorker(), detailUrls);
			loger.info("全量爬取结束");
			System.exit(1);
	}
	
	public List<String> fetchPageDoc(boolean crawlerAll,int page) throws JSONException{
		String url = String.format(shafaAPI, page);
		String content = NHttpUtil.doGetWithRetry(url, "utf-8",referer);
		System.out.println(content);
		List<String> urls = new ArrayList<String>();
		if(StringUtils.isNotBlank(content)){
			JSONObject json = new JSONObject(content);
			String htmlContent = json.getString("json_view");
			Document doc = Jsoup.parse(htmlContent);
			if(totalPage == 0){
			  Element pageElement = doc.select("ul[class=pagination pagination-sm] > li").last();
			  String pagetTotalStr = JsoupUtil.getNumberString(pageElement.html());
			  if(StringUtils.isNotBlank(pagetTotalStr)){
				  totalPage = Integer.valueOf(pagetTotalStr);
			  }
			}
			if(page==1){
				Elements els = doc.select("a[class=thumbnail-link]");
				for(Element el:els){
				    String urlStr = el.attr("href");
				    if(!urls.contains(urlStr)){
				    	urls.add(el.attr("href"));
				    }
			    }
		   }
		   Elements urlels = doc.select("a[class=thumbnail app-item cleatfix]");
		   for(Element el:urlels){
			   String urlStr = el.attr("href");
			   if(StringUtils.isBlank(urlStr))
			    	continue;
			    urlStr=host+urlStr;
			    if(!crawlerAll&&!decideCrawlerUrl(urlStr)){
			       stopPage = page;
			       break;	
			    }
//			    if(!urls.contains(urlStr)){
			    	urls.add(urlStr);
//			    }
		   }
	  }
		return urls;
	}
	
	private boolean decideCrawlerUrl(String url){
		int fetchDay=Integer.parseInt(ConfigFactory.getString(Constants.APK_FETCH_DAY));
		Date fetchDate=DateUtil.raiseDay(new Date(), fetchDay);
		Document doc = JsoupUtil.getDocumentWithRetry(url);
		String updateTime = JsoupUtil.getElementByCss("section[class=app-info]>div>div>div>div[class*=meta-info]", doc, 5).select("span").text();
		if(StringUtils.isBlank(updateTime))return true;
		if(updateTime.indexOf(".")>-1){
			updateTime = updateTime.replaceAll("\\.", "-");
		}
		updateTime = updateTime.indexOf(":")>-1?updateTime:updateTime+" 00:00:00";
		Date updateTimeDate = DateUtil.string2Date(updateTime);
		if(updateTimeDate.after(fetchDate))
			return true;
		return false;
	}
	
	public synchronized void testSynchronizedMethod() throws InterruptedException{
		System.out.println(System.currentTimeMillis()+": begin Test");
		Thread.sleep(2*60*1000);
		System.out.println(System.currentTimeMillis()+": end Test");
		
	}
	

}
