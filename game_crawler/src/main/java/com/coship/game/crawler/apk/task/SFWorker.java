package com.coship.game.crawler.apk.task;

import org.jsoup.nodes.Document;

import com.coship.game.crawler.apk.processor.SFProcessor;
import com.coship.game.crawler.domain.APKEntity;
import com.coship.game.crawler.thread.Worker;
import com.coship.game.crawler.utils.JsoupUtil;

public class SFWorker extends Worker<String> {

	@Override
	protected Object handler(String url){
		Document doc = JsoupUtil.getDocumentWithRetry(url);
		SFProcessor process = new SFProcessor("sf");
		process.setDoc(doc);
		System.out.println(Thread.currentThread().getName());
		APKEntity entity = process.extract();
		if(entity.getApkUrl()==null){
			return null;
		}
		return entity;
	}
}
