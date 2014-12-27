package com.coship.game.crawler.thread;

import java.util.Map;
import java.util.Queue;

/**
 * 工作线程,真正业务实现
 * @author 907708
 *
 */
public class Worker<T> implements Runnable {

	protected Queue<T> workQueue;
	
	protected Map<String,Object> resultMap;
	
	protected String workerName;
	
	public Worker(){
		
	}
	
	public Worker(String workerName){
		this.workerName = workerName;
	}
	
	
	@Override
	public void run() {
       while(!workQueue.isEmpty()){
    	   T crawler = workQueue.poll();
    	   Object object = handler(crawler);
    	   if(object!=null)
    	   resultMap.put(this.workerName+"#"+Long.toString(System.currentTimeMillis()), object);
       }
	}

	public Queue<T> getWorkQueue() {
		return workQueue;
	}

	public void setWorkQueue(Queue<T> workQueue) {
		this.workQueue = workQueue;
	}
	
	/***
	 * 子类覆盖处理方法
	 * @param object
	 * @param date
	 */
	protected Object handler(T object){
		return object;
	}

	public void setResultMap(Map<String, Object> resultMap) {
		this.resultMap = resultMap;
	}

	public String getWorkerName() {
		return workerName;
	}

	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}
	
}
