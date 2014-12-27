package com.coship.game.crawler.video.processor;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.coship.game.crawler.domain.PictureEntity;
import com.coship.game.crawler.domain.VideoEntity;
import com.coship.game.crawler.thread.Worker;
import com.coship.game.crawler.utils.Constants.PictureSourceType;
import com.coship.game.crawler.utils.Constants.VideoSource;



public class AipaiVideoProcessor extends Worker<JSONObject>{
	
	Logger logger = Logger.getLogger(AipaiVideoProcessor.class);
	JSONObject json;
	
	public AipaiVideoProcessor(String workerName){
		this.workerName = workerName;
	}
	
	public AipaiVideoProcessor(JSONObject json){
		this.json = json;
	}
	
	

	public void pre() {
	}

	public String getVideoName(){
		try {
			return URLDecoder.decode(json.getString("title"), "utf-8");
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	public String getVideoUrl() {
		try {
			return json.getString("url");
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}

	public String getSourceName() {
		return VideoSource.AIPAI.getName();
	}
	
	public String getSnapShot(){
		try {
			return json.getString("thumbFileName");
		} catch (Exception e) {
			logger.error(e);
		}
		return "";
	}
	
	public String getVideoRealFileUrl(){
		try {
			if(json.has("flvFileName")){
				return json.getString("baseURL")+json.getString("flvFileName");
			}
			
		} catch (Exception e) {
			logger.error(e);
		}
		return "";
	}
	

	protected void post() {
		json = null;
	}
	
	protected  String getUpdateDate(){
		return "";
	}
	
	protected  PictureEntity getPicture(){
		PictureEntity picture = new PictureEntity();
		try {
			picture.setPictureType(PictureSourceType.VIDEO.getValue());
			picture.setSourceUrl(json.getString("thumbFileName"));
		} catch (Exception e) {
			logger.error(e);
		}
		return picture;
	}
	
	public VideoEntity extract(){
		VideoEntity video=new VideoEntity();
		video.setVideoName(getVideoName());
		video.setVideoUrl(getVideoUrl());
		video.setSourceName(getSourceName());
		video.setRealFileUrl(getVideoRealFileUrl());
		video.setPicture(getPicture());
		/*video.setSnapShotUrl(getSnapShot());
		video.setVideoRealFileUrl(getVideoRealFileUrl());*/
		post();
		return video;
	}
	
	@Override
	protected Object handler(JSONObject object){
		logger.info("aipai running");
		json = object;
		if(json!=null){
			return extract();
		}
		return null;
	}


}
