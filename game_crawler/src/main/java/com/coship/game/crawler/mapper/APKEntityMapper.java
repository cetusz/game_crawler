package com.coship.game.crawler.mapper;

import java.util.List;

import com.coship.game.crawler.domain.APKEntity;
import com.my.mybatis.support.mapper.BaseMapperInterface;

public interface APKEntityMapper  extends BaseMapperInterface<APKEntity>{
	

	/**
	 * 根据源名称查询计数
	 * @param query
	 */
	public Integer countBySourceName(String sourceName);
	
	public void deleteMutil(List<Long> ids);
	

}
