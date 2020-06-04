/*
 * Author github: https://github.com/zs-neo
 * Author Email: 2931622851@qq.com
 */
package server.websocket;

import redis.clients.jedis.*;

import java.util.Set;

/**
 * @author zhousheng
 * @version 1.0
 * @since 2020/5/31 20:32
 */
public class RedisServer {
	
	/**
	 * -redis数据库设计
	 * zet userscore("userscore"-"score"-"openId")
	 * hash userdata("userdata"-"openId"-"nickname")
	 * hash userorder("userorder"-"openId"- 1/0) 暂不用，目前使用openid的大小来判断先后手
	 * hash userresult("userresult"-"openid"- 0-100)
	 */
	private final static String SERVERADRESS = "106.55.36.70";
	private final static int SERVERPORT = 6379;
	private final static String USERDATA = "userdata";
	private final static String USERSCORE = "userscore";
	private final static String USERRESULT = "userresult";
	
	private JedisPool pool;
	
	// 单例
	static RedisServer redis;
	
	public static RedisServer getInstance() {
		if (redis == null) {
			redis = new RedisServer();
		}
		return redis;
	}
	
	private JedisPool getPool() {
		if (pool == null) {
			pool = new JedisPool(new JedisPoolConfig(),
					SERVERADRESS,
					SERVERPORT,
					Protocol.DEFAULT_TIMEOUT,
					"aa123456");
		}
		return pool;
	}
	
	/**
	 * userInfo : {openid:xx,nickname:xx}
	 *
	 * @param OpenId
	 * @param userInfo
	 */
	public void setUserInfo(String OpenId, String userInfo) {
		Jedis jedis = null;
		try {
			jedis = getPool().getResource();
			jedis.hset(USERDATA, OpenId, userInfo);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	/**
	 * 获取用户信息nickname:xx
	 *
	 * @param openId
	 * @return
	 */
	public String getUserInfo(String openId) {
		Jedis jedis = null;
		try {
			jedis = getPool().getResource();
			return jedis.hget(USERDATA, openId);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	/**
	 * 获取用户对局要猜的数字
	 *
	 * @param openId
	 * @return
	 */
	public String getUserResult(String openId) {
		Jedis jedis = null;
		try {
			jedis = getPool().getResource();
			return jedis.hget(USERRESULT, openId);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	/**
	 * 设置用户对局要猜的数
	 *
	 * @param openId
	 * @param result
	 */
	public void setUserResult(String openId, String result) {
		Jedis jedis = null;
		try {
			jedis = getPool().getResource();
			jedis.hset(USERRESULT, openId, result);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	/**
	 * 删除用户对局要猜的数
	 *
	 * @param openId
	 * @return
	 */
	public void deleteUserResult(String openId) {
		Jedis jedis = null;
		try {
			jedis = getPool().getResource();
			jedis.hdel(USERRESULT, openId);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	/**
	 * 增加用户分数
	 * userscore : {openid:xx,score:xx}
	 *
	 * @param openId
	 * @param score
	 */
	public void addUserScore(String openId, int score) {
		Jedis jedis = null;
		try {
			jedis = getPool().getResource();
			jedis.zincrby(USERSCORE, score, openId);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	/**
	 * 设置用户分数
	 * userscore : {openid:xx,score:xx}
	 *
	 * @param openId
	 * @param score
	 */
	public void setUserScore(String openId, int score) {
		Jedis jedis = null;
		try {
			jedis = getPool().getResource();
			jedis.zadd(USERSCORE, score, openId);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	/**
	 * 减少用户分数
	 *
	 * @param openId
	 * @param score
	 */
	public void decreaseUserScore(String openId, int score) {
		Jedis jedis = null;
		try {
			jedis = getPool().getResource();
			int add = score * (-1);
			jedis.zincrby(USERSCORE, add, openId);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	/**
	 * 获取用户分数
	 *
	 * @param openId
	 */
	public Double getUserScore(String openId) {
		Jedis jedis = null;
		try {
			jedis = getPool().getResource();
			Double res = jedis.zscore(USERSCORE, openId);
			return res;
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	/**
	 * 获取用户排名,默认取前10名
	 */
	public Set<Tuple> getUserRank() {
		Jedis jedis = null;
		try {
			jedis = getPool().getResource();
			return jedis.zrangeWithScores(USERSCORE, 0, 10);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
	
	public static void main(String[] args) {
		System.out.println(Math.round(-15.36));
	}
	
}
