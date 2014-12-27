package com.coship.game.crawler.service;
import com.coship.game.crawler.domain.VideoEntity;

public interface IVideoService extends BaseService<VideoEntity>{
	

	public int countBySourceName(String sourceName);

}
