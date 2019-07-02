package priv.hypo.chess.common;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 任务管理器
 * @author Administrator
 */
public class QueueTaskManager extends Thread {
	/** 结束标记，发起结束标记会设置为true*/
	private boolean finished = false;
	
	private Queue<Runnable> tasks = new LinkedList<Runnable>();
	
	public QueueTaskManager() {
		
	}
	
	@Override
	public void run() {
		while(!finished) {
			try {
				synchronized(this) {
					if(!tasks.isEmpty()) {
						Runnable task = tasks.poll();
						if(task != null) {
							task.run();
						}
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
		
	/**
	 * 发起结束请求
	 */
	public void finish() {
		this.finished = true;
	}
	
	public void add(Runnable task) {
		if(task != null) {
			tasks.offer(task);
		}
	}
	
	public static abstract class Task {
//		private String name;
		
		public Task(String name, Runnable run) {
//			this.name = name;
		}
	}
}