package com.coship.game.crawler.video.processor;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.coship.game.crawler.domain.PictureEntity;
import com.coship.game.crawler.utils.ConfigFactory;
import com.coship.game.crawler.utils.Constants;


/**
 * 
 * @author 909191
 *
 */
public class TgbusProcessor extends AbstractVideoProcessor{
	
	public TgbusProcessor(String workerName) {
		super(workerName);
	}
	
	private Elements detailBody;
	
	private Element detailContent;
	
	private String fetchUrl;
	
	
	@Override
	public void pre(){
		fetchUrl = ConfigFactory.getString(Constants.TGBUS_FETCHURL);
		detailBody=doc.select("div[class=detail_top] > dl");
		detailContent=detailBody.select("> dd").get(0);
	}
	
	@Override
	public String getVideoName() {
		return detailContent.getElementsByTag("h1").text();
	}

	@Override
	public String getVideoUrl() {
		return fetchUrl+detailContent.select("strong > a").attr("href");
	}
	
	@Override
	public String getSourceName() {
		return Constants.VideoSource.TGBUS.getName();
	}

	@Override
	protected PictureEntity getPicture() {
		PictureEntity picture=new PictureEntity();
		picture.setPictureType(Constants.PictureType.SNAPSHOT.name());
		String detailImg=detailBody.select("> dt > a > img").attr("src");
		picture.setSourceUrl(detailImg);
		return picture;
	}

	@Override
	protected String getUpdateDate() {
		return detailContent.select("p > font").get(0).text();
	}

	@Override
	protected void post() {
		
	}

	@Override
	protected String getRealFileUrl() {
		// TODO Auto-generated method stub
		return null;
	}

}
