package com.coship.game.crawler.thread;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * master主控线程
 * @author 907708
 *
 */
public class Master<T>{
	//work线程队列
	private Queue<T> queues = new ConcurrentLinkedQueue<T>();
	
	
	protected Map<String,Thread> threadMap = new HashMap<String,Thread>();
	
	protected Map<String,Object> resultMap = new ConcurrentHashMap<String, Object>();
	
	public Master(Worker<T> worker,int workCounts){
        worker.setWorkQueue(queues);
        worker.setResultMap(resultMap);
        for(int i=0;i<workCounts ;i++){
            threadMap.put(worker.getWorkerName()+"#"+Integer.toString(i), new Thread(worker,Integer.toString(i)));
        }
	}
	
	  //提交一个任务
    public void submit(T job){
    	queues.add(job);
    }
    
    public void addBatchJob(List<T> list){
    	queues.addAll(list);
    }
    
    public void execute(){
    	 for(Map.Entry<String, Thread> entry:threadMap.entrySet()){
             entry.getValue().start();
         }
    }
    
    public boolean isComplete(){
   	 for(Map.Entry<String, Thread> entry:threadMap.entrySet()){
            if(entry.getValue().getState()!=Thread.State.TERMINATED){
            	return false;
            }
     }
   	 return true;
   }

	public Map<String, Object> getResultMap() {
		return resultMap;
	}
    
    
    
	

	
	
}
