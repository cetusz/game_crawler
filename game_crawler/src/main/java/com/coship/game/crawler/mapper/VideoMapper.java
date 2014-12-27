package com.coship.game.crawler.mapper;

import java.util.List;

import com.coship.game.crawler.domain.VideoEntity;
import com.my.mybatis.support.mapper.BaseMapperInterface;

public interface VideoMapper extends BaseMapperInterface<VideoEntity>{
	
	public void deleteMulti(List<Long> ids);
	
	/**
	 * 根据源名称查询计数
	 * @param query
	 */
	public Integer countBySourceName(String sourceName);

}
