/**
 * 
 * @cms-manager
 * @TaskServiceImpl.java
 * @907708
 * @2014年6月30日-下午2:22:45
 */
package com.coship.game.crawler.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.coship.game.crawler.domain.TaskEntity;
import com.coship.game.crawler.domain.TaskQuery;
import com.coship.game.crawler.domain.TaskVO;
import com.coship.game.crawler.mapper.TaskMapper;
import com.coship.game.crawler.service.ITaskService;
import com.coship.game.crawler.service.TaskSpringService;
import com.my.mybatis.support.Page;
import com.my.mybatis.support.Sort;

/**
 * @cms-manager
 * @TaskServiceImpl.java
 * @author 904032
 * @2014年6月30日-下午2:22:45
 */
@Component
public class TaskServiceImpl implements ITaskService {

	@Autowired private TaskMapper taskMapper;
	@Autowired private TaskSpringService taskTriggerService;
	
	@Override
	@Transactional
	public void saveOrUpdate(TaskEntity taskEntity) {
		taskMapper.saveOrUpdate(taskEntity);
	}

	@Override
	public TaskEntity findOne(Long id) {

		return this.taskMapper.findOne(id);
	}

	@Override
	public void pageList(Page<TaskVO> pager, TaskQuery taskQuery, Sort sorts) {
		Page<TaskEntity> pageTmp = new Page<TaskEntity>();
		taskMapper.getPage(pageTmp, taskQuery, sorts);
		BeanUtils.copyProperties(pageTmp,pager,new String[]{"result"});//忽略列表
		List<TaskVO> list = new ArrayList<TaskVO>();
		List<TaskEntity>  dbList = pageTmp.getResult();
		if(null!=dbList)
		{
			for(TaskEntity entity : dbList)
			{
				TaskVO tmp = new TaskVO();
				BeanUtils.copyProperties(entity,tmp);
				
				tmp.setState(taskTriggerService.getTaskState(entity));
				list.add(tmp);
			}
		}
		BeanUtils.copyProperties(pageTmp,pager,new String[]{"result"});//忽略列表
		pager.setResult(list);
	}

	@Override
	public void delete(Long id) {
		taskMapper.deleteOne(id);
	}
	
}
