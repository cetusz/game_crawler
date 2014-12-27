package com.coship.game.crawler.processor;

import org.jsoup.nodes.Document;

import com.coship.game.crawler.thread.Worker;

public abstract class AbstractProcessor extends Worker<String>{
	

	public AbstractProcessor(String workerName){
		super(workerName);
	}
	
	protected String detailUrl;
	
	protected Document doc;

	public String getDetailUrl() {
		return detailUrl;
	}

	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}

	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}
	
	public abstract Object extract();
	
	@Override 
	protected Object handler(String url){
	   return url;
	}
	


}
