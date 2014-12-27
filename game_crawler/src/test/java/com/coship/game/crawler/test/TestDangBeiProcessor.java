package com.coship.game.crawler.test;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.coship.game.crawler.domain.APKEntity;

public class TestDangBeiProcessor {
	
	
	public void execute(){
		try {
			Document doc=Jsoup.connect("http://www.dangbei.com/app/tv/list-2.html").get();
			String baseUrl=doc.getElementsByClass("headlogo").get(0).children().attr("href");
			Elements links=doc.getElementById("softList").getElementsByTag("li");
			
			String page=doc.getElementById("mainContent").select("span[class=pageinfo] >strong").get(0).text();
			System.out.println("page:"+page);
			
			String date=doc.select("#softList >li").get(0).select("font[class=infoVal]").get(1).text();
			System.out.println(date);
			String detailPageUrl="";
			String apkUrl="";
			for(Element e:links){
				detailPageUrl=e.getElementsByTag("a").get(0).attr("href");
				System.out.println("apkURL:"+apkUrl);
				parseHtml(baseUrl+detailPageUrl);
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void parseHtml(String url){
		APKEntity entity=null;
		try {
			Document doc=Jsoup.connect(url).get();
			//游戏内容主体
			Element bodyContent=doc.getElementById("softAbs");
			System.out.println("详情:"+doc.getElementById("mainContent").select("p[class=message]").text());
			//图标
			String iconPic=bodyContent.select("div[class=soft_img] >img").attr("src");
			//游戏信息
			Elements gameInfo=bodyContent.getElementsByTag("p");
			//apk
			String apk=bodyContent.getElementsByClass("fast_download").get(0).attr("href");
			//截图
			Elements bigPic=doc.getElementById("mainContent").select("ul#thumb >li >a >img");
			
			Elements titleScore=gameInfo.get(0).getElementsByTag("span");
			Elements sizeVersion=gameInfo.get(1).getElementsByTag("span");
			Elements languageUptime=gameInfo.get(2).getElementsByTag("span");
			Elements downloadOption=gameInfo.get(3).getElementsByTag("span");
			
			System.out.println("apk:"+apk);
			
			System.out.println("图标:"+iconPic);
			System.out.println("截图:"+bigPic);
			
			System.out.println("标题:"+gameInfo.get(0).text().split(" ")[0]);
			System.out.println("分数:"+gameInfo.get(0).getElementsByClass("real_score").text());
			
			System.out.println("大小:"+sizeVersion.get(0).text().split("： ")[1]);
			System.out.println("版本:"+sizeVersion.get(1).text().split("： ")[1]);
			
			System.out.println("语言:"+languageUptime.get(0).text().split(":")[1]);
			System.out.println("更新时间:"+languageUptime.get(1).text().split(":")[1]);
			
			System.out.println("下载量:"+downloadOption.get(0).text().split("：")[1]);
			System.out.println("操控设备:"+downloadOption.get(1).text().split(":")[1]);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
}
