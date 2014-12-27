 package com.coship.game.crawler.apk.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coship.game.crawler.apk.processor.DangBeiProcessor;
import com.coship.game.crawler.service.IAPKEntityService;
import com.coship.game.crawler.utils.ConfigFactory;
import com.coship.game.crawler.utils.Constants;
import com.coship.game.crawler.utils.DateUtil;
import com.coship.game.crawler.utils.JsoupUtil;
import com.coship.game.crawler.utils.Constants.APKSource;

@Component("dangBeiExecutor")
public class DangBeiExecutor extends BaseExecutor {
	
	private Logger log=Logger.getLogger(this.getClass());
	
	private @Autowired IAPKEntityService apkEntityService; 
	//首页
	private String host=ConfigFactory.getString(Constants.DANGBEI_HOMEURL);
	//抓取网页
	private String fetchUrl=ConfigFactory.getString(Constants.DANGBEI_FETCHURL);
	//是否全量抓取
	private boolean fetchAll=ConfigFactory.getBoolean(Constants.APK_FETCH_ALL);
	//总页数
	private int totalPage=0;
	//抓取时间
	private Date fetchDate;
	
	public synchronized void execute(){
		log.info("==============执行dangBei网抓取任务开始!==============");
		int page = 1;
		try {
			//配置不是全量抓取时查询数据库是否有数据，有则增量，无则全量
			if(!fetchAll){
				//查询表中是否存在记录
				int count=apkEntityService.countBySourceName(Constants.APKSource.DANGBEI.getName());
				if(count>0){
					int fetchDay=Integer.parseInt(ConfigFactory.getString(Constants.APK_FETCH_DAY));
					fetchDate=DateUtil.raiseDay(DateUtil.getDateFormat(new Date(),DateUtil.YYYYMMDD), fetchDay);
					log.info("=====================配置apk抓取开始时间:"+DateUtil.date2String(fetchDate)+"=====================");
				}else{
					fetchAll=true;
				}
			}
			List<String> detailUrls = new ArrayList<String>();
			long startTime=System.currentTimeMillis();
			do {
				detailUrls.addAll(fetchDetailPage(page));
				page++;
			} 
			while(page<=totalPage);
			log.info("==============dangBei网共抓取到"+detailUrls.size()+"个详情页==============");
			postProcess(new DangBeiProcessor(APKSource.DANGBEI.getName()), detailUrls);
			long endTime=System.currentTimeMillis();
			log.info("==============抓取dangBei网"+detailUrls.size()+"个详情页用时:"+(endTime-startTime)+"ms"+"==============");
		} catch (Exception e) {
			log.error("抓取dangBei网任务出现异常",e);
		}
		log.info("==============执行dangBei网抓取任务结束!==============");
	}
	
	/**
	 * 抓取详情页
	 * @param page
	 */
	public List<String> fetchDetailPage(int page) throws Exception{
		List<String> detailUrls=new ArrayList<String>();
		try {
			String url=String.format(fetchUrl, page);
			Document doc=JsoupUtil.getDocumentWithRetry(url);
			if(totalPage==0){//获取总页数
				Elements pageEle=doc.getElementById("mainContent").select("span[class=pageinfo] >strong");
				totalPage=Integer.parseInt(pageEle.get(0).text());
			}
			Elements links=doc.getElementById("softList").getElementsByTag("li");
			for(Element e:links){
				if(!fetchAll){
					//获取apk更新时间
					String dateFontText=e.select("font[class=infoVal]").get(1).text();
					String monthAndDay[]=dateFontText.split("-");
					Date time=DateUtil.getDefinedTime(0,Integer.parseInt(monthAndDay[0]),Integer.parseInt(monthAndDay[1]));
					Date updateDate=DateUtil.getDateFormat(time, DateUtil.YYYYMMDD);
					if(fetchDate.getTime()>updateDate.getTime()){//只抓取APK更新时间大于配置时间的APK
						break;
					}
				}
				String detailUrl=e.getElementsByTag("a").get(0).attr("href");
				detailUrls.add(host+detailUrl);
			}
		} catch (Exception e) {
			log.error("抓取dangBei网详情页任务出现异常",e);
		}
		return detailUrls;
	}
	
	
	public static void main(String[] args) {
		System.out.println(DateUtil.date2String(DateUtil.getDefinedTime(2015,02,19)));
		
	}
}
