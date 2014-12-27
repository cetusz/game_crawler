/**
 * 
 */
package com.coship.game.crawler.utils;


import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;





/**
 * 需要将FtpClientUtil和FtpClientCommon合并
 * @author Administrator
 *
 */
public class FtpUtil {
	private static Logger logger = Logger.getLogger(FtpUtil.class);
	
	public static FTPClient connectFTPServer(String host , int port, String username ,String password){
		
		FTPClient ftpClient = new FTPClient();
		try {			
			ftpClient.connect(host, port);
            int reply = ftpClient.getReplyCode();   
            if (!FTPReply.isPositiveCompletion(reply)) {   
            	ftpClient.disconnect();   
                logger.error("FTP服务器 [" + host +":" + port+ "]拒绝连接");   
                return null;
            }   
            
            if (!ftpClient.login(username, password)) {
            	ftpClient.disconnect();   
                logger.error("登陆[" + username +":" + port+ ","+ username +":" + username+ "]验证失败，请检查账号和密码是否正确");   
                return null;   
            } 
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
		} catch (SocketException e) {
            logger.error("FTP服务器 [" + host +":" + port+ "]拒绝连接",e);   
		} catch (IOException e) {
            logger.error("登陆[" + host +":" + port+ ","+ username +":" + password+ "]验证失败，请检查账号和密码是否正确",e);  
		} catch (Exception e){
			logger.error("登陆[" + host +":" + port+ ","+ username +":" + password+ "]验证失败，请检查账号和密码是否正确",e); 
		} 
		
		return ftpClient;
	}
	
	/**
	 * 从FTP移除文件
	 * @param ftpClient
	 * @param sourceFilename 相对FTP的根目录，绝对路径
	 * @param destFilename 相对FTP的根目录，绝对路径
	 */
	public static void rename(FTPClient ftpClient,String sourceFilename ,String destFilename){
		try {
			ftpClient.deleteFile(destFilename);
			boolean isSucc = ftpClient.rename(sourceFilename, destFilename);
			logger.info("从FTP服务器移动文件 [" +sourceFilename + " --> " +destFilename+ "] 操作结果为:" + isSucc);
		} catch (IOException e) {
			logger.error("从FTP服务器移动文件 [" +sourceFilename + " --> " +destFilename+ "] 失败" ,e );
		}
	}
	
	/**
	 * 
	 * @param ftpClient
	 * @param file
	 * @param remote 无需带路径分隔符
	 * @param remoteFile 无需带路径分隔符
	 */
	public static boolean upload(FTPClient ftpClient,File file ,String remote,String remoteFile)
	{
		InputStream is = null; 
		try 
		{
			is = new FileInputStream(file);
			FtpUtil.mkDir(ftpClient, remote);
			boolean isSucc = ftpClient.storeFile(remote+File.separator+remoteFile, is);
			logger.info("上传FTP 文件 [" +remote + " --> " +file+ "] 操作结果为:" + isSucc);
			return isSucc;
		} 
		catch (IOException e) 
		{
			logger.error("上传FTP 文件 [" +remote + " --> " +file+ "] 操作结果失败" ,e);
			return false;
		} 
		finally
		{
			IOUtils.closeQuietly(is);
		}
	}
	
	
	/**
	 * 
	 * @param ftpClient
	 * @param file 本地文件
	 * @param dir 上传文件存放的路径
	 * @param fileName 文件名称
	 */
	public static boolean uploadFile(FTPClient ftpClient,File file ,String dir,String fileName)
	{
		InputStream is = null; 
		try 
		{
			is = new FileInputStream(file);
			FtpUtil.fileMkDir(ftpClient, dir);
			boolean isSucc = ftpClient.storeFile(fileName, is);
			
			logger.info("上传FTP 文件 [" +dir + " --> " +fileName+ "] 操作结果为:" + isSucc);
			return isSucc;
		} 
		catch (IOException e) 
		{
			logger.error("上传FTP 文件 [" +dir + " --> " +fileName+ "] 操作结果失败" ,e);
			return false;
		} 
		finally
		{
			IOUtils.closeQuietly(is);
		}
	}
	
	
	/**
	 * 
	 * @param ftpClient
	 * @param content
	 * @param remote 无需带路径分隔符
	 * @param remoteFile 无需带路径分隔符
	 */
	public static boolean upload(FTPClient ftpClient,String content ,String remote,String remoteFile)
	{
		InputStream is = null; 
		try 
		{
			is = new ByteArrayInputStream(content.getBytes());
			FtpUtil.mkDir(ftpClient, remote);
			String remoteFileName = remote+File.separator+remoteFile;
			boolean isSucc = ftpClient.storeFile(remoteFileName, is);
			logger.info("上传FTP 内容 [" +content + " --> " +remoteFileName+ "] 操作结果为:" + isSucc);
			return isSucc;
		} catch (IOException e) {
			logger.error("上传FTP 内容 [" +content + "] 操作结果失败" ,e);
			return false;
		}
		finally
		{
			IOUtils.closeQuietly(is);
		}
	}
	
