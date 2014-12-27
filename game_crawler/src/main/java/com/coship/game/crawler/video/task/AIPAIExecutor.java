package com.coship.game.crawler.video.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;

import com.coship.game.crawler.domain.VideoEntity;
import com.coship.game.crawler.thread.Master;
import com.coship.game.crawler.utils.ConfigFactory;
import com.coship.game.crawler.utils.Constants;
import com.coship.game.crawler.utils.NHttpUtil;
import com.coship.game.crawler.utils.Constants.VideoSource;
import com.coship.game.crawler.utils.JsoupUtil;
import com.coship.game.crawler.video.processor.AipaiUrlListWorker;
import com.coship.game.crawler.video.processor.AipaiVideoProcessor;





public class AIPAIExecutor extends BaseExecutor{
  
   //控制并发
   private volatile boolean isRunning = false;

   Logger logger = Logger.getLogger(AIPAIExecutor.class);
   private String entryUrl = "http://www.aipai.com/allgame.html";
   
   //默认单页100条记录
   private String pageUrlFormatterAll = "http://www.aipai.com/app/www/apps/gameAreaInfo.php?callback=jsonp&data={\"gameid\":%s,page\":%s,\"pageSize\":10}&action=getWork";
   private String pageUrlFormatter = "http://www.aipai.com/app/www/apps/gameAreaInfo.php?callback=jsonp&data={\"gameid\":%s,";
   private String pageUrlFormaterrsubfix = "\"page\":%s,\"pageSize\":10}&action=getWork";
   
   
   public synchronized void executeWithThread(){
//	   if(!isRunning){
//		   isRunning = true;
		   long startAll = System.currentTimeMillis();
		   logger.info("开始抓取aipai网数据");
		   Document doc = JsoupUtil.getDocumentWithRetry(entryUrl);
		   Elements aels = doc.select("div[class=class_letter]>div[class=cl_bd wrapfix]>ul>li>p>a");
		   List<String> urls = new ArrayList<String>();
		   for(Element el:aels){
			  String url = el.attr("href");
			  if(StringUtils.isEmpty(url)){
				  continue;
			  }
			  String gameId = extractGameId(url);
			  String pageUrl = String.format(pageUrlFormatter,gameId);
			  pageUrl = pageUrl+pageUrlFormaterrsubfix;
			  urls.add(pageUrl);
		   }
		   logger.info("本次处理游戏数量："+urls.size());
		   int threadCounts = ConfigFactory.getInt(Constants.THREADCOUNTS, 5);
		   Master<String> jsonMaster = new Master<String>(new AipaiUrlListWorker("aipaijsonlist"),threadCounts);
		   jsonMaster.addBatchJob(urls);
		   jsonMaster.execute();
		   Map<String,Object> resultMap = jsonMaster.getResultMap();
		   List<JSONObject> jsonlist = new ArrayList<JSONObject>();
		   while(!jsonMaster.isComplete()||resultMap.size()>0){
			   Iterator<String> it = resultMap.keySet().iterator();
				if (it.hasNext()) {
					String key = (String) it.next();
					@SuppressWarnings("unchecked")
					List<JSONObject> list = (List<JSONObject>) resultMap.get(key);
					postProcess(new AipaiVideoProcessor(VideoSource.AIPAI.getName()),list);
					//jsonlist.addAll(list);
					list = null;
					resultMap.remove(key);
				}
		   }
		   logger.info("结束抓取aipai网数据");
		   logger.info((System.currentTimeMillis()-startAll)/(1000*60)+"分钟");
		   isRunning = false;
//	   }
   }
   
   public synchronized void executeWithOneThread(){
		   long startAll = System.currentTimeMillis();
		   logger.info("开始抓取aipai网数据");
		   Document doc = JsoupUtil.getDocumentWithRetry(entryUrl);
		   Elements aels = doc.select("div[class=class_letter]>div[class=cl_bd wrapfix]>ul>li>p>a");
		   List<String> urls = new ArrayList<String>();
		   for(Element el:aels){
			  String url = el.attr("href");
			  if(StringUtils.isEmpty(url)){
				  continue;
			  }
			  String gameId = extractGameId(url);
			  String pageUrl = String.format(pageUrlFormatter,gameId);
			  pageUrl = pageUrl+pageUrlFormaterrsubfix;
			  urls.add(pageUrl);
		   }
		   logger.info("本次处理游戏数量："+urls.size());
		   int threadCounts = ConfigFactory.getInt(Constants.THREADCOUNTS, 5);
		   Master<String> jsonMaster = new Master<String>(new AipaiUrlListWorker("aipaijsonlist"),threadCounts);
		   jsonMaster.addBatchJob(urls);
		   jsonMaster.execute();
		   Map<String,Object> resultMap = jsonMaster.getResultMap();
		   List<JSONObject> jsonlist = new ArrayList<JSONObject>();
		   while(!jsonMaster.isComplete()||resultMap.size()>0){
			   Iterator<String> it = resultMap.keySet().iterator();
				if (it.hasNext()) {
					String key = (String) it.next();
					@SuppressWarnings("unchecked")
					List<JSONObject> list = (List<JSONObject>) resultMap.get(key);
					for(JSONObject json:list){
					   VideoEntity video = new AipaiVideoProcessor(json).extract();
					   process(video);
					}
					//jsonlist.addAll(list);
					list = null;
					resultMap.remove(key);
				}
		   }
		   logger.info("结束抓取aipai网数据");
		   logger.info((System.currentTimeMillis()-startAll)/(1000*60)+"分钟");
   }
   
