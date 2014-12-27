package com.coship.game.crawler.apk.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.coship.game.crawler.domain.APKEntity;
import com.coship.game.crawler.domain.PictureEntity;
import com.coship.game.crawler.service.IAPKEntityService;
import com.coship.game.crawler.service.IPictureEntityService;
import com.coship.game.crawler.thread.Master;
import com.coship.game.crawler.thread.Worker;
import com.coship.game.crawler.utils.ConfigFactory;
import com.coship.game.crawler.utils.Constants;
import com.coship.game.crawler.utils.Constants.Status;
import com.coship.game.crawler.utils.FtpUtil;
import com.coship.game.crawler.utils.HttpUtils;
import com.coship.game.crawler.utils.Md5Util;
/**
 * 执行类基类
 * @author 907708
 *
 */
@Component("BaseExecutor")
public class BaseExecutor {
	@Autowired IAPKEntityService apkEntityService; 
	@Autowired IPictureEntityService pictureService;
	
	protected void postProcess(Worker<String> worker,List<String> urls){
        //开启master-work模式执行任务
		int threadCounts = ConfigFactory.getInt(Constants.THREADCOUNTS, 5);
		final Master<String> master = new Master<String>(worker,threadCounts);
		master.addBatchJob(urls);
		master.execute();
		final Map<String,Object> resultMap = master.getResultMap();
//		Executors.newFixedThreadPool(5).execute(new Runnable(){
//			@Override
//			public void run() {
				while(!master.isComplete()||resultMap.size()>0){
					Iterator<String> it=resultMap.keySet().iterator();
					APKEntity entity = null;
					if(it.hasNext()){
						String key=(String) it.next();
						entity = (APKEntity)resultMap.get(key);
						resultMap.remove(key);
						process(entity);
					}
				}
//			}
//		});
//		
	}
	/**
	 * 处理业务
	 * @param entity
	 */
	protected void process(APKEntity entity){
		long start = System.currentTimeMillis();
		System.out.println("处理:"+entity.getName());
		String ftpBasePath = ConfigFactory.getString(Constants.FTPPATH)+"/apk";
//		try {
//			ftpBasePath += "/"+entity.getSourceName()+"/"+new String(entity.getName().getBytes("utf-8"),"iso-8859-1");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		String ftpHost = ConfigFactory.getString(Constants.FTPHOST);
//		int ftpPort = ConfigFactory.getInt(Constants.FTPPORT);
//		String user = ConfigFactory.getString(Constants.FTPUSER);
//		String pwd = ConfigFactory.getString(Constants.FTPPWD);
//		//FTPClient client = FtpUtil.connectFTPServer(ftpHost, ftpPort, user, pwd);
		saveBaseInfoAndDownloadAPK(null,ftpBasePath,entity);
		downloadPictureAndUpload(null,ftpBasePath,entity.getPictures(),entity.getId());
		
		try {
			makeIntroduceFile(null,ftpBasePath,entity);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(entity.getName()+",处理时间:"+(System.currentTimeMillis()-start)/1000+"s");
	}
	
	private boolean saveBaseInfoAndDownloadAPK(FTPClient client,String parentPath,APKEntity apk){
		String apkLocalPath = ConfigFactory.getString(Constants.APKLOCALPATH);
		//Md5Util.Md5(apk.getApkUrl())
		if(StringUtils.isBlank(apk.getApkUrl()))
			return false;
		File downloadFile = HttpUtils.downloadFile(apk.getApkUrl(),apkLocalPath,apk.getName()+".apk",-1);
		
		boolean uploadSuccess = false;
		parentPath+="/apk";
		if(downloadFile!=null){
			//uploadSuccess = FtpUtil.uploadFile( client, downloadFile, parentPath, downloadFile.getName());
		}
		if(uploadSuccess){
			apk.setStatus(Status.SUCCESS.getValue());
			try {
				apk.setServerPath(parentPath+"/"+new String(downloadFile.getName().getBytes("iso-8859-1"),"utf-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(downloadFile!=null)
				 downloadFile.delete();
		}else{
			apk.setStatus(Status.FAILURE.getValue());
		}
		//基本信息保存成功后再上传图片
		APKEntity query = new APKEntity();
		query.setSourceName(apk.getSourceName());
		query.setSourceId(apk.getSourceId());
		List<APKEntity> apkList = apkEntityService.selectList(query, null);
		if(apkList.size()>0){
			apk.setId(apkList.get(0).getId());
		}
		//Long id = (Long)apkEntityService.saveOrUpdate(apk);
		//return id!=null&&uploadSuccess;
		return true;
	}
	
	/**
	 * 上传图片
	 * @param pictures
	 */
	private void downloadPictureAndUpload(FTPClient client,String parentFilePath,List<PictureEntity> pictures,Long resourceId){
		 String pictureLocalPath = ConfigFactory.getString(Constants.PICTURELOCALPATH);
		 for(PictureEntity picture:pictures){
			 picture.setResourceId(resourceId);
			 picture.setSourceType(Constants.PictureSourceType.APK.getCode());
			 String fileName = Md5Util.Md5(picture.getSourceUrl());
			 //用md5字符串作为唯一标示
			 picture.setSourceId(fileName);
			 String picturePath =  parentFilePath+"/picture";
			 File downloadFile = HttpUtils.downloadFile(picture.getSourceUrl(), pictureLocalPath, fileName+".jpg",-1);
//			 try{
//				 boolean uploadSuccess = FtpUtil.uploadFile(client, downloadFile,picturePath, downloadFile.getName());
//				 if(uploadSuccess){
//					 picture.setStatus(Status.SUCCESS.getValue());
//					 picture.setServerUrl(new String(picturePath.getBytes("iso-8859-1"),"utf-8")+"/"+fileName+".jpg");
//					 if(downloadFile!=null)
//						 downloadFile.delete();
//				 }else{
//					 picture.setStatus(Status.FAILURE.getValue());
//				 }
//			 }catch(Exception ex){
//				 picture.setStatus(Status.FAILURE.getValue());
//			 }
			 PictureEntity query = new PictureEntity();
			 //根据md5字符串来查
			 query.setSourceId(fileName);
			 query.setPictureType(picture.getPictureType());
			 query.setSourceType(picture.getSourceType());
			 List<PictureEntity> pictureList = pictureService.selectList(query, null);
			 if(pictureList.size()>0){
				 picture.setId(pictureList.get(0).getId());
			 }
			 //pictureService.saveOrUpdate(picture);
			}
	}
	
	private void makeIntroduceFile(FTPClient client,String parentPath,APKEntity entity) throws IOException{
		String apkLocalPath = ConfigFactory.getString(Constants.APKLOCALPATH);
		String fileName = new String(entity.getName().getBytes("utf-8"),"iso-8859-1")+".txt";
		File file = new File(apkLocalPath+"/"+fileName);
		FileOutputStream out = new FileOutputStream(file);
		out.write(entity.getIntroduce().getBytes());
		out.close();
		//FtpUtil.uploadFile(client, file, parentPath, fileName);
		file.delete();
		
	}

}
