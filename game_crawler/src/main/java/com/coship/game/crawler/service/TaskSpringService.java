/**
 * 
 * @cms-manager
 * @SpringTriggerService.java
 * @904032
 * @2014年6月28日-下午4:31:03
 */
package com.coship.game.crawler.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.CronTriggerBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerBean;
import org.springframework.stereotype.Component;

import com.coship.game.crawler.domain.TaskEntity;
import com.coship.game.crawler.domain.TaskQuery;
import com.coship.game.crawler.mapper.TaskMapper;
import com.coship.game.crawler.utils.TriggerType;
import com.coship.game.crawler.utils.YesOrNo;

/**
 * @cms-manager
 * @SpringTriggerService.java
 * @author 904032
 * @2014年6月28日-下午4:31:03
 */
@Component
public class TaskSpringService {

	private Logger logger = LoggerFactory.getLogger(TaskSpringService.class);
	
	@Autowired private SchedulerFactoryBean schedulerFactory;
	@Autowired private ApplicationContext applicationContext;
	
	@Autowired private TaskMapper taskMapper;
	
	
	public void start(){

		TaskQuery taskQuery = new TaskQuery();
		List<TaskEntity> taskList = taskMapper.selectList(taskQuery, null);
		
		if(taskList==null||taskList.size()==0){
			logger.error("no tasks,exit.");
			return;
		}
		
		//所有的Trigger
		List<Trigger> allTrigger = new ArrayList<Trigger>();
		//所有的Trigger
		List<Trigger> pauseTrigger = new ArrayList<Trigger>();
		
		//trigger参数设置
		this.handlerTriggers(allTrigger, pauseTrigger, taskList);

		Trigger[] triggers = new Trigger[allTrigger.size()];
		schedulerFactory.setTriggers(allTrigger.toArray(triggers));
		try 
		{
			schedulerFactory.afterPropertiesSet();
		} 
		catch (SchedulerException e) 
		{
			logger.error("Triggers set fail.",e);
		} 
		catch (Exception e) 
		{
			logger.error("Triggers set fail.",e);
		}
		try 
		{	
			//设置暂停的任务
			for(Trigger task : pauseTrigger)
			{
				schedulerFactory.getScheduler().pauseTrigger(task.getName(), Scheduler.DEFAULT_GROUP);
			}
		} 
		catch (SchedulerException e) 
		{
			logger.error("Triggers set fail.",e);
		} 
		
		//自动运行
		schedulerFactory.setAutoStartup(true);
		schedulerFactory.start();
	}	
	
	/**
	 * 
	 * @param task
	 * @return
	 */
	public int getTaskState(TaskEntity task){
		try 
		{
			return schedulerFactory.getScheduler().getTriggerState(task.getJobTriggerName(),
					Scheduler.DEFAULT_GROUP);
		} 
		catch (SchedulerException e) 
		{
			logger.info("trigger[name="+task.getJobTriggerName()+"] getTaskState fail.",e);
		}
		
		return -2;
	}
	
	public void deleteTask(TaskEntity task){
		Scheduler scheduler = schedulerFactory.getScheduler();
		try {
			
			scheduler.deleteJob(task.getJobDetailName(),Scheduler.DEFAULT_GROUP);
		} catch (SchedulerException e) {
			logger.error("错误原因:{}", e);
		}
	}
	
	/**
	 * trigger参数设置
	 * @param allTrigger
	 * @param pauseTrigger
	 * @param taskList
	 */
	private void handlerTriggers(List<Trigger> allTrigger,List<Trigger> pauseTrigger,List<TaskEntity> taskList){

		for(TaskEntity task : taskList)
		{//读取trigger类型，调用不同的数据类型
			Trigger trigger = this.getTriggerByTask(task);
			
			if(null!=trigger)
			{
				allTrigger.add(trigger);
				if(YesOrNo.no.getInt() == task.getUsable().intValue())
				{
					pauseTrigger.add(trigger);
				}
			}
		}
	}
	
	
	private Trigger getTriggerByTask(TaskEntity task){
		//读取trigger类型，调用不同的数据类型
		if(TriggerType.simple.getInt()==task.getTriggerType())
		{//simple类型 查看API接口：startTime设置优先，startDelay次之
			return this.getSimpleTrigger(task);
		}
		else if(TriggerType.cron.getInt()==task.getTriggerType())
		{//cron类型
			return this.getCronTrigger(task);
		}
		
		return null;
	}
	