	/**
	 * 删除FTP文件
	 * @param ftpClient
	 * @param pathname 文件相对路径
	 */
	public static boolean deleteFile(FTPClient ftpClient,String pathname){
		boolean success=true;
		try {
			success=ftpClient.deleteFile(pathname);
		} catch (IOException e) {
			success=false;
			logger.error("删除FTP文件路径[" +pathname + "] 操作结果失败" ,e);
		}
		return success;
	}
	
	
	public static File download(String fileFtpUrl,String targetFileName)
	{
		URI ftpUrl = null;
		try {
			ftpUrl = new URI(fileFtpUrl);
		} catch (URISyntaxException e) {
			logger.error("{}",e);
		}
		String userInfo = ftpUrl.getUserInfo();
		if(StringUtils.isEmpty(userInfo))
		{
			userInfo = ":";
		}
		String[] userApass = userInfo.split(":");
		FTPClient ftpClient = FtpUtil.connectFTPServer(ftpUrl.getHost(), ftpUrl.getPort()<0?21:ftpUrl.getPort(), 
				userApass[0], userApass[1]);
		OutputStream os = null;
		try {
			if(StringUtils.isEmpty(targetFileName))
			{
				targetFileName = String.valueOf(System.currentTimeMillis());
			}
			File targetFile = new File(targetFileName);
			os = new FileOutputStream(targetFile);
			ftpClient.retrieveFile(ftpUrl.getPath(), os);
			os.flush();
			return targetFile;
		} 
		catch (FileNotFoundException e) 
		{
            logger.error("ftp下载失败",e); 
		} 
		catch (IOException e) 
		{
            logger.error("ftp下载失败",e); 
		} 
		finally
		{
			IOUtils.closeQuietly(os);
			FtpUtil.closeConnect(ftpClient);
		}
		return null;
		//FtpUtil.connectFTPServer(host, port, username, password);
	}
	
	
	  /*** 
     * 下载文件 
     * @param remoteFileName   待下载文件名称 
     * @param localDires 下载到当地那个路径下 
     * @param remoteDownLoadPath remoteFileName所在的路径 
     * */  
  
