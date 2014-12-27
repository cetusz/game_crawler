/**
 * 
 * @cms-manager
 * @TaskListner.java
 * @907708
 * @2014年6月28日-下午1:58:24
 */
package com.coship.game.crawler.quartz;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.coship.game.crawler.service.TaskSpringService;

/**
 * @cms-manager
 * @TaskListner.java
 * @author 904032
 * @2014年6月28日-下午1:58:24
 */
public class TaskServlet  extends HttpServlet {
	
    Logger loger = LoggerFactory.getLogger(TaskServlet.class);
	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		String taskSpringServiceBeanName = this.getServletContext().getInitParameter("taskSpringServiceBean");
		if(StringUtils.isEmpty(taskSpringServiceBeanName)){
			taskSpringServiceBeanName = "taskSpringService";
		}
		
		ApplicationContext applicationContext = 
				WebApplicationContextUtils.getWebApplicationContext(this.getServletContext(),
						"org.springframework.web.servlet.FrameworkServlet.CONTEXT.spring-front-controller");
		Object taskBean = applicationContext.getBean(taskSpringServiceBeanName);
		if(taskBean==null){
		    loger.error("spring容器中获取任务bean失败");
			return;
		
		}
		TaskSpringService taskSpringService = (TaskSpringService)taskBean;
		//启动任务
		taskSpringService.start();
	}


}
