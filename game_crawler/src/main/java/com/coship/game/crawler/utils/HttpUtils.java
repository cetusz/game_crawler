package com.coship.game.crawler.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;




public class HttpUtils {
	static Logger log = Logger.getLogger(HttpUtils.class);
	
	private static final String COOKIE="bid=\"dprHP/2aPyc\"; ll=\"118282\"; __utma=30149280.1649332086.1375150457.1375150457.1375150457.1; __utmb=30149280.2.10.1375150457; __utmc=30149280; __utmz=30149280.1375150457.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)";
	private static HttpUtils httpUtils = null;
	private HttpUtils(){
		
	}
	public static HttpUtils getInstance(){
		if(httpUtils==null)
			return new HttpUtils();
		return httpUtils;
	}
	public String doGet(String url,String charset,String referer) {
		HttpClient client = new HttpClient(); // 实例化httpClient
		HttpMethod method = new GetMethod(url); //
		method.addRequestHeader("Cache-Control", "max-age=0");  
		method.addRequestHeader("Connection", "keep-alive");
		//method.addRequestHeader("Cookie", COOKIE.replaceFirst("dprHP", String.valueOf(Math.round((Math.random()*1000000000))+Math.round(Math.random()*10000000)))+"");
		method.addRequestHeader("User-Agent","Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.116 Safari/537.36");
		if(StringUtils.isNotBlank(referer)){
			method.addRequestHeader("Referer",referer);
		}
		method.addRequestHeader("X-Requested-With","XMLHttpRequest");
		//使用系统提供的默认的恢复策略
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
			    new DefaultHttpMethodRetryHandler()); 
		client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		client.getHttpConnectionManager().getParams().setSoTimeout(5000); 
		String responseContent = "";
		try {
			int stateCode = client.executeMethod(method); // 执行
			if(stateCode != HttpStatus.SC_OK){
				log.error("Method failed: " + method.getStatusLine()+" url:["+url+"]");
				return "error:"+stateCode;
			}
			InputStream jsonStr;
			jsonStr = method.getResponseBodyAsStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int i = -1;
			while ((i = jsonStr.read()) != -1) {
				baos.write(i);
			}
			responseContent = baos.toString(charset);
			jsonStr.close();
			baos.close();
			method.releaseConnection();
			if(log.isDebugEnabled()){
				log.debug("成功解析:"+url);
			}
		} catch (HttpException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return responseContent;
	}
	
