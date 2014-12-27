package com.coship.game.crawler.apk.processor;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.coship.game.crawler.domain.PictureEntity;
import com.coship.game.crawler.utils.Constants.APKSource;
import com.coship.game.crawler.utils.Md5Util;

public class DangBeiProcessor extends AbstractAPKProcessor{
	
	public DangBeiProcessor(String workerName) {
		super(workerName);
	}


	/**
	 * 主体
	 */
	Element bodyContent;
	
	/**
	 * 游戏内容
	 */
	private Element gameContent;
	
	/**
	 * 游戏详细
	 */
	private Elements gameDetail;
	
	private Elements titleScore;
	
	private Elements sizeVersion;
	
	private Elements languageUptime;
	
	private Elements downloadOption;
	
	
	@Override
	protected void preProcess() {
		bodyContent=doc.getElementById("mainContent");
		gameContent=doc.getElementById("softAbs");
		gameDetail=gameContent.getElementsByTag("p");
		titleScore=gameDetail.get(0).getElementsByTag("span");
		sizeVersion=gameDetail.get(1).getElementsByTag("span");
		languageUptime=gameDetail.get(2).getElementsByTag("span");
		downloadOption=gameDetail.get(3).getElementsByTag("span");
	}


	@Override
	protected String extractName() {
		return gameDetail.get(0).text().split(" ")[0];
	}


	@Override
	protected String extractOperationWay() {
		return downloadOption.get(1).text().split(":")[1];
	}


	@Override
	protected String extractType() {
		return null;
	}


	@Override
	protected String extractIntroduce() {
		return bodyContent.select("p[class=message]").text();
	}


	@Override
	protected String extractSize() {
		return sizeVersion.get(0).text().split("： ")[1];
	}


	@Override
	protected String extractUpdateDate() {
		return languageUptime.get(1).text().split(":")[1];
	}


	@Override
	protected String extractLanguage() {
		return languageUptime.get(0).text().split(":")[1];
	}


	@Override
	protected String extractVersion() {
		return sizeVersion.get(1).text().split("： ")[1];
	}


	@Override
	protected String extractAndroidSystemVersion() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected String extractDownloadTimes() {
		return downloadOption.get(0).text().split("：")[1];
	}


	@Override
	protected String extractSourceId() {
		return Md5Util.Md5(detailUrl);
	}


	@Override
	protected void postProcess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String extractApkUrl() {
		return gameContent.getElementsByClass("fast_download").get(0).attr("href");
	}

	@Override
	protected List<PictureEntity> extractPictures() {
		List<PictureEntity> picList=new ArrayList<PictureEntity>();
		PictureEntity smallPic=new PictureEntity();
		smallPic.setSourceUrl(bodyContent.select("div[class=soft_img] >img").attr("src"));
		Elements els=bodyContent.select("ul#thumb >li >a >img");
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
	protected String extractSourceName() {
		return APKSource.DANGBEI.getName();
	}



	
	
}
