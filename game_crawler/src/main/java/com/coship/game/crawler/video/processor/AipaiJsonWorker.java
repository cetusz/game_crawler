package com.coship.game.crawler.video.processor;

import com.coship.game.crawler.thread.Worker;
import com.coship.game.crawler.utils.HttpUtils;

public class AipaiJsonWorker extends Worker<String> {

	public AipaiJsonWorker(String workerName){
		super(workerName);
	}
	
	@Override
	protected String handler(String url){
		String content = HttpUtils.getInstance().doGetWithReconnect(url, "utf-8", null);
		System.out.println("处理:"+url);
		return content;
	}
}
