/**
 * 
 */
package org.own.commons.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author Administrator
 *
 */
public class RedisUtil {
	
	private static JedisPool pool = null;
	private static Logger Log = LogManager.getLogger(RedisUtil.class);
	
	private Properties properties;
	
	
	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	private void init(){
		Log.info("初始化 RedisUtil");
		try{
			JedisPoolConfig config = new JedisPoolConfig();
			for(Object object : properties.keySet()){
				String param = ((String)object);
				System.out.println(param);
				if(!param.startsWith("jedis.")) continue	;
				param = param.replace("jedis.", "");
				//BeanInfoUtil.setBean(config, param, properties.get(param));
				org.apache.commons.beanutils.BeanUtils.setProperty(config, param, properties.get(param));
			}
			pool = new JedisPool(config, 
						properties.getProperty("host.jedis.uri"),  
						Integer.parseInt(properties.getProperty("host.jedis.port")), 
						100<<5,
						properties.getProperty("host.jedis.password"));
		}catch (Exception e) {
			Log.error(e.getMessage(),e);
			e.printStackTrace();
		}
	}

	public static Jedis getJedis(){
		return  pool.getResource();
	}
	@SuppressWarnings("deprecation")
	public static void returnJedis(Jedis jedis,boolean error){
		if(error)
			pool.returnBrokenResource(jedis);
		else
			pool.returnResource(jedis);
	}
	@SuppressWarnings("deprecation")
	public static void returnJedis(Jedis jedis){
		returnJedis(jedis,false);
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
		/*RedisUtil redisUtil = new RedisUtil();
		Properties properties = new Properties();
		properties.load(new FileInputStream("F:\\eclipse_win10_works\\own\\framework\\commons\\src\\main\\resources\\jedis.properties"));
		redisUtil.setProperties(properties);
		redisUtil.init();
		TimeUnit.SECONDS.sleep(10);
		Jedis jedis = redisUtil.getJedis();
		jedis.set("keyq", "val1");
		System.out.println(jedis.get("keyq"));
		*/
			// 池基本配置
			/*JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxIdle(5);
			config.setMaxTotal(20);
			config.setMaxWaitMillis(1000l);
			config.setTestOnBorrow(false);*/
		 try {
	            JedisPoolConfig config = new JedisPoolConfig();
	            //连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
	            config.setBlockWhenExhausted(true);
	            //设置的逐出策略类名, 默认DefaultEvictionPolicy(当连接超过最大空闲时间,或连接数超过最大空闲连接数)
	            config.setEvictionPolicyClassName("org.apache.commons.pool2.impl.DefaultEvictionPolicy"); 
	            //是否启用pool的jmx管理功能, 默认true
	            config.setJmxEnabled(true);
	            //MBean ObjectName = new ObjectName("org.apache.commons.pool2:type=GenericObjectPool,name=" + "pool" + i); 默认为"pool", JMX不熟,具体不知道是干啥的...默认就好.
	            config.setJmxNamePrefix("pool");
	            //是否启用后进先出, 默认true
	            config.setLifo(true);
	            //最大空闲连接数, 默认8个
	            config.setMaxIdle(8);
	            //最大连接数, 默认8个
	            config.setMaxTotal(8);
	            //获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
	            config.setMaxWaitMillis(-1);
	            //逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
	            config.setMinEvictableIdleTimeMillis(1800000);
	            //最小空闲连接数, 默认0
	            config.setMinIdle(0);
	            //每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
	            config.setNumTestsPerEvictionRun(3);
	            //对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,不再根据MinEvictableIdleTimeMillis判断  (默认逐出策略)   
	            config.setSoftMinEvictableIdleTimeMillis(1800000);
	            //在获取连接的时候检查有效性, 默认false
	            config.setTestOnBorrow(false);
	            //在空闲时检查有效性, 默认false
	            config.setTestWhileIdle(false);
	            //逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
	            config.setTimeBetweenEvictionRunsMillis(-1);
	            JedisPool  jedisPool = new JedisPool(config,"119.23.11.51", 6379, 3000);
	           // JedisPool	jedisPool = new JedisPool(config,"119.23.11.51", 6379);
	            
	            jedisPool.getResource().set("V1", "FG");

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
			
	}
}
