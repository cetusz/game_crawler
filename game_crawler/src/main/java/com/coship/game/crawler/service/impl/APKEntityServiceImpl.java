package com.coship.game.crawler.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coship.game.crawler.domain.APKEntity;
import com.coship.game.crawler.mapper.APKEntityMapper;
import com.coship.game.crawler.service.IAPKEntityService;
import com.my.common.domain.Query;
import com.my.mybatis.support.Page;
import com.my.mybatis.support.Sort;

@Service
public class APKEntityServiceImpl implements IAPKEntityService{


	@Autowired APKEntityMapper apkEntityMapper;
	@Override
	public Long saveOrUpdate(APKEntity entity) {
		return (Long)apkEntityMapper.saveOrUpdate(entity);		
	}

	@Override
	public APKEntity findOneById(Long id) {
		return apkEntityMapper.findOne(id);
	}

	@Override
	public void deleteOne(Long id) {
		apkEntityMapper.deleteOne(id);
	}

	@Override
	public void deleteMulti(List<Long> ids) {
	}

	@Override
	public Page<APKEntity> selectPageList(Query query,
			Sort sorts,int page,int pageSize) {
		Page<APKEntity> pager = new Page<APKEntity>(pageSize, page);
		return apkEntityMapper.getPage(pager, query, sorts);
	}

	@Override
	public List<APKEntity> selectList(Query query, Sort sorts) {
		return apkEntityMapper.selectList(query, sorts);
	}

	@Override
	public int countBySourceName(String sourceName) {
		return apkEntityMapper.countBySourceName(sourceName);
	}

}