   public synchronized void executeIncrement(){
	   long startAll = System.currentTimeMillis();
	   logger.info("开始抓取aipai网数据");
	   Document doc = JsoupUtil.getDocumentWithRetry(entryUrl);
	   Elements aels = doc.select("div[class=class_letter]>div[class=cl_bd wrapfix]>ul>li>p>a");
	   List<String> urls = new ArrayList<String>();
	   for(Element el:aels){
		  String url = el.attr("href");
		  if(StringUtils.isEmpty(url)){
			  continue;
		  }
		  String gameId = extractGameId(url);
		  String pageUrl = String.format(pageUrlFormatter,gameId);
		  pageUrl = pageUrl+pageUrlFormaterrsubfix;
		  urls.add(pageUrl);
	   }
	   logger.info("本次处理游戏数量："+urls.size());
	   for(String url:urls){
		      int pageIndex = 1;
			  String pageUrl = String.format(url,pageIndex++);
			  String content = NHttpUtil.doGetWithRetry(pageUrl, "utf-8", null);
			  while(content.indexOf("{")>-1){
				  //默认只抓取5页更新数据  
				   if(pageIndex>5)
				    	break;
				    content = content.substring(6,content.length()-2);
					JSONObject json;
					try {
						json = new JSONObject(content);
						JSONArray array = json.getJSONArray("data").getJSONArray(0);
						for(int index=0,len=array.length();index<len;index++){
							JSONObject workObject = array.getJSONObject(index).getJSONObject("work");
							if(workObject!=null){
								   VideoEntity video = new AipaiVideoProcessor(workObject).extract();
								   process(video);
							}
							workObject = null;
						}
						content = null;
						json = null;
					} catch (JSONException e) {
						e.printStackTrace();
					}
					pageUrl = String.format(url,pageIndex++);
					content = NHttpUtil.doGetWithRetry(pageUrl, "utf-8", null);
			}
	   }
	
	   logger.info("结束抓取aipai网数据");
	   logger.info((System.currentTimeMillis()-startAll)/(1000*60)+"分钟");
}
   
   
   public synchronized void executeAll(){
		   long startAll = System.currentTimeMillis();
		   logger.info("开始抓取aipai网数据");
		   Document doc = JsoupUtil.getDocumentWithRetry(entryUrl);
		   Elements aels = doc.select("div[class=class_letter]>div[class=cl_bd wrapfix]>ul>li>p>a");
		   List<String> urls = new ArrayList<String>();
		   for(Element el:aels){
			  String url = el.attr("href");
			  if(StringUtils.isEmpty(url)){
				  continue;
			  }
			  String gameId = extractGameId(url);
			  String pageUrl = String.format(pageUrlFormatter,gameId);
			  pageUrl = pageUrl+pageUrlFormaterrsubfix;
			  urls.add(pageUrl);
		   }
		   logger.info("本次处理游戏数量："+urls.size());
		   for(String url:urls){
			      int pageIndex = 1;
				  String pageUrl = String.format(url,pageIndex++);
				  String content = NHttpUtil.doGetWithRetry(pageUrl, "utf-8", null);
				  while(content.indexOf("{")>-1){
					    //System.out.println(pageUrl);
					    content = content.substring(6,content.length()-2);
						JSONObject json;
						try {
							json = new JSONObject(content);
							JSONArray array = json.getJSONArray("data").getJSONArray(0);
							for(int index=0,len=array.length();index<len;index++){
								JSONObject workObject = array.getJSONObject(index).getJSONObject("work");
								if(workObject!=null){
									   VideoEntity video = new AipaiVideoProcessor(workObject).extract();
									   process(video);
								}
								workObject = null;
							}
							content = null;
							json = null;
						} catch (JSONException e) {
							e.printStackTrace();
						}
						pageUrl = String.format(url,pageIndex++);
						content = NHttpUtil.doGetWithRetry(pageUrl, "utf-8", null);
				}
		   }
		
		   logger.info("结束抓取aipai网数据");
		   logger.info((System.currentTimeMillis()-startAll)/(1000*60)+"分钟");
   }
   
   private static String extractGameId(String href){
	   Pattern pattern = Pattern.compile("\\d+");
	   Matcher matcher = pattern.matcher(href);
	   if(matcher.find()){
		   return matcher.group();
	   }
	   return "";
   }
   
   

}
