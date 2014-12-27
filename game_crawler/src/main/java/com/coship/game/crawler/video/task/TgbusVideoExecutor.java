package com.coship.game.crawler.video.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coship.game.crawler.service.IVideoService;
import com.coship.game.crawler.utils.ConfigFactory;
import com.coship.game.crawler.utils.Constants;
import com.coship.game.crawler.utils.DateUtil;
import com.coship.game.crawler.utils.JsoupUtil;
import com.coship.game.crawler.video.processor.TgbusProcessor;

/**
 * 
 * @author 909191
 *
 */
@Component("tgbusVideoExecutor")
public class TgbusVideoExecutor extends BaseExecutor{

	private Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private IVideoService videoService;

	// 抓取网页
	private String fetchUrl = ConfigFactory.getString(Constants.TGBUS_FETCHURL);
	// 是否全量抓取
	private boolean fetchAll = ConfigFactory.getBoolean(Constants.VIDEO_FETCH_ALL);
	// 总页数
	private int totalPage = 0;
	// 抓取时间
	private Date fetchDate;

	public synchronized void execute(){
		log.info("=====================执行tgbus视频抓取任务开始!=====================");
		int page = 1;
		try {
			//配置不是全量抓取时第一次全量
			if(!fetchAll){
				int count=videoService.countBySourceName(Constants.VideoSource.TGBUS.getName());
				if(count>0){
					int fetchDay=Integer.parseInt(ConfigFactory.getString(Constants.VIDEO_FETCH_DAY));
					fetchDate=DateUtil.raiseDay(DateUtil.getDateFormat(new Date(),DateUtil.YYYYMMDD), fetchDay);
					System.out.println("=====================配置视频抓取开始时间:"+DateUtil.date2String(fetchDate)+"=====================");
				}else{
					fetchAll=true;
				}
			}
			List<String> detailUrls = new ArrayList<String>();
			detailUrls.addAll(fetchDetailPage());
			//解析详情页
			log.info("==============tgbus视频共抓取到"+detailUrls.size()+"个详情页==============");
			long startTime=System.currentTimeMillis();
			postProcess(new TgbusProcessor(Constants.VideoSource.TGBUS.getName()), detailUrls);
			long endTime=System.currentTimeMillis();
			log.info("==============抓取tgbus视频网"+detailUrls.size()+"个详情页用时:"+(endTime-startTime)+"ms"+"==============");
		} catch (Exception e) {
			log.error("抓取tgbus网任务出现异常",e);
		}
		log.info("=====================执行tgbus视频抓取任务结束!=====================");
	}
	
	/**
	 * 抓取详情页Url
	 * @param page
	 */
	private List<String> fetchDetailPage(){
		List<String> detailUrls=new ArrayList<String>();
		try {
			Document doc=JsoupUtil.getDocumentWithRetry(fetchUrl);
			Elements categoryEles=doc.select("div[class^=login_big] > blockquote > p > a");
			categoryEles.remove(categoryEles.last());
			for(Element ce:categoryEles){//遍历栏目
				String url=ce.attr("href");
				if("/".equals(url)){//首页解析
					Document indexDoc = JsoupUtil.getDocumentWithRetry(fetchUrl);
					/**
					 * 巴士原创
					 */
					Elements bugsBody=indexDoc.select("div[class=neirong]");
					//电玩天堂,173专栏
					Elements dw173Eles=bugsBody.select("div[class=game]");
					for(int i=0;i<dw173Eles.size();i++){
						Elements dianwanEles=dw173Eles.get(i).select("> div").get(1).select("> div");
						for(Element e:dianwanEles){
							String dUrl=e.select("> a").attr("href");
							detailUrls.add(fetchUrl+dUrl);
						}
					}
					//游戏抢先报,游戏新体位
					Elements qx_tw_eles=bugsBody.select("div[class=game1] > div");
					Elements qxEles=qx_tw_eles.get(1).select("div > a");
					Elements twEles=qx_tw_eles.get(3).select("div > a");
					
					String qxUrl=qxEles.attr("href");//网页布局不同
					String twUrl=twEles.attr("href");
					detailUrls.add(fetchUrl+twUrl);
					/**
					 * 网络游戏,电视游戏,单机游戏,手游·科技,搞笑视频
					 */
					Elements indexCategoryBody=indexDoc.select("div[class=m box] > div[class=left_700 lf] > div[class^=tour] > ul > li");
					for(Element iEles:indexCategoryBody){
						Elements aEles=iEles.select(" >p").get(0).select("> a");
						String categoryUrl=aEles.attr("href");
						if(categoryUrl.contains("vid=tgbus")){//网络游戏前两个视频布局不同
							continue;
						}
						detailUrls.add(fetchUrl+categoryUrl);
					}
				}else{//分类页解析
					int page=1;
					do {
						//进入分类页
						Document categoryPageDoc = JsoupUtil.getDocumentWithRetry(fetchUrl+url+page+"/");
						Elements categoryBody=categoryPageDoc.select("div[class^=left_700]");
						if(totalPage==0){//获取总页数
							String pageText=categoryBody.select("div[class=page ma20] > ul > li >a").last().attr("href");
							totalPage=Integer.parseInt(pageText.split("/")[2]);
							totalPage=totalPage>5?5:totalPage;//总页数大于5页取5页记录
						}
						Elements detailEles=categoryBody.select("div[class=video_list ma20] >dl");
						for(Element de:detailEles){
							if(!fetchAll){
								String updateTimeStr=de.select("dd > span").get(1).text().split("：")[1];
								Date updateDate=DateUtil.string2Date(updateTimeStr, "yyyy-MM-dd");
								if(fetchDate.getTime()>updateDate.getTime()){//抓取更新时间大于抓取时间的视频
									continue;
								}
							}
							String detailUrl=de.select("dt > a").attr("href");
							if(StringUtils.isEmpty(detailUrl))
								continue;
							detailUrls.add(fetchUrl+detailUrl);
						}
						page++;
					} while (page<=totalPage);
				}
			}
		} catch (Exception e) {
			log.error("抓取tgbus视频任务出现异常",e);
		}
		return detailUrls;
	}
	
	
}
