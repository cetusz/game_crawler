/**
 * 
 */
package com.coship.game.crawler.mapper.impl;

import org.springframework.stereotype.Repository;

import com.coship.game.crawler.domain.TaskEntity;
import com.coship.game.crawler.mapper.TaskMapper;
import com.my.mybatis.support.mapper.AbstractBaseMapper;

/**
 * 
 * @cms-core
 * @TaskMapperImpl.java
 * @author 904032
 * @2014年6月27日-上午10:49:34
 */
@Repository
public class TaskMapperImpl extends AbstractBaseMapper<TaskEntity> implements TaskMapper {
	


	@Override
	public String getSqlNamespace() {
		return TaskMapper.class.getName();
	}


	@Override
	public String getSeqName() {
		return null;
	}

}
