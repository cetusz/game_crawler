package com.coship.game.crawler.utils;

import java.io.File;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.log4j.Logger;
/**
 * 使用 FileAlterationListenerAdaptor 类的onFileChange方法监听
 * 配置文件的变化情况，一旦发生内容变化，刷新配置文件内存缓存
 * @author 907708
 *
 * @param <FileListenerAdaptor>
 */
public class ConfigFileChangeListener<FileListenerAdaptor> extends FileAlterationListenerAdaptor {
    
	Logger logger = Logger.getLogger(ConfigFileChangeListener.class);
	
	String configFilePath = System.getProperty("appConfigHome")+"/game_crawler/conf/conf.xml";
	@Override
	public void onFileChange(final File file) {
		 ConfigFactory.init(configFilePath);
	}
	 
	public void onStart(final FileAlterationObserver observer) {
		logger.info("start checking file:"+configFilePath);
	}

    @Override  
    public void onStop(final FileAlterationObserver observer) {
    	logger.info(" finished checking file:"+configFilePath);
    }

}
