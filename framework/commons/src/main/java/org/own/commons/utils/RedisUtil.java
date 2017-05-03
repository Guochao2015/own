/**
 * 
 */
package org.own.commons.utils;

import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author Administrator
 *
 */
public class RedisUtil {
	
	private JedisPool pool = null;
	
	private void init(){
		try(InputStream resourceAsStream = this.getClass().getResourceAsStream("jedis.properties");){
			Properties properties = new Properties();
			properties.load(resourceAsStream);
			JedisPoolConfig config = new JedisPoolConfig();
			for(Object object : properties.keySet()){
				String param = ((String)object);
				if(!param.startsWith("jedis.")) continue	;
				param = param.replace("jedis.", "");
				BeanInfoUtil.setBean(config, param, properties.get(param));
			}
			pool = new JedisPool(config, 
						properties.getProperty("host.jedis.ip"),  
						Integer.parseInt(properties.getProperty("host.jedis.port")), 
						100<<5,
						properties.getProperty("host.jedis.password"));
		}catch (Exception e) {
			
		}
	}

}
