package com.coship.game.crawler.test;

import java.io.UnsupportedEncodingException;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.coship.game.crawler.utils.HttpUtils;
import com.coship.game.crawler.utils.JsoupUtil;

public class TestShaFaExtractor extends TestCase{
  

	public void testExtractPageUrl() throws JSONException{
		String shafaAPI ="http://app.shafa.com/list/game?output=json&page=%d";
		String url = String.format(shafaAPI, 1);
		String content = HttpUtils.getInstance().doGetWithReconnect(url, "utf-8","");
		if(StringUtils.isNotBlank(content)){
			JSONObject json = new JSONObject(content);
		String htmlContent = json.getString("json_view");
		Document doc = Jsoup.parse(htmlContent);
		Elements els = doc.select("a[class=thumbnail-link]");
		Assert.assertTrue("解析正常", els.size()>0);
	   }
	}
	
	public void testExtractBaseInfo() throws Exception{
		Document doc = Jsoup.connect("http://app.shafa.com/apk/kuangbiaofalali.html").get();
		String name = JsoupUtil.getTextAndSpace("div[class=app-title]", doc, 0);
		Assert.assertEquals("狂飙法拉利TV版 Final Freeway 2R", name);
		String operateWay = JsoupUtil.getAttributeValue("div[class=app-rating]>span>a", "title", doc, 0);
		Assert.assertEquals("遥控器游戏", operateWay);
		String introduce = JsoupUtil.getText("div[itemprop=description]>div>p", doc, 0);
	    Assert.assertNotNull(introduce);
	    String apkIconUrl= JsoupUtil.getAttributeValue("section[class=page-header app-header]>div>div>img", "src", doc, 0);
	    Assert.assertEquals("http://img.sfcdn.org/1c349019bc82883f731b8d9c4be6820248fe51b3.png!medium.icon",apkIconUrl);
	    String type = JsoupUtil.getElementByCss("section[class=app-info]>div>div>div>div[class*=meta-info]", doc, 0).select("a").text();
	    Assert.assertEquals("竞速体育",type);
	    String size = JsoupUtil.getElementByCss("section[class=app-info]>div>div>div>div[class*=meta-info]", doc, 1).select("span").text();
	    Assert.assertEquals("17.71MB",size);
	    String downloadTimes = JsoupUtil.getElementByCss("section[class=app-info]>div>div>div>div[class*=meta-info]", doc, 2).select("span").text();
	    Assert.assertNotNull(downloadTimes);
	    String language = JsoupUtil.getElementByCss("section[class=app-info]>div>div>div>div[class*=meta-info]", doc, 3).select("span").text();
	    Assert.assertEquals("英语",language);
	    String version =JsoupUtil.getElementByCss("section[class=app-info]>div>div>div>div[class*=meta-info]", doc, 4).select("span").text();
	    Assert.assertEquals("1.8.10.0",version);
	    String updateTime = JsoupUtil.getElementByCss("section[class=app-info]>div>div>div>div[class*=meta-info]", doc, 5).select("span").text();
	    Assert.assertEquals("2014.11.11",updateTime);
	    String android = JsoupUtil.getElementByCss("section[class=app-info]>div>div>div>div[class*=meta-info]", doc, 6).select("span").text();
	    Assert.assertEquals("2.3, 2.3.1, 2.3.2 及以上",android);
	    Elements els = doc.select("section[class=row app-screenshots]>div>div[class=carousel-inner]>div[class*=item]");
	    for(Element el:els){
	    	Elements aels = el.select("a");
	    	for(Element ael:aels){
	    		System.out.println(ael.attr("href"));
	    	}
	    }
	    
	    String downloadKey = JsoupUtil.getAttributeValue("div[class=app-download]>button","data-app-id" ,doc, 1);
	    System.out.println(downloadKey);
	    String downloadUrlFormater = "http://app.shafa.com/api/push/download/%s?response-content-type=application/fvnd.android.package-archive";
	    String sourceUrl = String.format(downloadUrlFormater, downloadKey);
		System.out.println(HttpUtils.getInstance().getRedirectUrlWithRetry(sourceUrl));
	}
	
	public void testChinese() throws UnsupportedEncodingException{
		String str = "刀魂 SoulCalibur";
        System.out.println(new String(str.getBytes("utf-8")));
	}

}