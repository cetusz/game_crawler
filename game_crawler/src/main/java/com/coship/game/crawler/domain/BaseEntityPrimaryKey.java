/**
 * 
 */
package com.coship.game.crawler.domain;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.my.common.domain.BaseEntity;

/**
 * 
 * @cms-domain
 * @BaseEntityPrimaryKey.java
 * @author 904032
 * @2014年3月21日-上午11:21:02
 */
public abstract class BaseEntityPrimaryKey implements BaseEntity<Long> {


	private Long id;
	@DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
	private Date createTime;
	@DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
	private Date lastUpdateTime;
	
	private Integer status;
	
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

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
	
}
