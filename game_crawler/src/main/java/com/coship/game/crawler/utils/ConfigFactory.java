package com.coship.game.crawler.utils;

import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

/**
 * @ClassName: ConfigFactory
 * @Description:  配置接口对象
 */
public class ConfigFactory {

	private static final String CONFIG_FILE_DEFAULT_PATH = "/conf";
	private static XMLConfiguration config = null;

	/**
	 * @Title: init
	 * @Description: 初始化，加载配置文件
	 * @author: jianzr
	 * @date: Oct 14, 2010 5:31:59 PM
	 * @param configFilePath
	 */
	public static void init(String configFilePath) {
		// 如果未配置获取默认
		if (configFilePath == null) {
			configFilePath = CONFIG_FILE_DEFAULT_PATH;
		}
		try {
			config = new XMLConfiguration(configFilePath);
			config.setReloadingStrategy(new FileChangedReloadingStrategy());
		} catch (ConfigurationException e) {
			System.out.println("Fatal:Create Config Object Error!!!");
			e.printStackTrace();
//			System.exit(1);
			
		}
	}

	/**
	 * 不允许外部实例化
	 */
	private ConfigFactory() {
	}

	/**
	 * @Title: getString
	 * @Description: 获取配置的字符串
	 * @author: jianzr
	 * @param configXPath 配置项路
	 * @return
	 */
	public static String getString(String configXPath) {
		return config.getString(configXPath, null);
	}

	/**
	 * 获取配置的字符串
	 * @param configXPath 配置项路径
	 * @param defaultValue 配置项的默认
	 * @return
	 */
	public static String getString(String configXPath, String defaultValue) {
		return config.getString(configXPath, defaultValue);
	}

	/**
	 * 获取配置的整数
	 * @param configXPath 配置项路径
	 * @return
	 */
	public static int getInt(String configXPath) {
		return config.getInt(configXPath);
	}

	/**
	 * 获取配置的整数值
	 * @param configXPath 配置项路径
	 * @param defaultValue  配置项的默认路径
	 * @return
	 */
	public static int getInt(String configXPath, int defaultValue) {
		return config.getInt(configXPath, defaultValue);
	}

	/**
	 * 获取配置的float
	 * @param configXPath
	 * @return
	 */
	public static float getFloat(String configXPath) {
		return config.getFloat(configXPath, 1.0f);
	}

	/**
	 * 获取配置的boolean
	 * @param configXPath  配置项路径
	 * @return
	 */
	public static boolean getBoolean(String configXPath) {
		return config.getBoolean(configXPath);
	}

	/**
	 * 获取配置的boolean
	 * @param configXPath  配置项路径
	 * @param defaultValue  配置项的默认值
	 * @return
	 */
	public static boolean getBoolean(String configXPath, boolean defaultValue) {
		return config.getBoolean(configXPath, defaultValue);
	}

	/**
	 * 获取配置的List
	 * @param configXPath  配置项路径
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Object> getList(String configXPath) {
		return  config.getList(configXPath);
	}

	public static void main(String[] args) {
//		init("conf/config.xml");
		init("F:\\CAP\\git\\Game_Crawler\\game_crawler\\game_crawler\\conf\\conf.xml");
		System.out.println(ConfigFactory.getString("picture.ftpPath"));
	}
}
