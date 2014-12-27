package com.coship.game.crawler.mapper.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.coship.game.crawler.domain.VideoEntity;
import com.coship.game.crawler.mapper.VideoMapper;
import com.my.mybatis.support.mapper.AbstractBaseMapper;

@Repository
public class VideoMapperImpl extends AbstractBaseMapper<VideoEntity> implements VideoMapper{

	@Override
	public String getSeqName() {
		return null;
	}

	@Override
	public String getSqlNamespace() {
		return VideoMapper.class.getName();
	}

	@Override
	public void deleteMulti(List ids) {
		this.sqlSessionTemplate.delete(getSqlNamespace()+".deleteMulti", ids);
	}

	@Override
	public Integer countBySourceName(String sourceName) {
		return this.sqlSessionTemplate.selectOne(getSqlNamespace()+".countBySourceName", sourceName);
	}

}
