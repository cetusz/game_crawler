package com.coship.game.crawler.domain;

public class PictureEntity extends BaseEntityPrimaryKey{

	//资源Id
	private Long resourceId;
	//源地址
	private String sourceUrl;
	//保存后服务器地址
	private String serverUrl;
	//图片类型
	private String pictureType;
	
	private Integer status;
	
	//资源类型1.apk,2.video
	private Integer sourceType;
	
	private String sourceId;
	
	public String getSourceUrl() {
		return sourceUrl;
	}
	
	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}
	public String getServerUrl() {
		return serverUrl;
	}
	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}
	
	
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getPictureType() {
		return pictureType;
	}

	public void setPictureType(String pictureType) {
		this.pictureType = pictureType;
	}

	public Integer getSourceType() {
		return sourceType;
	}

	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	
	
	
	
	

}
