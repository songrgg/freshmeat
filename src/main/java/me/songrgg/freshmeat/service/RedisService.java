package me.songrgg.freshmeat.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author songrgg
 * @since 1.0
 */
public interface RedisService {
  /**
   * <pre>
   * 通过key删除
   * @return 被删除的记录数
   * </pre>
   */
  long delete(String... keys);

  /**
   * <pre>
   * 通过keys删除
   * @return 被删除的记录数
   * </pre>
   */
  long delete(Collection<String> keys);

  /**
   * <pre>
   *  @param activeTime 秒
   *  @return 添加key value 并且设置存活时间
   * </pre>
   */
  boolean set(byte[] key, byte[] value, long activeTime);

  /**
   * <pre>
   * @param activeTime 秒
   * @return 添加key value 并且设置存活时间
   * </pre>
   */
  boolean set(String key, String value, long activeTime);

  /**
   * <pre>
   *  @param key
   *  @param value
   *  @return 添加key value
   * </pre>
   */
  boolean set(String key, String value);

  /**
   * <pre>
   *  @param key
   *  @param value
   *  @return 添加key value
   * </pre>
   */
  boolean set(byte[] key, byte[] value);

  /**
   * <pre>
   * @param key
   * @return 获得value
   * </pre>
   */
  String get(String key);

  /**
   * <pre>
   * @param key      键
   * @param delta    增量
   * @return 更新后的值
   * </pre>
   */
  Long incr(String key, long delta);

  /**
   * 指定键的值+1
   * <pre>
   * @param key      键
   * @return 更新后的值
   * </pre>
   */
  Long incr(String key);

  /**
   * 指定键的值-1
   * <pre>
   * @param key      键
   * @return 更新后的值
   * </pre>
   */
  Long decr(String key);

  /**
   * 指定键的值-delta
   * <pre>
   * @param key      键
   * @return 更新后的值
   * </pre>
   */
  Long decr(String key, long delta);

  /**
   * <pre>
   * @param pattern
   * @return 通过正则匹配keys
   * </pre>
   */
  Set<String> matchKeys(String pattern);

  /**
   * <pre>
   * @param key
   * @return 检查key是否已经存在
   * </pre>
   */
  boolean exists(String key);

  /**
   * <pre>
   * @return 清空所有数据
   * </pre>
   */
  boolean flushDB();

  void hset(String key, String field, String value);

  void hset(byte[] key, byte[] field, byte[] value);

  Boolean hexists(String key, String field);

  String hget(String key, String field);

  void hdel(String key, String... fields);

  List<String> hmget(String key, String... fields);

  List<String> mget(String... fields);

}