/*
 * Author github: https://github.com/zs-neo
 * Author Email: 2931622851@qq.com
 */
package server.websocket;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author zhousheng
 * @version 1.0
 * @since 2020/5/31 20:32
 */
public class RedisServer {
	
	private JedisPool pool;
	static RedisServer redis;
	String USERINFO = "userInfo";
	
	public static RedisServer getInstance() {
		if (redis == null) {
			redis = new RedisServer();
		}
		return redis;
	}
	
	private JedisPool getPool() {
		if (pool == null) {
//			pool = new JedisPool(new JedisPoolConfig(), "localhost", Protocol.DEFAULT_PORT, Protocol.DEFAULT_TIMEOUT, "1314");
//            pool = new JedisPool(new JedisPoolConfig(), "guanzhiwangluogongyi.vip", Protocol.DEFAULT_PORT, Protocol.DEFAULT_TIMEOUT, "1314");
//            pool = new JedisPool(new JedisPoolConfig(), "47.107.178.120", Protocol.DEFAULT_PORT, Protocol.DEFAULT_TIMEOUT, "1314");
//            pool = new JedisPool(new JedisPoolConfig(), "47.107.178.120", Protocol.DEFAULT_PORT, Protocol.DEFAULT_TIMEOUT, "1314");
		}
		return pool;
	}
	
	public String getUserInfo(String OpenId) {
		Jedis jedis = null;
		try {
			jedis = getPool().getResource();
			return jedis.hget(USERINFO, OpenId);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	public void setUserInfo(String OpenId, String userInfo) {
		Jedis jedis = null;
		try {
			jedis = getPool().getResource();
			jedis.hset(USERINFO, OpenId, userInfo);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
}
