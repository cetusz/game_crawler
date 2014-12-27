package com.coship.game.crawler.apk.processor;

import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;

import com.coship.game.crawler.domain.APKEntity;
import com.coship.game.crawler.domain.PictureEntity;
import com.coship.game.crawler.processor.AbstractProcessor;
import com.coship.game.crawler.utils.JsoupUtil;
/**
 * 页面处理
 * @author 907708
 *
 */
public abstract class AbstractAPKProcessor extends AbstractProcessor{
	
	public AbstractAPKProcessor(String workerName){
		super(workerName);
	}
	
	private Logger log=Logger.getLogger(this.getClass());
	
	protected APKEntity apkEntity;
	//预处理方法
	protected abstract void preProcess();
	protected abstract String extractName();
    protected abstract String extractOperationWay();
    protected abstract String extractType();
	protected abstract String extractIntroduce();
	protected abstract String extractSize();
	protected abstract String extractUpdateDate();
	protected abstract String extractLanguage();
	protected abstract String extractVersion();
	protected abstract String extractAndroidSystemVersion();
	protected abstract String extractDownloadTimes();
	protected abstract String extractSourceId();
	protected abstract String extractApkUrl();
	protected abstract String extractSourceName();
	protected abstract List<PictureEntity> extractPictures();
	//后置方法
	protected abstract void postProcess();
	
	public APKEntity extract(){
		preProcess();
		long start = System.currentTimeMillis();
		apkEntity = new APKEntity();
		apkEntity.setName(extractName());
		apkEntity.setOperationWay(extractOperationWay());
		apkEntity.setType(extractType());
		apkEntity.setIntroduce(extractIntroduce());
		apkEntity.setSize(extractSize());
		apkEntity.setUpdateDate(extractUpdateDate());
		apkEntity.setLanguage(extractLanguage());
		apkEntity.setVersion(extractVersion());
		apkEntity.setAndroidSystemVersion(extractAndroidSystemVersion());
		apkEntity.setDownloadTimes(extractDownloadTimes());
		apkEntity.setSourceId(extractSourceId());
		apkEntity.setApkUrl(extractApkUrl());
		//apkEntity.setApkUrl("http://www.sonatype.com/system/images/W1siZiIsIjIwMTQvMTEvMTQvMTMvNDEvMTUvNDI1L2Rvd25sb2FkX3BhbmVsXzEuanBnIl1d/download-panel-1.jpg");
		apkEntity.setSourceName(extractSourceName());
		apkEntity.setPictures(extractPictures());
		postProcess();
		log.info("解析"+extractSourceName()+"网["+extractName()+detailUrl+"]详情页耗时:"+(System.currentTimeMillis()-start)+"ms");
		System.out.println(Thread.currentThread().getName());
		return apkEntity;
	}
	
	@Override
	protected Object handler(String url){
		doc = JsoupUtil.getDocumentWithRetry(url);
		detailUrl = url;
		APKEntity entity;
		try{
		 entity = extract();
		if(entity.getApkUrl()==null){
			return null;
		}
		}catch(Exception ex){
			return null;
		}
		return entity;
	}
    
    

}
