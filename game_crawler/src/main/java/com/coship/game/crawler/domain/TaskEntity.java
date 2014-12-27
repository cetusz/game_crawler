/**
 * 
 * @cms-domain
 * @TaskEntity.java
 * @907708
 * @2014年6月27日-上午10:14:55
 */
package com.coship.game.crawler.domain;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.my.common.domain.BaseEntity;

/**
 * @cms-domain
 * @TaskEntity.java
 * @author 904032
 * @2014年6月27日-上午10:14:55
 */
public class TaskEntity implements BaseEntity<Integer>{
	
	private Integer id;
	private Date createTime;
	private Date lastUpdateTime;
	
	private String title;
	private String jobClass;
	private String jobBeanName;
	private String jobDetailName; 
	private String jobTriggerName;
	private Integer triggerType;//simple|cron
	private String targetMethod;	
	private String cronExpression;
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") 
	private Date jobStartTime;
	private Integer startDelay;
	private Integer repeatInterval;
	private Integer usable;
	private Integer runnable;
	private String description;

	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getJobClass() {
		return jobClass;
	}
	public void setJobClass(String jobClass) {
		this.jobClass = jobClass;
	}
	public String getJobBeanName() {
		return jobBeanName;
	}
	public void setJobBeanName(String jobBeanName) {
		this.jobBeanName = jobBeanName;
	}
	public String getJobDetailName() {
		return jobDetailName;
	}
	public void setJobDetailName(String jobDetailName) {
		this.jobDetailName = jobDetailName;
	}
	public String getJobTriggerName() {
		return jobTriggerName;
	}
	public void setJobTriggerName(String jobTriggerName) {
		this.jobTriggerName = jobTriggerName;
	}
	public Integer getTriggerType() {
		return triggerType;
	}
	public void setTriggerType(Integer triggerType) {
		this.triggerType = triggerType;
	}
	public String getTargetMethod() {
		return targetMethod;
	}
	public void setTargetMethod(String targetMethod) {
		this.targetMethod = targetMethod;
	}
	public String getCronExpression() {
		return cronExpression;
	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	public Date getJobStartTime() {
		return jobStartTime;
	}
	public void setJobStartTime(Date jobStartTime) {
		this.jobStartTime = jobStartTime;
	}
	public Integer getStartDelay() {
		return startDelay;
	}
	public void setStartDelay(Integer startDelay) {
		this.startDelay = startDelay;
	}
	public Integer getRepeatInterval() {
		return repeatInterval;
	}
	public void setRepeatInterval(Integer repeatInterval) {
		this.repeatInterval = repeatInterval;
	}
//	public Integer getUsable() {
//		return usable;
//	}
//	public void setUsable(Integer usable) {
//		this.usable = usable;
//	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getRunnable() {
		return runnable;
	}
	public void setRunnable(Integer runnable) {
		this.runnable = runnable;
	}
	public Integer getUsable() {
		return usable;
	}
	public void setUsable(Integer usable) {
		this.usable = usable;
	}
	
	
		
	
	
	
}