	public  String doPost(String url,Map<String,String> params,String charset) {
		HttpClient client = new HttpClient(); // 实例化httpClient
		PostMethod method = new PostMethod(url); //	
		client.getHttpConnectionManager().getParams().setConnectionTimeout(30000);
		client.getHttpConnectionManager().getParams().setSoTimeout(30000);
		method.addRequestHeader("Cache-Control", "max-age=0");  
		method.addRequestHeader("Connection", "keep-alive");
		method.addRequestHeader("Cookie", COOKIE.replaceFirst("dprHP", String.valueOf(Math.round((Math.random()*1000000000))+Math.round(Math.random()*10000000)))+"");
		method.setRequestHeader("User-Agent","Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.116 Safari/537.36");
		if (params != null) {
			 StringPart[] parts=new StringPart[params.size()];
		     int i=0;           
             for (Map.Entry<String, String> entry : params.entrySet()) {
            	 parts[i]=new StringPart(entry.getKey(), entry.getValue());
            	 i++;
             }
             method.setRequestEntity(new MultipartRequestEntity(parts, method.getParams()));
		 }
			
		String responseContent = "";
		try {
			int stateCode = client.executeMethod(method); // 执行
			if(stateCode != HttpStatus.SC_OK){
				log.error("Method failed: " + method.getStatusLine());
				return "error:"+stateCode;
			}
			InputStream jsonStr;
			jsonStr = method.getResponseBodyAsStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int i = -1;
			while ((i = jsonStr.read()) != -1) {
				baos.write(i);
			}
			responseContent = baos.toString(charset);
			jsonStr.close();
			baos.close();
			method.releaseConnection();
			if(log.isDebugEnabled()){
				log.debug("成功解析:"+url);
			}
		} catch (HttpException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return responseContent;
	}
	public String doGetWithReconnect(String url,String charset,String referer){
		String content = doGet(url,charset,referer);
		//检测重连
		if(StringUtils.isEmpty(content)){
			for(int i=0;i<3;i++){
				content = HttpUtils.getInstance().doGet(url, "utf-8",referer);
				if(StringUtils.isNotEmpty(content)){
					break;
				}
			}
		}
		return content;
	}
	
	
	 /**
     * 文件下载，存放当前项目的根目录
     * @param httpUrl 文件URL
     * @param maxSize 文件大小 字节  小于0 则下载不限制大小
     * @return
     */
    public static File downloadFile(String httpUrl, String toPath ,String fileName,long maxSize) {
        File resultFile = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        HttpURLConnection httpconn = null;
        try 
        {
            /**
             * 重构 关闭HTTP连接
             */
            int HttpResult = 0; // 服务器返回的状态
            URL postHttpUrl = new URL(httpUrl); // 创建URL
            URLConnection urlconn = postHttpUrl.openConnection(); // 试图连接并取得返回状态码urlconn.connect();
            urlconn.setConnectTimeout(10000);//10秒超时
            urlconn.setReadTimeout(30000);//30秒读超时
            urlconn.setDoOutput(true);
            
            httpconn = (HttpURLConnection) urlconn;
            HttpResult = httpconn.getResponseCode();

            if (HttpResult != HttpURLConnection.HTTP_OK) 
            { // 不等于HTTP_OK说明连接不成功
                log.error(httpUrl + "：连接不成功");
                return null;
            } 
            else 
            {
                long filesize = urlconn.getContentLength(); // 取数据长度
                log.debug("文件filesize = " + filesize);
                
                //maxSize大于0则进行文件下载校验,小于0不校验大小
                if (filesize > maxSize && maxSize >0) 
                {
                    log.error("要下载的文件太大，超过maxSize=" + maxSize + "chars");
                    return null;
                }
                bis = new BufferedInputStream(urlconn.getInputStream());
                if(StringUtils.isNotEmpty(toPath)){
                	if(!toPath.endsWith("/")){
                		toPath += "/";
                	}
                	
                	File dirs = new File(toPath);
                	if(!dirs.exists()){
                		 dirs.mkdirs();
                     }
                	resultFile = new File(toPath+"/"+fileName);
                }else{
                	resultFile = new File(httpUrl.substring(httpUrl.lastIndexOf("/")+1));
                }
               
                bos = new BufferedOutputStream(new FileOutputStream(resultFile));
                byte[] buffer = new byte[1024]; // 创建存放输入流的缓冲
                int num = 0; // 读入的字节数
                num = bis.read(buffer);
                while (num != -1) 
                {
                    bos.write(buffer, 0, num);
                    bos.flush();
                    num = bis.read(buffer); // 读入到缓冲区
                }
            }
        } 
        catch (Exception e) 
        {
            log.error("下载文件失败， 地址为： " + httpUrl,e);
            return null;
        } 
        finally 
        {
            IOUtils.closeQuietly(bos);
            IOUtils.closeQuietly(bis);

            // 关闭http连接
            if (httpconn != null) {
                httpconn.disconnect();
            }
        }

        return resultFile;
    }
    
    /**
     * 根据源地址获取跳转后的地址
     * @param entrySourceUrl
     * @return
     */
    public String getRedirectUrl(String entrySourceUrl){
    	HttpClient client = new HttpClient(); // 实例化httpClient
		HttpMethod method = new GetMethod(entrySourceUrl); //
		method.addRequestHeader("Cache-Control", "max-age=0");  
		method.addRequestHeader("Connection", "keep-alive");
		method.addRequestHeader("User-Agent","Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.116 Safari/537.36");
		//使用系统提供的默认的恢复策略
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
			    new DefaultHttpMethodRetryHandler()); 
		client.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
		client.getHttpConnectionManager().getParams().setSoTimeout(10000); 
		try {
			int stateCode = client.executeMethod(method); // 执行
			if(stateCode != HttpStatus.SC_OK){
				return "";
			}
			return method.getURI().toString();
		} catch (HttpException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		}finally{
			method.releaseConnection();
		}
		return "";
    	
    }
    
    
    public String getRedirectUrlWithRetry(String entrySourceUrl){
    	String result = getRedirectUrl(entrySourceUrl);
    	if(StringUtils.isBlank(result)){
    		for(int i=0;i<3;i++){
    			result = getRedirectUrl(entrySourceUrl);
    			if(StringUtils.isNotBlank(result)){
    				break;
    			}
    		}
    	}
    	return result;
    }
    
	
	public static void main(String[] args) {
		String url = "http://app.shafa.com/api/push/download/54818c8c726e09bb51b041d4?response-content-type=application%2fvnd.android.package-archive";
		
	
		
	}
	


}
