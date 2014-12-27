/**
 * 
 * @cap-video-service
 * @IAssetPubService.java
 * @907708
 * @2014年3月31日-下午2:35:00
 */
package com.coship.game.crawler.service;

import java.util.List;

import com.my.common.domain.Query;
import com.my.mybatis.support.Page;
import com.my.mybatis.support.Sort;

/**
 * @cap-video-service
 * @IAssetPubService.java
 * @author 904032
 * @2014年3月31日-下午2:35:00
 */
public interface BaseService <T>{
	
	/**
	 * 
	 * @param entity
	 */
	public Long saveOrUpdate(T entity);
	
	/**
	 * 查询
	 * @param id
	 * @return
	 */
	public T findOneById(Long id);
	
	/**
	 * 删除
	 * @param id
	 */
	public void deleteOne(Long id);
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void deleteMulti(List<Long> ids);

	/**
	 * 分页查询
	 * @param pager
	 * @param query
	 * @param sorts
	 * @return
	 */
	public Page<T> selectPageList(Query query, Sort sorts,int page,int rows);
	
	/**
	 * 查询列表
	 * @param query
	 * @param sorts
	 * @return
	 */
	public List<T> selectList(Query query, Sort sorts);
	
	

}
