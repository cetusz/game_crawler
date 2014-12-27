package com.coship.game.crawler.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.coship.game.crawler.domain.PictureEntity;
import com.coship.game.crawler.service.IPictureEntityService;
import com.my.mybatis.support.Page;

@Controller
@RequestMapping("picture")
public class PictureController extends BaseController{
	
	@Autowired IPictureEntityService pictureService;

	@RequestMapping("list")
	public ResponseEntity<Map<String,Object>> list(PictureEntity query,int page,int rows){
		Page<PictureEntity> pager = pictureService.selectPageList(query, null, rows, page);
		return createPageResponseEntity(pager);
	}
	
	@RequestMapping("save")
	public Map<String,Object> save(PictureEntity entity){
		pictureService.saveOrUpdate(entity);
		return result(true,"保存成功!");
	}
	
	@RequestMapping("del")
	public  Map<String,Object> del(String ids){
		try {
			pictureService.deleteMulti(this.convertIdsStr2Long(ids, ","));
		} catch (Exception e) {
			e.printStackTrace();
			return result(false,"删除失败!");
		}
		return result(true,"删除成功!");
	}
	
	

}
