package com.coship.game.crawler.domain;



/**
 * 
 * @cms-domain
 * @BaseEntityStatus.java
 * @author 904032
 * @2014年3月21日-上午9:12:38
 */

public class BaseEntityStatus extends BaseEntityPrimaryKey {
	private Integer status;

	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

}
