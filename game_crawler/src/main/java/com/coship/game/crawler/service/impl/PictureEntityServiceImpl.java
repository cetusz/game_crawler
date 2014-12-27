package com.coship.game.crawler.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coship.game.crawler.domain.PictureEntity;
import com.coship.game.crawler.mapper.PictureEntityMapper;
import com.coship.game.crawler.service.IPictureEntityService;
import com.my.common.domain.Query;
import com.my.mybatis.support.Page;
import com.my.mybatis.support.Sort;

@Service
public class PictureEntityServiceImpl implements IPictureEntityService {

	@Autowired PictureEntityMapper pictureEntityMapper;
	@Override
	public Long saveOrUpdate(PictureEntity entity) {
		return (Long)pictureEntityMapper.saveOrUpdate(entity);
	}

	@Override
	public PictureEntity findOneById(Long id) {
		return pictureEntityMapper.findOne(id);
	}

	@Override
	public void deleteOne(Long id) {
		pictureEntityMapper.deleteOne(id);
	}

	@Override
	public void deleteMulti(List<Long> ids) {
		pictureEntityMapper.deleteMutil(ids);
	}


	@Override
	public List<PictureEntity> selectList(Query query, Sort sorts) {
		return pictureEntityMapper.selectList(query, sorts);
	}

	@Override
	public Page<PictureEntity> selectPageList(Query query, Sort sorts,
			int rows, int page) {
		Page<PictureEntity> pager = new Page<PictureEntity>(rows,page);
		return pictureEntityMapper.getPage(pager, query, sorts);
	}

}