	private CronTriggerBean getCronTrigger(TaskEntity task){
		//获取spring加载的beanFactory得到spring上下文环境，用于后续的创建
		DefaultListableBeanFactory autowireCapableBeanFactory = (DefaultListableBeanFactory)applicationContext
				.getAutowireCapableBeanFactory();
		
		//从spring获取Bean
		CronTriggerBean trigger = null;
		try 
		{
			//如果spring上下文未加载到该trigger，则创建该trigger并放入spring上下文环境
			if(null==trigger)
			{
				//从spring上下文环境中该trigger对应的jobDetail
				MethodInvokingJobDetailFactoryBean methodFactory = this.getJobDetail(task, autowireCapableBeanFactory);
				
				//创建trigger
				trigger = new CronTriggerBean();
				trigger.setBeanName(task.getJobTriggerName());
				trigger.setGroup(Scheduler.DEFAULT_GROUP);
				trigger.setName(task.getJobTriggerName());
				trigger.setJobName(task.getJobDetailName());
				trigger.setJobDetail(methodFactory.getObject());
				
				//放入spring上下文
			    if (!autowireCapableBeanFactory.containsBean(task.getJobTriggerName())) 
			    {  
			       autowireCapableBeanFactory.registerSingleton(task.getJobTriggerName(), trigger);
			    } 
			    schedulerFactory.getScheduler().addJob(methodFactory.getObject(), true);
			}
			
			trigger.setCronExpression(task.getCronExpression());
			
		} 
		catch (ClassNotFoundException e) 
		{
			logger.error("methodFactory set targetClass fail.",e);
		}
		catch (NoSuchMethodException e) 
		{
			logger.error("methodFactory afterPropertiesSet fail.",e);
		}
		catch (ParseException e) 
		{
			logger.error("triggers set CronExpression fail.",e);
		}
		catch (Exception e) 
		{
			logger.error("triggers get fail.",e);
		}

		return trigger;
	}
	
