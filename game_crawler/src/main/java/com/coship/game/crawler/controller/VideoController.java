package com.coship.game.crawler.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.coship.game.crawler.domain.VideoEntity;
import com.coship.game.crawler.service.IVideoService;
import com.coship.game.crawler.utils.ConfigFactory;
import com.coship.game.crawler.utils.Constants;
import com.my.mybatis.support.Page;

@Controller
@RequestMapping("video")
public class VideoController extends BaseController{

	@Autowired IVideoService videoService;
	
	@RequestMapping("pageIndex")
	public ModelAndView pageIndex(){
		ModelAndView mv = new ModelAndView();
		mv.setViewName("video/list");
		mv.addObject("serverPath", ConfigFactory.getString(Constants.FTPHOST));
		return mv;
	}
	@RequestMapping("list")
	public ResponseEntity<Map<String,Object>> list(VideoEntity entity,int page,int rows){
		Page<VideoEntity> pager = videoService.selectPageList(entity,null,page,rows);
		return createPageResponseEntity(pager);
	}
	
	@RequestMapping("save")
	public @ResponseBody Map<String,Object> save(VideoEntity entity){
		videoService.saveOrUpdate(entity);
		return result(true,"保存成功!");
	}
	
	@RequestMapping("del")
	public  Map<String,Object> del(String ids){
		videoService.deleteMulti(this.convertIdsStr2Long(ids, ","));
		return result(true,"保存成功!");
	}

}
