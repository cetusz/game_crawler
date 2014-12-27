package com.coship.game.crawler.video.processor;

import org.apache.log4j.Logger;

import com.coship.game.crawler.domain.PictureEntity;
import com.coship.game.crawler.domain.VideoEntity;
import com.coship.game.crawler.processor.AbstractProcessor;
import com.coship.game.crawler.utils.JsoupUtil;
/**
 * 
 * @author 909191
 *
 */
public abstract class AbstractVideoProcessor extends AbstractProcessor{
	
	private Logger log=Logger.getLogger(this.getClass());

	VideoEntity videoEntity;
	public AbstractVideoProcessor(String workerName) {
		super(workerName);
	}
	
	protected abstract void pre();
	
	protected abstract String getVideoName();
	
	protected abstract String getVideoUrl();
	
	protected abstract String getSourceName(); 
	
	protected abstract String getUpdateDate();
	
	protected abstract String getRealFileUrl();
	
	protected abstract PictureEntity getPicture();
	
	protected abstract void post();
	
	public VideoEntity extract(){
		pre();
		VideoEntity video=new VideoEntity();
		video.setVideoName(getVideoName());
		video.setVideoUrl(getVideoUrl());
		video.setSourceName(getSourceName());
		video.setUpdateDate(getUpdateDate());
		video.setRealFileUrl(getRealFileUrl());
		video.setPicture(getPicture());
		post();
		return video;
	}

	@Override
	protected Object handler(String url) {
		try {
			doc = JsoupUtil.getDocumentWithRetry(url);
			detailUrl = url;
			videoEntity = extract();
			if(videoEntity.getVideoUrl()==null){
				log.info("["+videoEntity.getVideoName()+"]视频地址为空!");
				return null;
			}
		} catch (Exception e) {
			log.info("解析详情页url:["+url+"]出现异常",e);
		}
		return videoEntity; 
	}
	
	
	
	
	
	

}
