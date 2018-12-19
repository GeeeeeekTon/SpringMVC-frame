package com.leo.core.myutil;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.Pool;

import com.leo.core.mycache.redis.RedisCacheProvider;

/**
 * JedisTemplate 提供了一个template方法，负责对Jedis连接的获取与归还。 JedisAction<T> 和
 * JedisActionNoResult两种回调接口，适用于有无返回值两种情况。 同时提供一些最常用函数的封装, 如get/set/zadd等。
 * 
 * @author zhangzhen
 */
public class JedisTemplate {
	private static Logger logger = LoggerFactory.getLogger(JedisTemplate.class);

	private static JedisTemplate instance;

	private Pool<Jedis> jedisPool;

	private JedisTemplate(Pool<Jedis> jedisPool) {
		this.jedisPool = jedisPool;
	}

	/**
	 * 单例方法
	 * 
	 * @return
	 */
	public final static JedisTemplate getInstance() {
		if (instance == null) {
			synchronized (JedisTemplate.class) {
				if (instance == null) {
					instance = new JedisTemplate(RedisCacheProvider.getPool());
				}
			}
		}
		return instance;
	}

	/**
	 * 执行有返回结果的action。
	 */
	public <T> T execute(JedisAction<T> jedisAction) throws JedisException {
		Jedis jedis = null;
		boolean broken = false;
		try {
			jedis = jedisPool.getResource();
			return jedisAction.action(jedis);
		} catch (JedisConnectionException e) {
			logger.error("Redis connection lost.", e);
			broken = true;
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
	}

	/**
	 * 执行无返回结果的action。
	 */
	public void execute(JedisActionNoResult jedisAction) throws JedisException {
		Jedis jedis = null;
		boolean broken = false;
		try {
			jedis = jedisPool.getResource();
			jedisAction.action(jedis);
		} catch (JedisConnectionException e) {
			logger.error("Redis connection lost.", e);
			broken = true;
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
	}

	/**
	 * 根据连接是否已中断的标志，分别调用returnBrokenResource或returnResource。
	 */
	protected void closeResource(Jedis jedis, boolean connectionBroken) {
		if (jedis != null) {
			try {
				if (connectionBroken) {
					jedisPool.returnBrokenResource(jedis);
				} else {
					jedisPool.returnResource(jedis);
				}
			} catch (Exception e) {
				logger.error(
						"Error happen when return jedis to pool, try to close it directly.",
						e);
				JedisUtils.closeJedis(jedis);
			}
		}
	}

	/**
	 * 获取内部的pool做进一步的动作。
	 */
	public Pool<Jedis> getJedisPool() {
		return jedisPool;
	}

	/**
	 * 有返回结果的回调接口定义。
	 */
	public interface JedisAction<T> {
		T action(Jedis jedis);
	}

	/**
	 * 无返回结果的回调接口定义。
	 */
	public interface JedisActionNoResult {
		void action(Jedis jedis);
	}

	// ////////////// 常用方法的封装 ///////////////////////// //

	// ////////////// 公共 ///////////////////////////
	/**
	 * 删除key, 如果key存在返回true, 否则返回false。
	 */
	public Boolean del(final String... keys) {
		return execute(new JedisAction<Boolean>() {

			@Override
			public Boolean action(Jedis jedis) {
				return jedis.del(keys) == 1 ? true : false;
			}
		});
	}
	public Boolean del(final byte key[]){
		return execute(new JedisAction<Boolean>() {
			@Override
			public Boolean action(Jedis jedis) {
				return jedis.del(key) == 1 ? true : false;
			}
		});
	}
	
	public Boolean exists(final String key) {
		return execute(new JedisAction<Boolean>() {

			@Override
			public Boolean action(Jedis jedis) {
				return jedis.exists(key);
			}
		});
	}
	public void flushDB() {
		execute(new JedisActionNoResult() {

			@Override
			public void action(Jedis jedis) {
				jedis.flushDB();
			}
		});
	}

	// ////////////// 关于String ///////////////////////////
	/**
	 * 如果key不存在, 返回null.
	 */
	public String get(final String key) {
		return execute(new JedisAction<String>() {

			@Override
			public String action(Jedis jedis) {
				return jedis.get(key);
			}
		});
	}

	/**
	 * 如果key不存在, 返回null.
	 */
	public Long getAsLong(final String key) {
		String result = get(key);
		return result != null ? Long.valueOf(result) : null;
	}

	/**
	 * 如果key不存在, 返回null.
	 */
	public Integer getAsInt(final String key) {
		String result = get(key);
		return result != null ? Integer.valueOf(result) : null;
	}

	public void set(final String key, final String value) {
		execute(new JedisActionNoResult() {

			@Override
			public void action(Jedis jedis) {
				jedis.set(key, value);
			}
		});
	}

	public void setex(final String key, final String value, final int seconds) {
		execute(new JedisActionNoResult() {

			@Override
			public void action(Jedis jedis) {
				jedis.setex(key, seconds, value);
			}
		});
	}

	/**
	 * 如果key还不存在则进行设置，返回true，否则返回false.
	 */
	public Boolean setnx(final String key, final String value) {
		return execute(new JedisAction<Boolean>() {

			@Override
			public Boolean action(Jedis jedis) {
				return jedis.setnx(key, value) == 1 ? true : false;
			}
		});
	}

	/**
	 * 综合setNX与setEx的效果。
	 */
	public Boolean setnxex(final String key, final String value,
			final int seconds) {
		return execute(new JedisAction<Boolean>() {

			@Override
			public Boolean action(Jedis jedis) {
				String result = jedis.set(key, value, "NX", "EX", seconds);
				return JedisUtils.isStatusOk(result);
			}
		});
	}

	public Long incr(final String key) {
		return execute(new JedisAction<Long>() {
			@Override
			public Long action(Jedis jedis) {
				return jedis.incr(key);
			}
		});
	}

	public Long expire(final String key, final int seconds) {
		return execute(new JedisAction<Long>() {
			@Override
			public Long action(Jedis jedis) {
				return jedis.expire(key, seconds);
			}
		});
	}
	public Long expire(final byte[] key, final int seconds) {
		return execute(new JedisAction<Long>() {
			@Override
			public Long action(Jedis jedis) {
				return jedis.expire(key, seconds);
			}
		});
	}

	public Long ttl(final String key) {
		return execute(new JedisAction<Long>() {
			@Override
			public Long action(Jedis jedis) {
				return jedis.ttl(key);
			}
		});
	}
	public Long ttl(final byte[] key) {
		return execute(new JedisAction<Long>() {
			@Override
			public Long action(Jedis jedis) {
				return jedis.ttl(key);
			}
		});
	}

	public Long decr(final String key) {
		return execute(new JedisAction<Long>() {
			@Override
			public Long action(Jedis jedis) {
				return jedis.decr(key);
			}
		});
	}

	// ////////////// 关于List ///////////////////////////
	public void rpush(final String key, final String... values) {
		execute(new JedisActionNoResult() {
			@Override
			public void action(Jedis jedis) {
				jedis.rpush(key, values);
			}
		});
	}
	public List<String> lrangeAll(final String key){
		return execute(new JedisAction<List<String>>() {
			@Override
			public List<String> action(Jedis jedis) {
				return jedis.lrange(key, 0, -1);
			}
		});
	}
	/**
	 * 返回List长度, key不存在时返回0，key类型不是list时抛出异常.
	 */
	public Long llen(final String key) {
		return execute(new JedisAction<Long>() {
			@Override
			public Long action(Jedis jedis) {
				return jedis.llen(key);
			}
		});
	}
	public void hset(final byte[] key, final byte[] field, final byte[] value) {
		execute(new JedisActionNoResult() {
			@Override
			public void action(Jedis jedis) {
				jedis.hset(key, field, value);
			}
		});
	}
	public byte[] hget(final byte[] key, final byte[] field) {
		return execute(new JedisAction<byte[]>() {
			@Override
			public byte[] action(Jedis jedis) {
				return jedis.hget(key, field);
			}
		});
	}
	public Long hdel(final byte[] key, final byte[]... fields) {
		return execute(new JedisAction<Long>() {
			@Override
			public Long action(Jedis jedis) {
				return jedis.hdel(key, fields);
			}
		});
	}
	public Long hlen(final byte[] key) {
		return execute(new JedisAction<Long>() {
			@Override
			public Long action(Jedis jedis) {
				return jedis.hlen(key);
			}
		});
	}
	public Set<byte[]> hkeys(final byte[] key){
		return execute(new JedisAction<Set<byte[]>>() {
			@Override
			public Set<byte[]> action(Jedis jedis) {
				return jedis.hkeys(key);
			}
		});
	}
	public List<byte[]> hvals(final byte[] key){
		return execute(new JedisAction<List<byte[]>>() {
			@Override
			public List<byte[]> action(Jedis jedis) {
				return jedis.hvals(key);
			}
		});
	}

	public void lpush(final String key, final String... values) {
		execute(new JedisActionNoResult() {
			@Override
			public void action(Jedis jedis) {
				jedis.lpush(key, values);
			}
		});
	}

	public String rpop(final String key) {
		return execute(new JedisAction<String>() {

			@Override
			public String action(Jedis jedis) {
				return jedis.rpop(key);
			}
		});
	}



	/**
	 * 删除List中的第一个等于value的元素，value不存在或key不存在时返回false.
	 */
	public Boolean lremOne(final String key, final String value) {
		return execute(new JedisAction<Boolean>() {
			@Override
			public Boolean action(Jedis jedis) {
				Long count = jedis.lrem(key, 1, value);
				return (count == 1);
			}
		});
	}

	/**
	 * 删除List中的所有等于value的元素，value不存在或key不存在时返回false.
	 */
	public Boolean lremAll(final String key, final String value) {
		return execute(new JedisAction<Boolean>() {
			@Override
			public Boolean action(Jedis jedis) {
				Long count = jedis.lrem(key, 0, value);
				return (count > 0);
			}
		});
	}

	// ////////////// 关于hash///////////////////////////
	public String hget(final String key, final String field) {
		return execute(new JedisAction<String>() {
			@Override
			public String action(Jedis jedis) {
				return jedis.hget(key, field);
			}
		});
	}

	public void hset(final String key, final String field, final String value) {
		execute(new JedisActionNoResult() {

			@Override
			public void action(Jedis jedis) {
				jedis.hset(key, field, value);
			}
		});
	}

	public List<String> hmget(final String key, final String[] fields) {
		return execute(new JedisAction<List<String>>() {
			@Override
			public List<String> action(Jedis jedis) {
				return jedis.hmget(key, fields);
			}
		});
	}

	public void hmset(final String key, final Map<String, String> map) {
		execute(new JedisActionNoResult() {

			@Override
			public void action(Jedis jedis) {
				jedis.hmset(key, map);
			}
		});
	}

	public Long hdel(final String key, final String... fieldsName) {
		return execute(new JedisAction<Long>() {
			@Override
			public Long action(Jedis jedis) {
				return jedis.hdel(key, fieldsName);
			}
		});
	}

	public Set<String> hkeys(final String key) {
		return execute(new JedisAction<Set<String>>() {
			@Override
			public Set<String> action(Jedis jedis) {
				return jedis.hkeys(key);
			}
		});
	}

	public Boolean hmset(final byte[] key, final Map<byte[], byte[]> hash) {
		return execute(new JedisAction<Boolean>() {
			@Override
			public Boolean action(Jedis jedis) {
				String result = jedis.hmset(key, hash);
				return JedisUtils.isStatusOk(result);
			}
		});
	}

	public List<byte[]> hmget(final byte[] key, final byte[]... fields) {
		return execute(new JedisAction<List<byte[]>>() {
			@Override
			public List<byte[]> action(Jedis jedis) {
				List<byte[]> list = jedis.hmget(key, fields);
				return list;
			}
		});
	}

	// ////////////// 关于Sorted Set ///////////////////////////
	/**
	 * 加入Sorted set, 如果member在Set里已存在, 只更新score并返回false, 否则返回true.
	 */
	public Boolean zadd(final String key, final String member,
			final double score) {
		return execute(new JedisAction<Boolean>() {

			@Override
			public Boolean action(Jedis jedis) {
				return jedis.zadd(key, score, member) == 1 ? true : false;
			}
		});
	}

	/**
	 * 删除sorted set中的元素，成功删除返回true，key或member不存在返回false。
	 */
	public Boolean zrem(final String key, final String member) {
		return execute(new JedisAction<Boolean>() {

			@Override
			public Boolean action(Jedis jedis) {
				return jedis.zrem(key, member) == 1 ? true : false;
			}
		});
	}

	/**
	 * 当key不存在时返回null.
	 */
	public Double zscore(final String key, final String member) {
		return execute(new JedisAction<Double>() {

			@Override
			public Double action(Jedis jedis) {
				return jedis.zscore(key, member);
			}
		});
	}

	/**
	 * 返回sorted set长度, key不存在时返回0.
	 */
	public Long zcard(final String key) {
		return execute(new JedisAction<Long>() {

			@Override
			public Long action(Jedis jedis) {
				return jedis.zcard(key);
			}
		});
	}
}
