package com.coship.game.crawler.apk.processor;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.coship.game.crawler.domain.PictureEntity;
import com.coship.game.crawler.utils.Constants.APKSource;
import com.coship.game.crawler.utils.Md5Util;

public class QiPoProcessor extends AbstractAPKProcessor{
	
	public QiPoProcessor(String workerName){
		super(workerName);
	}
	
	/**
	 * 主体内容
	 */
	private Element bodyContent;
	/**
	 * 游戏主体
	 */
	private Element gameBody;
	/**
	 * 游戏详细
	 */
	private Elements gameDetail;
	
	
	@Override
	protected void preProcess() {
		bodyContent=doc.getElementsByClass("bk").get(0);
		gameBody=bodyContent.getElementsByClass("apk_infos").get(0);
		gameDetail=gameBody.getElementsByTag("span");
	}


	@Override
	protected String extractName() {
		return gameBody.select("p[class=apk_title]").get(0).text();
	}


	@Override
	protected String extractOperationWay() {
		return gameDetail.get(6).text().split("：")[1];
	}


	@Override
	protected String extractType() {
		return gameDetail.get(6).text().split("：")[1];
	}


	@Override
	protected String extractIntroduce() {
		return bodyContent.select("pre[class=apk_detail]").text();
	}


	@Override
	protected String extractSize() {
		return gameDetail.get(2).text().split("：")[1];
	}


	@Override
	protected String extractUpdateDate() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected String extractLanguage() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected String extractVersion() {
		return gameDetail.get(1).text().split("：")[1];
	}


	@Override
	protected String extractAndroidSystemVersion() {
		return gameDetail.get(4).text().split("：")[1];
	}


	@Override
	protected String extractDownloadTimes() {
		return gameDetail.get(5).text().split("：")[1];
	}


	@Override
	protected String extractSourceId() {
		return Md5Util.Md5(detailUrl);
	}

	@Override
	protected String extractApkUrl() {
		return bodyContent.select("a[class=apk_down]").attr("href");
	}

	@Override
	protected List<PictureEntity> extractPictures() {
		List<PictureEntity> picList=new ArrayList<PictureEntity>();
		PictureEntity smallPic=new PictureEntity();
		smallPic.setSourceUrl(gameBody.select("img[src]").get(0).attr("src"));
		Elements els=bodyContent.select("ul[class=focus-list] >li >img");
		
		PictureEntity bigPic=null;
		for(int i=0;i<els.size();i++){
			bigPic=new PictureEntity();
			String src=els.get(i).attr("src");
			bigPic.setSourceUrl(src);
			picList.add(bigPic);
		}
		return picList;
	}
	
	
	@Override
	protected void postProcess() {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected String extractSourceName() {
		return APKSource.QIPO.getName();
	}


}