	/**
	 * 获取spring
	 * @param task
	 * @param autowireCapableBeanFactory
	 * @return
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 */
	private MethodInvokingJobDetailFactoryBean getJobDetail(TaskEntity task,DefaultListableBeanFactory autowireCapableBeanFactory) throws ClassNotFoundException, NoSuchMethodException{
		//从spring上下文环境中该trigger对应的jobDetail
		MethodInvokingJobDetailFactoryBean methodFactory =null;
		Object targetObject = null;
		if(applicationContext.containsBean(task.getJobBeanName()))
		{
			targetObject = applicationContext.getBean(task.getJobBeanName());
		}
		
		//从spring上下文环境中没有对应的targetObject,创建targetObject对象
		if(targetObject==null)
		{
		     BeanDefinitionBuilder bdb = BeanDefinitionBuilder.rootBeanDefinition(task.getJobClass());
		     autowireCapableBeanFactory.registerBeanDefinition(task.getJobBeanName(), bdb.getBeanDefinition());
		     targetObject = applicationContext.getBean(task.getJobBeanName());
		}
		
		methodFactory = new MethodInvokingJobDetailFactoryBean();
		methodFactory.setBeanFactory(autowireCapableBeanFactory);
		methodFactory.setBeanClassLoader(autowireCapableBeanFactory.getClass().getClassLoader());
		methodFactory.setBeanName(task.getJobDetailName());
		methodFactory.setGroup(Scheduler.DEFAULT_GROUP);
		//默认不让并发
		methodFactory.setConcurrent(false);
		methodFactory.setTargetMethod(task.getTargetMethod());
		methodFactory.setTargetObject(targetObject);
		methodFactory.setTargetClass(targetObject.getClass());
		methodFactory.afterPropertiesSet();
		
		//放入spring上下文
	    if (!autowireCapableBeanFactory.containsBean(task.getJobDetailName())) 
	    { 
	    	autowireCapableBeanFactory.registerSingleton(task.getJobDetailName(),methodFactory);
	    } 

		return methodFactory;
	}
	
	
	private SimpleTriggerBean getSimpleTrigger(TaskEntity task){
		DefaultListableBeanFactory autowireCapableBeanFactory = (DefaultListableBeanFactory)applicationContext
				.getAutowireCapableBeanFactory();
		
		SimpleTriggerBean trigger = null;
		try 
		{
			if(applicationContext.containsBean(task.getJobTriggerName()))
			{
				trigger = (SimpleTriggerBean)applicationContext.getBean(task.getJobTriggerName());
			}
			
			if(null==trigger)
			{
				MethodInvokingJobDetailFactoryBean methodFactory = this.getJobDetail(task, autowireCapableBeanFactory);
				
				trigger = new SimpleTriggerBean();
				trigger.setBeanName(task.getJobTriggerName());
				trigger.setGroup(Scheduler.DEFAULT_GROUP);
				trigger.setName(task.getJobTriggerName());
				trigger.setJobName(task.getJobDetailName());
				trigger.setJobDetail(methodFactory.getObject());
				
			    if (!autowireCapableBeanFactory.containsBean(task.getJobTriggerName())) 
			    {  
			       autowireCapableBeanFactory.registerSingleton(task.getJobTriggerName(), trigger);
			    } 
			}
			//simple类型 查看API接口：startTime设置优先，startDelay次之
			Date currentDate = new Date();
			if(null!=task.getJobStartTime()&&currentDate.before(task.getJobStartTime()))
			{//开始时间在当前时间之后，设置
				trigger.setStartTime(task.getJobStartTime());
			}
			else
			{
				trigger.setStartTime(currentDate);
			}
			trigger.setStartDelay(task.getStartDelay());
			trigger.setRepeatInterval(task.getRepeatInterval());
		} 
		catch (ClassNotFoundException e) 
		{
			logger.error("methodFactory set targetClass fail.",e);
		}
		catch (NoSuchMethodException e) 
		{
			logger.error("methodFactory afterPropertiesSet fail.",e);
		}
		catch (Exception e) 
		{
			logger.error("triggers get fail.",e);
		}

		return trigger;
	}
	
	
	/**
	 * 启动任务
	 * @param task
	 */
	public void start(TaskEntity task){
		
		Trigger trigger = this.getTriggerByTask(task);
		if(null==trigger)
		{
			logger.info("trigger[name="+task.getJobTriggerName()+"] not exist.");
			return;
		}

		try 
		{
			    StdScheduler scheduler =(StdScheduler)schedulerFactory.getScheduler();
			    if(scheduler.getTrigger(trigger.getName(), trigger.getGroup())==null){
			    	 schedulerFactory.getScheduler().scheduleJob(trigger);
			    }else{
					schedulerFactory.getScheduler().rescheduleJob(task.getJobTriggerName(), 
		        		Scheduler.DEFAULT_GROUP, trigger);
			    }
		} 
		catch (SchedulerException e) 
		{
			logger.error("trigger[name="+task.getJobTriggerName()+"] start fail.",e);
		}
	}
	
	/**
	 * 停止任务
	 * @param task
	 */
	public void pause(TaskEntity task){
		
		Trigger trigger = this.getTriggerByTask(task);
		if(null==trigger)
		{
			logger.info("trigger[name="+task.getJobTriggerName()+"] not exist.");
			return;
		}

		try 
		{
	        schedulerFactory.getScheduler().pauseTrigger(task.getJobTriggerName(), 
	        		Scheduler.DEFAULT_GROUP);
		} 
		catch (SchedulerException e) 
		{
			logger.error("trigger[name="+task.getJobTriggerName()+"] start fail.",e);
		}
	}
	
	/**
	 * 恢复任务，慎用
	 * @param task
	 */
	public void resume(TaskEntity task){
		
		Trigger trigger = this.getTriggerByTask(task);
		if(null==trigger)
		{
			logger.info("trigger[name="+task.getJobTriggerName()+"] not exist.");
			return;
		}

		try 
		{
	        schedulerFactory.getScheduler().resumeTrigger(task.getJobTriggerName(), 
	        		Scheduler.DEFAULT_GROUP);
		} 
		catch (SchedulerException e) 
		{
			logger.error("trigger[name="+task.getJobTriggerName()+"] start fail.",e);
		}
	}
	
	
}
