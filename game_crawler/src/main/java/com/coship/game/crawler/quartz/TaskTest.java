/**
 * 
 * @cms-manager
 * @TaskTest.java
 * @907708
 * @2014年6月27日-上午11:04:38
 */
package com.coship.game.crawler.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @cms-manager
 * @TaskTest.java
 * @author 904032
 * @2014年6月27日-上午11:04:38
 */

public class TaskTest {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private int count;
	private int count4Simple;
	
	public void execute(){
		logger.debug("TaskTest execute,run times="+count);
		count++;
		System.out.println(count);
	}
	
	public void execute4simple(){
		logger.debug("TaskTest execute4simple,run times="+count4Simple);
		count4Simple++;
		System.out.println(count4Simple);
	}
}
