/**
 * 
 */
package org.own.commons.utils;

import java.util.Calendar;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Administrator
 * 一个简易的缓存
 */
public class CacheUtil {

	private CacheUtil() {
		// TODO Auto-generated constructor stub
	}
	private static CacheUtil cacheUtil;
	static{
		cacheUtil = new CacheUtil();
	}
	
	public static CacheUtil getInstance(){
		return cacheUtil;
	}
	
	
	@SuppressWarnings("rawtypes")
	private Map<Object,Entry> cacheMap = new WeakHashMap<Object, Entry>(50);
	
	private ReadWriteLock lock = new ReentrantReadWriteLock();
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setCache(Object key,Object value,long timeOut){
		
		
		lock.writeLock().lock();
		try{
			cacheMap.put(key, new Entry(value, timeOut));
		}finally{
			lock.writeLock().unlock();
		}
	}
	public void setCache(Object key,Object value){
		 setCache(key,value,-1);
	}
	@SuppressWarnings("unchecked")
	public <T> T getCache(Object key){
		lock.readLock().lock();
		try{
			@SuppressWarnings("rawtypes")
			Entry entry = cacheMap.get(key);
			if(entry!=null ){ 
				if(entry.timeOut != -1 && entry.timeOut < System.currentTimeMillis()){
					lock.readLock().unlock();
					removeCache(key);
					lock.readLock().lock();
					return null;
				}else{
					return (T) entry.t;
				}
			}
		}finally{
			lock.readLock().unlock();
		}
		return null;
	}
	
	
	private void removeCache(Object key) {
		// TODO Auto-generated method stub
		lock.writeLock().lock();
		try{
			cacheMap.remove(key);
		}finally{
			lock.writeLock().unlock();
		}
	}


	static class Entry<T>{
		long timeOut = -1;
		T t = null;
		Entry(T t,long timeOut){
			this.t = t;
			this.timeOut = timeOut;
		}
	}
	
}
