package com.coship.game.crawler.domain;

import java.util.List;

/**
 * 
 * @author 909191
 *
 */
public class VideoEntity extends BaseEntityPrimaryKey{
	
	//视频名称
	private String videoName;
	//视频地址
	private String videoUrl;
	//来源
	private String sourceName;
	//更新时间
	private String updateDate;
	//视频截图
	private PictureEntity picture;
	//视频真实地址
	private String realFileUrl;
	
	private String sourceId;

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public PictureEntity getPicture() {
		return picture;
	}

	public void setPicture(PictureEntity picture) {
		this.picture = picture;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getRealFileUrl() {
		return realFileUrl;
	}

	public void setRealFileUrl(String realFileUrl) {
		this.realFileUrl = realFileUrl;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	
	
	
	

	
	
}
