/**
 * 
 * @cms-service
 * @ITaskService.java
 * @907708
 * @2014年6月30日-下午2:16:40
 */
package com.coship.game.crawler.service;

import com.coship.game.crawler.domain.TaskEntity;
import com.coship.game.crawler.domain.TaskQuery;
import com.coship.game.crawler.domain.TaskVO;
import com.my.mybatis.support.Page;
import com.my.mybatis.support.Sort;

/**
 * @cms-service
 * @ITaskService.java
 * @author 904032
 * @2014年6月30日-下午2:16:40
 */
public interface ITaskService {
	
	/**
	 * 任务新增或更新
	 * @param taskEntity
	 */
	public void saveOrUpdate(TaskEntity taskEntity);
		
	/**
	 * 任务查询
	 * @param id
	 */
	public TaskEntity findOne(Long id);
	
	public void delete(Long id);
	
	//public List<TaskEntity> selectList(TaskQuery taskQuery);
	
	/**
	 * 分页获取任务列表信息
	 * @param pager
	 * @param taskQuery
	 * @param sorts
	 */
	public void pageList(Page<TaskVO> pager,TaskQuery taskQuery, Sort sorts);
	
	

}
