package com.coship.game.crawler.video.processor;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.coship.game.crawler.thread.Worker;
import com.coship.game.crawler.utils.NHttpUtil;

public class AipaiUrlListWorker extends Worker<String> {
	Logger logger = Logger.getLogger(AipaiUrlListWorker.class);

	public AipaiUrlListWorker(String workerName){
		super(workerName);
	}
	
	
	protected List<JSONObject> handler(String entry){
		  logger.info("aipai running");
		  List<JSONObject> jsons = new ArrayList<JSONObject>();
		  int pageIndex = 1;
		  String pageUrl = String.format(entry,pageIndex++);
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
							jsons.add(workObject);
						}
						workObject = null;
					}
					content = null;
					json = null;
				} catch (JSONException e) {
					e.printStackTrace();
				}
				pageUrl = String.format(entry,pageIndex++);
				content = NHttpUtil.doGetWithRetry(pageUrl, "utf-8", null);
		}
		return jsons;
	}

}
