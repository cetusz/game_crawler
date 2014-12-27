/**
 * 
 * @cms-manager
 * @GlobalTriggerListner.java
 * @907708
 * @2014年6月27日-上午11:21:48
 */
package com.coship.game.crawler.quartz;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.coship.game.crawler.utils.PropertiesFileUtil;

/**
 * @cms-manager
 * @GlobalTriggerListner.java
 * @author 904032
 * @2014年6月27日-上午11:21:48
 */
@Component
public class CoshipTriggerListner implements TriggerListener {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public String getName() {
		return "coshipTriggerListner"; 
	}

	@Override
	public void triggerComplete(Trigger arg0, JobExecutionContext arg1, int arg2) {
		//System.out.println(33);
	}

	@Override
	public void triggerFired(Trigger arg0, JobExecutionContext arg1) {
		//System.out.println(33);
	}

	@Override
	public void triggerMisfired(Trigger arg0) {
		//System.out.println(33);
	}

	@Override
	public boolean vetoJobExecution(Trigger trigger, JobExecutionContext jobExecContext) {
		
		//获取主机列表
		String hosts = PropertiesFileUtil.getStringValue(System.getProperty("appConfigHome")+"/cap-vod/conf/config.properties",
				"host.ip.list");
		if(null==hosts||hosts.trim().isEmpty())
		{
			logger.warn("not setting ip hosts , task running ");
			return false;//未设置主机，执行任务
		}
		String[] hostArray = hosts.trim().split(";");
		Map<String, String> hostMap = new HashMap<String, String>();
		for(String host:hostArray)
		{
			hostMap.put(host, host);
		}
		
		List<String> ipList = this.getHostIps();
		boolean isTask = false;//是否主机，默认非主机
		for(String ip : ipList)
		{
			if(hostMap.containsKey(ip))
			{
				isTask = true;
				logger.warn("find the virtual host ip:"+ip);
				break;
			}
		}
		
		if(!isTask)
		{
			logger.warn("not find the virtual host ip , task stop ");
		}
		
		return !isTask;//取反，false代表执行
	}
	
	private List<String> getHostIps()
	{
		List<String> ipList = new ArrayList<String>();
		Enumeration<NetworkInterface> netInterfaces = null;
		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {  
		        NetworkInterface ni = netInterfaces.nextElement();  
		        //System.out.println("DisplayName:" + ni.getDisplayName());  
		        //System.out.println("Name:" + ni.getName());  
		        Enumeration<InetAddress> ips = ni.getInetAddresses();  
		        while (ips.hasMoreElements()) {  
		        	ipList.add(ips.nextElement().getHostAddress());
//		            System.out.println("IP:"  
//		            + ips.nextElement().getHostAddress());  
		        }  
		    } 
		} catch (SocketException e) {
			logger.error("{}",e);
		}
		return ipList;
	}
}
