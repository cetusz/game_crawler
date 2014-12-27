/**
 * 
 */
package com.coship.game.crawler.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.my.mybatis.support.Page;

/**
 * @author 904032
 *
 */
public class BaseController {
	
	private Logger logger = Logger.getLogger(getClass());
	
    public void responseByError(HttpServletResponse response ,int code,String message)
    {
		try {
			response.sendError(400, "ADI content is null.");
		} catch (IOException e) {
	          logger.error("处理消息返回失败", e);
		}
    }
    
    /**
     * 
     * @param succ
     * @param message
     * @return
     */
    public String makeRespMessage4Json(boolean succ , String message , Exception e)
    {
    	StringBuffer buffer = new StringBuffer();
    	buffer.append("{");
    	buffer.append("\"success\"").append(":").append(succ);
    	buffer.append(",");
    	buffer.append("\"message\"").append(":\"");
    	if(e!=null)
    	{
        	if(e instanceof Exception)
        	{
        		message = e.getMessage();
        	}
        	else if(e instanceof DataAccessException
        			|| e instanceof SQLException)
        	{
        		message = "数据库异常";//TODO
        	}
    	}

    	buffer.append(message);
    	buffer.append("\"}");
    	return buffer.toString();
    }
    
	/**
	 * 回馈消息
	 * @param response
	 * @param message
	 */
    public void responseMsg(HttpServletResponse response , String message)
    {
        PrintWriter out = null;
        try 
        {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(message);
            out.flush();
            out.close();
        } catch (IOException e) {
            logger.error("处理消息返回失败", e);
        } finally {
        	IOUtils.closeQuietly(out);
        }
    }
    
	/**
	 * 提供json返回拼装封装
	 * @param t
	 * @return
	 */
	protected <T> ResponseEntity<T> createResponseEntity(T t)
	{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.TEXT_PLAIN);
		
		return new ResponseEntity<T>(t, headers , HttpStatus.OK);
	}
	
	/**
	 * id字符串转数字型list.为空判断上层调用这决定
	 * @param idsStr
	 * @return
	 */
	protected List<Integer> convertIdsStr2Integer(String idsStr, String regex)
	{
		String[] rightArray = idsStr.split(regex);
		List<Integer> list = new ArrayList<Integer>();
		for(String rightId : rightArray)
		{
			list.add(Integer.valueOf(rightId));
		}
		return list;
	}
	
	
	/**
	 * id字符串转数字型list.为空判断上层调用这决定
	 * @param idsStr
	 * @return
	 */
	protected List<Long> convertIdsStr2Long(String idsStr, String regex)
	{
		String[] rightArray = idsStr.split(regex);
		List<Long> list = new ArrayList<Long>();
		for(String rightId : rightArray)
		{
			list.add(Long.valueOf(rightId));
		}
		return list;
	}
	
	/**
	 * 通用分页json返回处理
	 * @param pager
	 * @return
	 */
	public <T>  ResponseEntity<Map<String,Object>> createPageResponseEntity(Page<T> pager)
	{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("total",pager.getTotal());
		resultMap.put("rows", pager.getResult());
		return new ResponseEntity<Map<String, Object>>(resultMap, headers , HttpStatus.OK);
	}
	
	protected Map<String,Object> result(boolean successOrFailure,String message){
		Map<String,Object> result = new HashMap<String,Object>();
	 	result.put("success", successOrFailure);
        result.put("message", message);
		return result;
	}

}
