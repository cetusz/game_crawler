package com.coship.game.crawler.mapper;

import java.util.List;

import com.coship.game.crawler.domain.PictureEntity;
import com.my.mybatis.support.mapper.BaseMapperInterface;

public interface PictureEntityMapper extends BaseMapperInterface<PictureEntity>{
	public void deleteMutil(List<Long> ids);
}
