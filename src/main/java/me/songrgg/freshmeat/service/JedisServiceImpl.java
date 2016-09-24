package me.songrgg.freshmeat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author songrgg
 * @since 1.0
 */
@Service
public class JedisServiceImpl implements RedisService {

  private static final String CHARSET = "UTF8";

  @Autowired
  private RedisTemplate<String, Serializable> redisTemplate;

  public void setRedisTemplate(
          RedisTemplate<String, Serializable> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Override
  public boolean set(final byte[] key, final byte[] value,
                     final long activeTime) {
    return redisTemplate.execute((RedisCallback<Boolean>) connection -> {
      boolean rs = true;
      connection.set(key, value);
      if (activeTime > 0) {
        rs = connection.expire(key, activeTime);
      }
      return rs;
    });
  }

  @Override
  public boolean set(String key, String value, long activeTime) {
    return this.set(key.getBytes(), value.getBytes(), activeTime);
  }

  @Override
  public boolean set(String key, String value) {
    return this.set(key, value, 0L);
  }

  @Override
  public boolean set(byte[] key, byte[] value) {
    return this.set(key, value, 0L);
  }

  @Override
  public String get(final String key) {
    return redisTemplate.execute((RedisCallback<String>) connection -> {
      try {
        byte[] value = connection.get(key.getBytes());
        return value == null ? "" : new String(value, CHARSET);
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
      return "";
    });
  }

  @Override
  public Set<String> matchKeys(String pattern) {
    return redisTemplate.keys(pattern);

  }

  @Override
  public boolean exists(final String key) {
    return redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.exists(key.getBytes()));
  }

  @Override
  public boolean flushDB() {
    return redisTemplate.execute((RedisCallback<Boolean>) connection -> {
      connection.flushDb();
      return true;
    });
  }

  @Override
  public long delete(final Collection<String> keys) {
    return redisTemplate.execute((RedisCallback<Long>) connection -> {
      long result = 0;
      for (String key : keys) {
        result = connection.del(key.getBytes());
      }
      return result;
    });
  }

  @Override
  public long delete(final String... keys) {
    Collection<String> cols = new ArrayList<>();
    Collections.addAll(cols, keys);
    return this.delete(cols);
  }

  @Override
  public Long incr(final String key) {
//        return redisTemplate.opsForValue().increment(key, 1);
    return incr(key, 1);
  }

  @Override
  public Long incr(final String key, long delta) {
//        return redisTemplate.opsForValue().increment(key, delta);
    return redisTemplate.execute((RedisCallback<Long>) connection -> connection.incrBy(key.getBytes(), delta));
  }

  @Override
  public Long decr(final String key) {
    return decr(key, 1);
  }

  @Override
  public Long decr(final String key, long delta) {
    return redisTemplate.execute((RedisCallback<Long>) connection -> connection.decrBy(key.getBytes(), delta));
  }

  public void hset(final String key, final String field, final String value) {
    redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.hSet(key.getBytes(), field.getBytes(), value.getBytes()));
  }

  public Boolean hexists(final String key, final String field) {
    return redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.hExists(key.getBytes(), field.getBytes()));
  }

  public String hget(final String key, final String field) {
    return redisTemplate.execute((RedisCallback<String>) connection -> {
      byte[] value = connection.hGet(key.getBytes(), field.getBytes());
      try {
        return value == null ? "" : new String(value, CHARSET);
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
      return null;
    });
  }

  @Override
  public void hdel(final String key, final String... fields) {
//        redisTemplate.opsForHash().delete(key, fields);
    List<byte[]> arrFields = new ArrayList<>();
    for (String field : fields) {
      arrFields.add(field.getBytes());
    }
    redisTemplate.execute((RedisCallback<Long>) connection -> connection.hDel(key.getBytes(), arrFields.toArray(new byte[arrFields.size()][])));
  }

  @Override
  public List<String> hmget(String key, String... fields) {
    List<byte[]> arrFields = new ArrayList<>();
    for (String field : fields) {
      arrFields.add(field.getBytes());
    }
    List<byte[]> results = redisTemplate.execute(
            (RedisCallback<List<byte[]>>) connection -> connection.hMGet(key.getBytes(), arrFields.toArray(new byte[arrFields.size()][]))
    );
    if (results == null) {
      return new ArrayList<>();
    }
    return transferEncoding(results);
  }

  @Override
  public List<String> mget(String... fields) {
    List<byte[]> arrFields = new ArrayList<>();
    for (String field : fields) {
      arrFields.add(field.getBytes());
    }

    List<byte[]> results = redisTemplate.execute(
            (RedisCallback<List<byte[]>>) connection -> connection.mGet(arrFields.toArray(new byte[arrFields.size()][]))
    );
    if (results == null) {
      return new ArrayList<>();
    }
    return transferEncoding(results);
  }

  private List<String> transferEncoding(List<byte[]> results) {
    final List<String> ret = new ArrayList<>();
    results.forEach(result -> {
      if (result != null) {
        try {
          ret.add(new String(result, CHARSET));
        } catch (UnsupportedEncodingException e) {
          ret.add(null);
        }
      } else {
        ret.add(null);
      }
    });
    return ret;
  }

  @Override
  public void hset(byte[] key, byte[] field, byte[] value) {
    redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.hSet(key, field, value));
  }

}