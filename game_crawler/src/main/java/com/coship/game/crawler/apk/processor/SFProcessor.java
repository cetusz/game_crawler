package com.coship.game.crawler.apk.processor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.coship.game.crawler.domain.PictureEntity;
import com.coship.game.crawler.utils.Constants.APKSource;
import com.coship.game.crawler.utils.Constants.PictureType;
import com.coship.game.crawler.utils.Constants.Status;
import com.coship.game.crawler.utils.HttpUtils;
import com.coship.game.crawler.utils.JsoupUtil;
import com.coship.game.crawler.utils.Md5Util;

/**
 * 沙发网站抓取类
 * @author 907708
 *
 */
public class SFProcessor extends AbstractAPKProcessor {

	public SFProcessor(String workerName) {
		super(workerName);
	}

	private final String downloadUrlFormater = "http://app.shafa.com/api/push/download/%s?response-content-type=application/fvnd.android.package-archive";

	
	
	@Override
	protected String extractName() {
		String name = JsoupUtil.getTextAndSpace("div[class=app-title]", doc, 0);
		name = JsoupUtil.trim(name);
		return name;
	}

	@Override
	protected String extractOperationWay() {
		return JsoupUtil.getAttributeValue("div[class=app-rating]>span>a", "title", doc, 0);
	}

	@Override
	protected String extractType() {
		return JsoupUtil.getElementByCss("section[class=app-info]>div>div>div>div[class*=meta-info]", doc, 0).select("a").text();
	}

	@Override
	protected String extractIntroduce() {
		return JsoupUtil.getText("div[itemprop=description]>div>p", doc, 0);
	}

	@Override
	protected String extractSize() {
		return JsoupUtil.getElementByCss("section[class=app-info]>div>div>div>div[class*=meta-info]", doc, 1).select("span").text();
	}

	@Override
	protected String extractUpdateDate() {
		return JsoupUtil.getElementByCss("section[class=app-info]>div>div>div>div[class*=meta-info]", doc, 5).select("span").text();
	}

	@Override
	protected String extractLanguage() {
		return JsoupUtil.getElementByCss("section[class=app-info]>div>div>div>div[class*=meta-info]", doc, 3).select("span").text();
	}

	@Override
	protected String extractVersion() {
		Element el = JsoupUtil.getElementByCss("section[class=app-info]>div>div>div>div[class*=meta-info]", doc, 4);
	    if(el!=null){
	    	return el.select("span").text();
	    }
	    return "";
	}

	@Override
	protected String extractAndroidSystemVersion() {
		try{
			return JsoupUtil.getElementByCss("section[class=app-info]>div>div>div>div[class*=meta-info]", doc, 6).select("span").text();
		}catch(Exception ex){	
			return null;
		}
	}

	@Override
	protected String extractDownloadTimes() {
		return JsoupUtil.getElementByCss("section[class=app-info]>div>div>div>div[class*=meta-info]", doc, 2).select("span").text();
	}

	@Override
	protected String extractSourceId() {
		String sourceId = JsoupUtil.getAttributeValue("div[class=app-download]>button","data-app-id" ,doc, 1);
		if(StringUtils.isBlank(sourceId)){
			sourceId = Md5Util.Md5(detailUrl);
		}
		return sourceId;
	}


	@Override
	protected void preProcess() {
		System.out.println("Thread "+Thread.currentThread().getName()+" executing");
	}

	@Override
	protected void postProcess() {
	}
	

	@Override
	protected List<PictureEntity> extractPictures() {
		  List<PictureEntity> pictures = new ArrayList<PictureEntity>();
		  //icon
		  String iconUrl = JsoupUtil.getAttributeValue("section[class=page-header app-header]>div>div>img", "src", doc, 0);
		  PictureEntity iconPicture = new PictureEntity();
		  iconPicture.setSourceUrl(iconUrl);
		  iconPicture.setPictureType(PictureType.ICON.getValue());
		  iconPicture.setStatus(Status.INIT.getValue());
		  pictures.add(iconPicture);
		  //截图
		  Elements els = doc.select("section[class=row app-screenshots]>div>div[class=carousel-inner]>div[class*=item]");
		    for(Element el:els){
		    	Elements aels = el.select("a");
		    	for(Element ael:aels){
		    		PictureEntity picture = new PictureEntity();
		    		picture.setSourceUrl(ael.attr("href"));
		    		picture.setPictureType(PictureType.SNAPSHOT.getValue());
		    		picture.setStatus(Status.INIT.getValue());
		    		pictures.add(picture);
		    	}
		    }
		  return pictures;
	}

	@Override
	protected String extractApkUrl() {
		String sourceId = JsoupUtil.getAttributeValue("div[class=app-download]>button","data-app-id" ,doc, 1);
	    String sourceUrl = String.format(downloadUrlFormater, sourceId);
		return HttpUtils.getInstance().getRedirectUrlWithRetry(sourceUrl);
	}

	@Override
	protected String extractSourceName() {
		return APKSource.SHAFA.getName();
	}
	
}
