package com.coship.game.crawler.test;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TestQiPoProcessor {
	
	
	public void execute(){
		try {
			String url="http://down.7po.com/games_new_2.html";
			Document doc=Jsoup.connect(url).get();
			Elements el=doc.select("div[class^=big]");
			String date=el.get(0).select("span[class=apk_info]").text().split(" ")[0];
			
			String page=doc.select("div[class^=big]").select("a[class=page_to]").text().split(" ")[1];
			System.out.println("page:"+page);
			for(Element e:el){
				Elements tags=e.getElementsByTag("a");
				String detailUrl=tags.get(0).attr("href");
				parseHtml(detailUrl);
				break;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void parseHtml(String url){
		try {
			Document doc=Jsoup.connect(url).get();
			Element bodyContent=doc.getElementsByClass("bk").get(0);
			Element gameBody=bodyContent.getElementsByClass("apk_infos").get(0);
			//图标
			String iconPic=gameBody.select("img[src]").get(0).attr("src");
			
			System.out.println("详情："+bodyContent.select("pre[class=apk_detail]").text());
			//游戏信息
			Elements gameInfo=gameBody.getElementsByTag("span");
			
			String title=gameBody.select("p[class=apk_title]").get(0).text();
			String version=gameInfo.get(1).text().split("：")[1];
			String size=gameInfo.get(2).text().split("：")[1];
			String dev=gameInfo.get(3).text().split("：")[1];
			String sys=gameInfo.get(4).text().split("：")[1];
			String download=gameInfo.get(5).text().split("：")[1];
			String option=gameInfo.get(6).text().split("：")[1];
			
			//apk
			String apk=bodyContent.select("a[class=apk_down]").attr("href");
			
			//截图
			Elements bigPicEls=bodyContent.select("ul[class=focus-list] >li >img");
			
			
			System.out.println("apk:"+apk);
			
			System.out.println("图标:"+iconPic);
			System.out.println("截图:"+bigPicEls.get(0).attr("src"));
			
			System.out.println("标题:"+title);
			
			System.out.println("大小:"+size);
			System.out.println("版本:"+version);
			
			System.out.println("开发者:"+dev);
			System.out.println("系统要求:"+sys);
			
			System.out.println("下载量:"+download);
			System.out.println("操控设备:"+option);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
