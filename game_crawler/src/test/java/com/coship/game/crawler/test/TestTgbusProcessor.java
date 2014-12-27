package com.coship.game.crawler.test;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.coship.game.crawler.utils.Constants;
import com.coship.game.crawler.utils.JsoupUtil;

public class TestTgbusProcessor extends TestCase{
	
	private String fetchUrl="http://v.tgbus.com";
	
	public void parse(){
		try {
			Document doc=JsoupUtil.getDocumentWithRetry(fetchUrl);
			Elements categoryEles=doc.select("div[class^=login_big] > blockquote > p > a");
			categoryEles.remove(categoryEles.last());
			for(Element ce:categoryEles){//遍历栏目分类
				String url=ce.attr("href");
				if("/".equals(url)){//解析首页
					Document indexDoc = JsoupUtil.getDocumentWithRetry(fetchUrl);
					/**
					 * 巴士原创
					 */
					Elements bugsBody=indexDoc.select("div[class=neirong]");
					//电玩天堂,173专栏
					Elements dw173Eles=bugsBody.select("div[class=game]");
					for(int i=0;i<dw173Eles.size();i++){
						Elements dianwanEles=dw173Eles.get(i).select("> div").get(1).select("> div");
						for(Element e:dianwanEles){
							String durl=e.select("> a").attr("href");
							String img=e.select("> a > img").attr("src");
							System.out.println("电玩天堂,173专栏URL:"+durl);
							System.out.println("电玩天堂,173专栏IMG:"+img);
						}
					}
					//游戏抢先报,游戏新体位
					Elements qx_tw_eles=bugsBody.select("div[class=game1] > div");
					Elements qxEles=qx_tw_eles.get(1).select("div > a");
					Elements twEles=qx_tw_eles.get(3).select("div > a");
					
					String qxUrl=qxEles.attr("href");
					String twUrl=twEles.attr("href");
					String qxImg=qxEles.select("> img").attr("src");
					String twImg=twEles.select("> img").attr("src");
					
					System.out.println("游戏抢先报URL:"+qxUrl);
					System.out.println("游戏新体位URL:"+twUrl);
					System.out.println("游戏抢先报IMG:"+qxImg);
					System.out.println("游戏新体位IMG:"+twImg);
					
					/**
					 * 网络游戏,电视游戏,单机游戏,手游·科技,搞笑视频
					 */
					Elements indexCategoryBody=indexDoc.select("div[class=m box] > div[class=left_700 lf] > div[class^=tour] > ul > li");
					System.out.println("首页分类url条数:"+indexCategoryBody.size());
					for(Element iEles:indexCategoryBody){
						Elements aEles=iEles.select(" >p").get(0).select("> a");
						String categoryUrl=aEles.attr("href");
						String categoryImg=aEles.select("> span > img").attr("src");
						System.out.println("首页分类Url:"+categoryUrl);
						System.out.println("首页分类Img:"+categoryImg);
					}
				}else{
					//进入分类页
					Document categoryPageDoc = JsoupUtil.getDocumentWithRetry(fetchUrl+url);
					Elements categoryBody=categoryPageDoc.select("div[class^=left_700]");
					//获取总页数
					String pageText=categoryBody.select("div[class=page ma20] > ul > li >a").last().attr("href");
					String totalPage=pageText.split("/")[2];
					System.out.println("分类总页数:"+totalPage);
					Elements detailEles=categoryBody.select("div[class=video_list ma20] >dl");
					System.out.println("分类数量:"+detailEles.size());
					for(Element de:detailEles){
						String updateTimeText=de.select("dd > span").get(1).text();
						String updateTime=updateTimeText.split("：")[1];
						System.out.println("更新时间:"+updateTime);
						String detailUrl=de.select("dt > a").attr("href");
						System.out.println("详情页"+detailUrl);
						Assert.assertNotNull(detailUrl);
						parseDetail(fetchUrl+detailUrl);
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void parseDetail(String url){
		try {
			Document doc=JsoupUtil.getDocumentWithRetry(url);
			Elements detailBody=doc.select("div[class=detail_top] > dl");
			Element detailContent=detailBody.select("> dd").get(0);
			String videoNanme=detailContent.getElementsByTag("h1").text();
			String videoUrl=detailContent.select("strong > a").attr("href");
			String videoUpdateDate=detailContent.select("p > font").get(0).text();
			String detailImg=detailBody.select("> dt > a > img").attr("src");
			Assert.assertNotNull(videoNanme);
			Assert.assertNotNull(videoUrl);
			Assert.assertNotNull(detailImg);
			System.out.println("视频名称："+videoNanme);
			System.out.println("视频URL："+videoUrl);
			System.out.println("视频IMG："+detailImg);
			System.out.println("视频IMG："+videoUpdateDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void testParse(){
		System.out.println(Constants.PictureSourceType.VIDEO.getCode());
	/*	TestTgbusProcessor test=new TestTgbusProcessor();
		test.parse();*/
	}
}
