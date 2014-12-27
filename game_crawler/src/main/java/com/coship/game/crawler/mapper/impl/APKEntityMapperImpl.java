package com.coship.game.crawler.mapper.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.coship.game.crawler.domain.APKEntity;
import com.coship.game.crawler.mapper.APKEntityMapper;
import com.my.mybatis.support.mapper.AbstractBaseMapper;

@Repository
public class APKEntityMapperImpl extends AbstractBaseMapper<APKEntity> implements APKEntityMapper {
	
	@Override
	public String getSeqName() {
		return null;
	}

	@Override
	public String getSqlNamespace() {
		return APKEntityMapper.class.getName();
	}

	@Override
	public Integer countBySourceName(String sourceName) {
		return this.sqlSessionTemplate.selectOne("countBySourceName", sourceName);
	}

	@Override
	public void deleteMutil(List<Long> ids) {
		this.sqlSessionTemplate.delete("deleteMulti",ids);
	}

}
