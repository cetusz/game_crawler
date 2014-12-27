/**
 * 
 * @cms-manager
 * @SpringQuartJobController.java
 * @907708
 * @2014年6月27日-上午9:01:32
 */
package com.coship.game.crawler.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
 * @SpringQuartJobController.java
 * @author 904032
 * @2014年6月27日-上午9:01:32
 */
@Controller
@RequestMapping("/manager/task")
public class TaskController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired private TaskMapper taskMapper;
	@Autowired private ITaskService taskService;
	@Autowired private SchedulerFactoryBean schedulerFactory;	
	@Autowired private TaskSpringService taskTriggerService;
	

	@RequestMapping(value = "/listpage")
	public ModelAndView pageList() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/task/list");
		return modelAndView;
	}

	@RequestMapping(value = "/list", produces = { "application/json;charset=UTF-8" })
	public ResponseEntity<Map<String, Object>> list(TaskQuery taskQuery,
			int page, int rows) {
		Page<TaskVO> pager = new Page<TaskVO>(rows, page);// 设置
		Sort sorts = new Sort();
		sorts.setTableSort("lastUpdateTime", true);
		taskService.pageList(pager, taskQuery, sorts);
		return this.createPageResponseEntity(pager);
	}
	

	@RequestMapping(value = "/id/{id}/page")
	public ModelAndView goEditPage(@PathVariable Long id,String pageType) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/task/edit");
		modelAndView.addObject("id",id);
		modelAndView.addObject("pageType",pageType);
		return modelAndView;
	}

	@RequestMapping(value = "/id/{id}",  produces = { "application/json;charset=UTF-8"})
	public ResponseEntity<TaskEntity> get(@PathVariable Long id) {
		TaskEntity taskEntity = taskMapper.findOne(id);
		return this.createResponseEntity(taskEntity);
	}
	
	@RequestMapping(value="del", produces = { "application/json;charset=UTF-8"})
	public @ResponseBody Map<String,Object> delete(Long id){
		Map<String,Object> result = new HashMap<String,Object>();
		TaskEntity taskEntity = taskService.findOne(id);
		taskService.delete(id);
		taskTriggerService.deleteTask(taskEntity);
		result.put("Message", "删除成功!");
		return result;
	}
	/**
	 * 更新存储
	 * 
	 * @param objectVO
	 * @return
	 */
	@RequestMapping(value = "/save", produces = { "application/json;charset=UTF-8" })
	public ResponseEntity<String> save(TaskVO objectVO) {
		// System.out.println("save "+assetEditModel.getId());
		try {
			if (objectVO.getId() == null || objectVO.getId() < 1) {
				objectVO.setId(null);
				objectVO.setCreateTime(new Date());
			}
			objectVO.setLastUpdateTime(new Date());
			this.taskService.saveOrUpdate(objectVO);
			return this
					.createResponseEntity("{\"success\": true,\"message\": \"task save successfully.\"}");
		} catch (Exception e) {
			logger.error("{}",e);
		}
		return this
				.createResponseEntity("{\"success\": false,\"message\": \"task save failed.\"}");
	}
	
	
	
	/**
	 * 停止任务
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/pause", produces = { "application/json;charset=UTF-8" })
	public ResponseEntity<String>  pause(Integer[] ids){
		try 
		{
			for(Integer id : ids)
			{
				TaskEntity task = taskMapper.findOne(id);
				
				if(null==task)
				{
					logger.error("can't find task[id="+id+"]");
				}
				else
				{
					taskTriggerService.pause(task);

				}
			}

			logger.info("task[ids="+ids+"] pause success.");
			return this
					.createResponseEntity("{\"success\": true,\"message\": \"task pause successfully.\"}");
		} catch (Exception e) {
			logger.error("{}",e);
		} 
		
		return this
				.createResponseEntity("{\"success\": fals,\"message\": \"task pause fail.\"}");
	}
	
	/**
	 * 执行任务
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/start", produces = { "application/json;charset=UTF-8" })
	public ResponseEntity<String>  start(Integer[] ids){

		try 
		{
			for(Integer id : ids)
			{
				TaskEntity task = taskMapper.findOne(id);
				
				if(null==task)
				{
					logger.error("can't find task[id="+id+"]");
				}
				else
				{
					taskTriggerService.start(task);
					logger.info("task[id="+id+"] start success.");	
				}
			}
			return this
					.createResponseEntity("{\"success\": true,\"message\": \"task start successfully.\"}");
		} 
		catch (Exception e) 
		{
			logger.error("{}",e);
		} 
		
		return this
				.createResponseEntity("{\"success\": fals,\"message\": \"task start fail.\"}");
	}

}
