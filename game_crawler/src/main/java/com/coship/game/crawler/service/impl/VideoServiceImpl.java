package com.coship.game.crawler.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coship.game.crawler.domain.VideoEntity;
import com.coship.game.crawler.mapper.VideoMapper;
import com.coship.game.crawler.service.IVideoService;
import com.my.common.domain.Query;
import com.my.mybatis.support.Page;
import com.my.mybatis.support.Sort;
/**
 * 
 * @author 909191
 *
 */
@Service
public class VideoServiceImpl implements IVideoService{
	
	@Autowired
	private VideoMapper videoMapper;

	@Override
	public Long saveOrUpdate(VideoEntity entity) {
		return (Long)videoMapper.saveOrUpdate(entity);		
	}

	@Override
	public VideoEntity findOneById(Long id) {
		return videoMapper.findOne(id);
	}

	@Override
	public void deleteOne(Long id) {
		videoMapper.deleteOne(id);
		
	}

	@Override
	public void deleteMulti(List ids) {
		videoMapper.deleteMulti(ids);
	}

	@Override
	public Page selectPageList(Query query, Sort sorts, int page, int rows) {
		Page<VideoEntity> pager=new Page<VideoEntity>(rows,page);
		return videoMapper.getPage(pager,query,sorts);
	}

	@Override
	public List selectList(Query query, Sort sorts) {
		return videoMapper.selectList(query, sorts);
	}
	

	@Override
	public int countBySourceName(String sourceName) {
		return videoMapper.countBySourceName(sourceName);
	}

}
