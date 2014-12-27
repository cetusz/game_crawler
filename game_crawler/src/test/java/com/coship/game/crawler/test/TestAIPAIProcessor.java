package com.coship.game.crawler.test;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;

import junit.framework.TestCase;

import com.coship.game.crawler.utils.HttpUtils;
import com.coship.game.crawler.video.task.AIPAIExecutor;

public class TestAIPAIProcessor extends TestCase{

	public void testExtractSWF(){
		String content = HttpUtils.getInstance().doGetWithReconnect("http://www.aipai.com/app/www/apps/gameAreaInfo.php?callback=jsonp&data={%22gameid%22:28418,%22page%22:1,%22pageSize%22:1000}&action=getWork", "utf-8", null);
		String data = content.substring(6, content.length()-2);
		System.out.println(data);
	}
	
	public void testRunTask() throws JSONException{
		AIPAIExecutor executor = new AIPAIExecutor();
		executor.executeAll();
	}
	
	public void testStringConvert() throws UnsupportedEncodingException{
		String  str = "game_crawler/apk/shafa/å°é¸é£é£/picture/31c05b1e7018e358d434a13fed34ecc7.jpg";
		System.out.println(new String(str.getBytes("iso-8859-1"),"utf-8"));
	}
	
}
