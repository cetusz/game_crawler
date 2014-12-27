package com.coship.game.crawler.video.task;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.poi.hssf.record.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coship.game.crawler.domain.PictureEntity;
import com.coship.game.crawler.domain.VideoEntity;
import com.coship.game.crawler.service.IPictureEntityService;
import com.coship.game.crawler.service.IVideoService;
import com.coship.game.crawler.thread.Master;
import com.coship.game.crawler.thread.Worker;
import com.coship.game.crawler.utils.ConfigFactory;
import com.coship.game.crawler.utils.Constants;
import com.coship.game.crawler.utils.FtpUtil;
import com.coship.game.crawler.utils.HttpUtils;
import com.coship.game.crawler.utils.Md5Util;
import com.coship.game.crawler.utils.Constants.Status;
/**
 * 
 * @author 909191
 *
 */
@Component
public class BaseExecutor {
	@Autowired
	private IVideoService videoService;
	
	@Autowired
	private IPictureEntityService pictureService;
	
	protected <T> void  postProcess(Worker<T> worker, List<T> urls) {
		int threadCounts = ConfigFactory.getInt(Constants.THREADCOUNTS, 5);
		// 开启master-work模式执行任务
		final Master<T> master = new Master<T>(worker, threadCounts);
		master.addBatchJob(urls);
		master.execute();
		final Map<String, Object> resultMap = master.getResultMap();
		while (!master.isComplete() || resultMap.size() > 0) {
			Iterator<String> it = resultMap.keySet().iterator();
			VideoEntity entity = null;
			if (it.hasNext()) {
				String key = (String) it.next();
				entity = (VideoEntity) resultMap.get(key);
				resultMap.remove(key);
				process(entity);
			}
		}
	}
	
	protected void process(VideoEntity entity){
		String ftpBasePath = ConfigFactory.getString(Constants.FTPPATH)+"/video";
		ftpBasePath += "/"+entity.getSourceName()+"/picture";
		String ftpHost = ConfigFactory.getString(Constants.FTPHOST);
		int ftpPort = ConfigFactory.getInt(Constants.FTPPORT);
		String user = ConfigFactory.getString(Constants.FTPUSER);
		String pwd = ConfigFactory.getString(Constants.FTPPWD);
		FTPClient client = FtpUtil.connectFTPServer(ftpHost, ftpPort, user, pwd);
		entity.setSourceId(Md5Util.Md5(entity.getVideoUrl()));
		saveVideo(entity);
		if(entity.getId()>0){
			downloadPictureAndUpload(client,ftpBasePath,entity.getPicture(),entity.getId());
		}
		
		
		
	}
	
	/**
	 * 保存视频信息
	 * @param entity
	 * @return
	 */
	private long saveVideo(VideoEntity entity){
		VideoEntity query=new VideoEntity();
		query.setSourceId(Md5Util.Md5(entity.getVideoUrl()));
		List<VideoEntity> list=videoService.selectList(query, null);
		if(list!=null&&list.size()>0){
			entity.setId(list.get(0).getId());
		}
		return videoService.saveOrUpdate(entity);
	}
	
	/**
	 * 下载，上传图片
	 * @param client
	 * @param serverPath 服务器存放图片路径
	 * @param picture
	 * @param resourceId
	 */
	private void downloadPictureAndUpload(FTPClient client,String serverPath,PictureEntity picture,long resourceId){ 
		 //下载到本地的图片口路径
		 if(StringUtils.isBlank(picture.getSourceUrl())){
			 return;
		 }
		 String pictureLocalPath = ConfigFactory.getString(Constants.PICTURELOCALPATH);
		 picture.setResourceId(resourceId);
		 picture.setSourceType(Constants.PictureSourceType.VIDEO.getCode());
		 String md5Str = Md5Util.Md5(picture.getSourceUrl());
		 String fileName = md5Str;
		 //给个md5的加密串作为唯一标识
		 picture.setSourceId(md5Str);
		 String picturePath =  serverPath;
		 File downloadFile = HttpUtils.downloadFile(picture.getSourceUrl(), pictureLocalPath, fileName+".jpg",-1);
		 try{
			 boolean uploadSuccess = FtpUtil.uploadFile(client, downloadFile,picturePath, downloadFile.getName());
			 if(uploadSuccess){
				 picture.setStatus(Status.SUCCESS.getValue());
				 picture.setServerUrl(picturePath+"/"+fileName+".jpg");
			 }else{
				 picture.setStatus(Status.FAILURE.getValue());
			 }
		 }catch(Exception ex){
			 picture.setStatus(Status.FAILURE.getValue());
		 }
		 PictureEntity query = new PictureEntity();
		 //query.setSourceUrl(picture.getSourceUrl());
		 //根据唯一标识来查
		 query.setSourceId(md5Str);
		 query.setPictureType(picture.getPictureType());
		 query.setSourceType(picture.getSourceType());
		 List<PictureEntity> pictureList = pictureService.selectList(query, null);
		 if(pictureList.size()>0){
			 picture.setId(pictureList.get(0).getId());
		 }
		 pictureService.saveOrUpdate(picture);
	}
}
