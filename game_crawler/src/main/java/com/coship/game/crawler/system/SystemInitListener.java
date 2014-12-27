package com.coship.game.crawler.system;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.log4j.Logger;

import com.coship.game.crawler.utils.ConfigFactory;
import com.coship.game.crawler.utils.ConfigFileChangeListener;

public class SystemInitListener implements ServletContextListener{

    Logger loger = Logger.getLogger(ServletContextListener.class);
     
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ConfigFactory.init(System.getProperty("appConfigHome")+"/game_crawler/conf/conf.xml");
//		FileAlterationObserver observer = new FileAlterationObserver(   
//				  System.getProperty("appConfigHome")+"/game_crawler/conf/conf.xml",null,    
//                  null);   
//				observer.addListener(new ConfigFileChangeListener());   
//				FileAlterationMonitor monitor = new FileAlterationMonitor(10000,observer);   
//				// 开始监控   
//				try {
//					monitor.start();
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//		}  
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}

}
