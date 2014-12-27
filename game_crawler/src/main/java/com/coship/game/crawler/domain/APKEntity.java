package com.coship.game.crawler.domain;

import java.util.List;

public class APKEntity extends BaseEntityPrimaryKey{

	private String name;
	//操作方式
	private String operationWay;
	//类型 如：竞技体育
	private String type;
	//简介
	private String introduce;
	//文件大小 250M
	private String size;
	//更新日期 2014-12-03
	private String updateDate;
	//语言
	private String language;
	//版本号 v1.3
	private String version;
    //安卓系统版本
	private String androidSystemVersion;
	//下载次数
	private String downloadTimes;
	//源id 可以用md5加密地址作为唯一值
	private String sourceId;
	//apk下载地址
	private String apkUrl;
	//服务器地址
	private String serverPath;
	//来源
	private String sourceName;
	//图片列表 海报 icon都有
	private List<PictureEntity> pictures;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOperationWay() {
		return operationWay;
	}
	public void setOperationWay(String operationWay) {
		this.operationWay = operationWay;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getAndroidSystemVersion() {
		return androidSystemVersion;
	}
	public void setAndroidSystemVersion(String androidSystemVersion) {
		this.androidSystemVersion = androidSystemVersion;
	}
	public String getDownloadTimes() {
		return downloadTimes;
	}
	public void setDownloadTimes(String downloadTimes) {
		this.downloadTimes = downloadTimes;
	}
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	
	public String getApkUrl() {
		return apkUrl;
	}
	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}
	
	public String getServerPath() {
		return serverPath;
	}
	public void setServerPath(String serverPath) {
		this.serverPath = serverPath;
	}
	public List<PictureEntity> getPictures() {
		return pictures;
	}
	public void setPictures(List<PictureEntity> pictures) {
		this.pictures = pictures;
	}
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	
}
