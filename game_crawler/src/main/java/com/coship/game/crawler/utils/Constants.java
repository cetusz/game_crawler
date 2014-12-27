package com.coship.game.crawler.utils;

public class Constants {
	
	public final static String URLDELIMITER = ";";
	public final static String PICTUREPATH = "picture";
	public final static String APKPATH = "apk";
	public final static String PICTUREFTPPATH = "picture.ftpPath";
	public final static String PICTURELOCALPATH="picture.localSavePath";
	public final static String APKFTPPATH = "apk.ftpPath";
	public final static String APKLOCALPATH = "apk.localPath";
	public final static String FTPUSER = "ftp.user";
	public final static String FTPPWD = "ftp.pwd";
  	public final static String FTPHOST = "ftp.host";
    public final static String FTPPORT = "ftp.port";
    public final static String FTPPATH = "ftp.uploadBasePath";
    public final static String FTPDOWNLOADTMPDIR = "ftp.downloadTmpDir";
    public final static String APK_FETCH_ALL="fetch.apkFetchAll";
    public final static String VIDEO_FETCH_ALL="fetch.videoFetchAll";
    public final static String APK_FETCH_DAY = "fetch.apkFetchDay";
    public final static String VIDEO_FETCH_DAY = "fetch.videoFetchDay";
    public final static String THREADCOUNTS = "fetch.threadCounts";
    public final static String DANGBEI_HOMEURL = "network.dangbei.homeUrl";
    public final static String DANGBEI_FETCHURL = "network.dangbei.fetchUrl";
    public final static String QIPO_FETCHURL = "network.qipo.fetchUrl";
    public final static String TGBUS_FETCHURL = "network.tgbus.fetchUrl";
    
    public static enum APKSource{
    	DANGBEI("dangbei"),
    	QIPO("7po"),
    	SHAFA("shafa");
    	String name;
    	private APKSource(String name){
    	  this.name = name;	
    	}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
    	
    }
    
    public static enum Status{
		INIT(0),SUCCESS(1),FAILURE(-1);
		private Integer value;
		private Status(Integer value){
			this.value = value;
		}
		public Integer getValue() {
			return value;
		}
		public void setValue(Integer value) {
			this.value = value;
		}
	}
    
    public static enum PictureType{
    	ICON("icon"),SNAPSHOT("snapshot");
    	String value;
    	private PictureType(String value){
    		this.value = value;
    	}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
    }
    
    public static enum PictureSourceType{
    	APK(1,"apk"),VIDEO(2,"video");
    	
    	private int code;
    	
    	private String value;
    	
    	private PictureSourceType(int code,String value){
    		this.code = code;
    		this.value = value;
    	}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
		
		
		
    }
    
    public static enum VideoSource{
    	AIPAI("aipai"),
    	TGBUS("tgbus");
    	private String name;
    	private VideoSource(String name){
    		this.name = name;
    	}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
    }
}
