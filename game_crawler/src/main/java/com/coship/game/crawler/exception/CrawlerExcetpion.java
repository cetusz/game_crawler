package com.coship.game.crawler.exception;

public class CrawlerExcetpion extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CrawlerExcetpion(String msg){
		super(msg);
	}
	
	public CrawlerExcetpion(String msg,Throwable cause){
		super(msg,cause);
	}
}
