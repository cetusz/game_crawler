package com.coship.game.crawler.mapper.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.coship.game.crawler.domain.PictureEntity;
import com.coship.game.crawler.mapper.PictureEntityMapper;
import com.my.mybatis.support.mapper.AbstractBaseMapper;

@Repository
public class PictureEntityMapperImpl extends AbstractBaseMapper<PictureEntity> implements PictureEntityMapper {

	@Override
	public String getSeqName() {
		return null;
	}

	@Override
	public String getSqlNamespace() {
		return PictureEntityMapper.class.getName();
	}

	@Override
	public void deleteMutil(List<Long> ids) {
		this.sqlSessionTemplate.delete(getSqlNamespace()+".deleteMulti",ids);
	}

}
