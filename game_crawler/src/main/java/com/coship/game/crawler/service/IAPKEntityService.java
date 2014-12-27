package com.coship.game.crawler.service;

import com.coship.game.crawler.domain.APKEntity;

public interface IAPKEntityService extends BaseService<APKEntity> {
	

	/**
	 * 根据源名称查询计数
	 * @param query
	 */
	public int countBySourceName(String sourceName);
	

}
