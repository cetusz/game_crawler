package com.coship.game.crawler.utils;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;


/**
 * 基于httpclient 4.x版本的http工具类
 * @author 907708
 *
 */
public class NHttpUtil {

	
	public static String doGet(String urlstr,String chartSet,String referer){
		CloseableHttpClient client = HttpClients.createDefault();
		RequestConfig requestConfig = RequestConfig.custom()
				   .setSocketTimeout(5000)
				   .setConnectTimeout(5000)
				   .build();//设置请求和传输超时时间
	    String result = "";
	    HttpGet httpget = null;
		try {
			URL url = new URL(urlstr);
			URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
			httpget = new HttpGet(uri);
			httpget.setConfig(requestConfig);
	        httpget.setHeader("Cache-Control", "max-age=0");  
			httpget.setHeader("Connection", "keep-alive");
			httpget.setHeader("Connection", "close"); 
			httpget.setHeader("User-Agent","Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.116 Safari/537.36");
			if(StringUtils.isNotBlank(referer)){
				httpget.setHeader("Referer",referer);
			}
			httpget.setHeader("X-Requested-With","XMLHttpRequest");
	        HttpResponse response = client.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
	            InputStream instream = entity.getContent();
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int i = -1;
				while ((i = instream.read()) != -1) {
					baos.write(i);
				}
				result = baos.toString(chartSet);
				instream.close();
				baos.close();
	        }
			client.close();
		    return result;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}finally{
			if(httpget!=null)
				httpget.releaseConnection();
		}
		return "";
                   
	}
	
	public static String doGetWithRetry(String url,String chartSet,String referer){
		String content = doGet(url,chartSet,referer);
		if(StringUtils.isBlank(content)){
			for(int index=1;index<=3;index++){
				content = doGet(url,chartSet,referer);
				if(StringUtils.isNotBlank(content)){
					break;
				}
			}
		}
		return content;
	}
	
	public static void main(String[] args) throws URISyntaxException, MalformedURLException {
		System.out.println(doGet("http://www.aipai.com/app/www/apps/gameAreaInfo.php?callback=jsonp&data={\"gameid\":2429,\"page\":12,\"pageSize\":10}&action=getWork","utf-8",null));
	}

}