    public static boolean downloadFile(FTPClient ftpCilent,String remoteFileName, String localDires,  
            String remoteDownLoadPath) {  
    	String localFileName = "";
    	try {
    		localFileName = new String(remoteFileName.getBytes("iso-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
        String strFilePath = localDires + localFileName;  
        BufferedOutputStream outStream = null;  
        boolean success = false;  
        try {  
        	ftpCilent.changeWorkingDirectory("/"+remoteDownLoadPath);  
            outStream = new BufferedOutputStream(new FileOutputStream(  
                    strFilePath));  
            logger.info(remoteFileName + "开始下载....");  
            success = ftpCilent.retrieveFile(remoteFileName, outStream);  
            if (success == true) {  
                logger.info(remoteFileName + "成功下载到" + strFilePath);  
                return success;  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
            logger.error(remoteFileName + "下载失败");  
        } finally {  
            if (null != outStream) {  
                try {  
                    outStream.flush();  
                    outStream.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        if (success == false) {  
            logger.error(remoteFileName + "下载失败!!!");  
        }  
        return success;  
    }  
    
	public static boolean downLoadDirectory(FTPClient ftpClient,String localDirectoryPath,String remoteDirectory) {  
        try {  
            String fileName = new File(remoteDirectory).getName();  
            fileName = new String(fileName.getBytes("iso-8859-1"),"utf-8");
            localDirectoryPath = localDirectoryPath + fileName + "/";  
            new File(localDirectoryPath).mkdirs();  
            FTPFile[] allFile = ftpClient.listFiles("/"+remoteDirectory);  
            for (int currentFile = 0; currentFile < allFile.length; currentFile++) {  
            	 if (!allFile[currentFile].isDirectory()) {  
                     downloadFile(ftpClient,allFile[currentFile].getName(),localDirectoryPath, remoteDirectory);  
                 }
            }  
            for (int currentFile = 0; currentFile < allFile.length; currentFile++) {  
                if (allFile[currentFile].isDirectory()) {  
                    String strremoteDirectoryPath = remoteDirectory + "/"+ allFile[currentFile].getName();  
                    downLoadDirectory(ftpClient,localDirectoryPath,strremoteDirectoryPath);  
                }  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
            logger.info("下载文件夹失败");  
            return false;  
        }  
        return true;  
    }  
	
	/**
	 * 获取串的协议
	 * @param url
	 * @return
	 */
	public static String getUrlScheme(String url)
	{
		try {
			URI uri = new URI(url);
			return uri.getScheme();
		} catch (URISyntaxException e) {
		}
		return null;
	}
	
	public static void main(String[] args) throws IOException {
		FTPClient ftpClient = FtpUtil.connectFTPServer("172.30.25.245", 21, "ftper", "ftper");
		///downLoadDirectory(ftpClient,"E:/apk/","game_crawler/apk/shafa");
		File file = new File("E:/testwrite.txt");
		FileOutputStream out = new FileOutputStream(file);
		out.write("测试写入内容".getBytes());
		out.close();
		FtpUtil.uploadFile(ftpClient,file,"game_crawler/apk",new String("中文测试.txt".getBytes("utf-8"),"iso-8859-1"));
		file.delete();
	}
//	
	public static void closeConnect(FTPClient ftpClient ){
		if(ftpClient!=null){
			try {
				if(ftpClient.isConnected()){
					ftpClient.disconnect();
				}
			} catch (IOException e) {
	            logger.error("关闭FTP连接失败",e); 
			} 
		}
	}
	
	public static boolean mkDir(FTPClient ftpClient, String dir){
		logger.info("待切换的FTP目录为:" + dir);
		String pathChar = "/";
		if(dir.contains("\\")){
			dir = dir.replace("\\", pathChar);
		}
	
		try {
			if(pathChar.equalsIgnoreCase(dir)){
				return true;
			}
			if(!ftpClient.changeWorkingDirectory(dir)){
				String[] dirArray = dir.split(pathChar);
				for(String dirTmp : dirArray){
					if(!StringUtils.isEmpty(dirTmp)&&!ftpClient.changeWorkingDirectory(dirTmp)){//不存在
						if(ftpClient.makeDirectory(dirTmp)){
							logger.info("从FTP服务器创建目录" + dirTmp + "成功");
							ftpClient.changeWorkingDirectory(dirTmp);
						}
						else{
							logger.info("从FTP服务器创建目录" + dirTmp + "失败");
							return false;
						}
					}
				}
			}
			ftpClient.changeWorkingDirectory("/");//切回根目录
		} catch (IOException e) {
            logger.error("FTP切换目录失败",e); 
            return false;
		}
		return true;
	}
	
	/**
	 * 创建目录
	 * @param ftpClient
	 * @param dirPath 目录名
	 */
	public static boolean fileMkDir(FTPClient ftpClient, String dirPath){
		try {
			ftpClient.changeWorkingDirectory("/");//切回根目录
			String[] dirs = dirPath.split("/");
			String currentPath = "/";
			for(int index=0,len=dirs.length;index<len;index++){
				currentPath = dirs[index];
				if(!ftpClient.changeWorkingDirectory(dirs[index])){
					try {
						ftpClient.makeDirectory(dirs[index]);
					} catch (Exception e) {
						logger.info("从FTP服务器创建目录" + currentPath + "失败", e);
					}
					ftpClient.changeWorkingDirectory(currentPath);
				}
			}
		} catch (IOException e) {
			logger.error("FTP切换目录失败",e); 
		}
		return true;
	}
	
	
	
	
}
