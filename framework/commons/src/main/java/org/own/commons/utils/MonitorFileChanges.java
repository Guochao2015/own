/**
 * 
 */
package org.own.commons.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jiangguochao
 *
 * 监听文件改变
 */
public class MonitorFileChanges {

	private Map<String,Runnable> handles = new HashMap<String, Runnable>(10);
	
	private static Map<File,MonitorFileChanges> fileMonitorFile = new  HashMap<File, MonitorFileChanges>(10);
	
	public MonitorFileChanges(File dir) throws IOException {
		// TODO Auto-generated constructor stub
		//获得监听器
		final WatchService newWatchService = FileSystems.getDefault().newWatchService();
		//注册
		Paths.get(dir.getAbsolutePath()).register(newWatchService, StandardWatchEventKinds.ENTRY_MODIFY);
		Thread thread = new Thread("find file watcher"){
			public void run() {
				while(true){
					WatchKey take;
					try {
						take = newWatchService.take();
						for(WatchEvent watchEvent:take.pollEvents()){
							System.out.println(watchEvent.context()+" ??? "+ watchEvent.kind());
							invokeHandle(watchEvent.context().toString());
						}
						if(!take.reset()){ //
							break ;
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		thread.setDaemon(true);
		thread.start();
		
	}

	protected void invokeHandle(String string) {
		// TODO Auto-generated method stub
		Runnable runnable = handles.get(string);
		if(runnable!=null){
			runnable.run();
		}
		
	}

	public static synchronized MonitorFileChanges getMonitorFileChanges(File dir){
		MonitorFileChanges monitorFileChanges = fileMonitorFile.get(dir);
		if(monitorFileChanges==null){
			try {
				monitorFileChanges = new MonitorFileChanges(dir);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			fileMonitorFile.put(dir, monitorFileChanges);
		}
		return monitorFileChanges;
	}
	
	public void addHandle(String file,Runnable runnable){
		this.handles.put(file, runnable);
	}
	
}
