//package com.coship.game.crawler.system;
//
//import javax.servlet.ServletContextEvent;
//import javax.servlet.ServletContextListener;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import ch.qos.logback.classic.LoggerContext;
//import ch.qos.logback.classic.joran.JoranConfigurator;
//import ch.qos.logback.core.joran.spi.JoranException;
//
//public class LogbackListener implements ServletContextListener {
//    Logger loger = LoggerFactory.getLogger(LogbackListener.class);
//	@Override
//	public void contextInitialized(ServletContextEvent event) {
//		    String logbackConfigLocation = System.getProperty("appConfigHome")+"/game_crawler/conf/logback.xml"; 
//	        String fn = event.getServletContext().getRealPath(logbackConfigLocation);  
//	        try {  
//	            LoggerContext loggerContext = (LoggerContext)LoggerFactory.getILoggerFactory();  
//	            loggerContext.reset();  
//	            JoranConfigurator joranConfigurator = new JoranConfigurator();  
//	            joranConfigurator.setContext(loggerContext);  
//	            joranConfigurator.doConfigure(fn);  
//	            loger.debug("loaded slf4j configure file from {}", fn);  
//	        }  
//	        catch (JoranException e) {  
//	        	loger.error("can loading slf4j configure file from " + fn, e);  
//	        }  
//	}
//
//	@Override
//	public void contextDestroyed(ServletContextEvent sce) {
//
//	}
//
//}
