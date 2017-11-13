package saber.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;
import sun.rmi.runtime.Log;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocketFactory;
import java.util.Set;

/**
 * @author Kahle
 */
public class Jediser {
    private static final Logger log = LoggerFactory.getLogger(Jediser.class);

    public static Jediser on() {
        return new Jediser();
    }

    public static Jediser on(String host) {
        return new Jediser().setHost(host);
    }

    public static Jediser on(String host, Integer port) {
        return new Jediser().setHost(host).setPort(port);
    }

    public static Jediser on(String host, Integer port, String password) {
        return new Jediser().setHost(host).setPort(port).setPassword(password);
    }

    private JedisPoolConfig jedisPoolConfig;
    private String host = "127.0.0.1";
    private Integer port = 6379;
    private String password = null;
    private Integer database = 0;
    private String clientName = null;
    private Integer connectionTimeout = 6000;
    private Integer soTimeout = 6000;

    private Boolean ssl = false;
    private SSLSocketFactory sslSocketFactory = null;
    private SSLParameters sslParameters = null;
    private HostnameVerifier hostnameVerifier = null;

    private JedisPool jedisPool;

    public JedisPoolConfig getJedisPoolConfig() {
        return jedisPoolConfig;
    }

    public Jediser setJedisPoolConfig(JedisPoolConfig jedisPoolConfig) {
        this.jedisPoolConfig = jedisPoolConfig;
        return this;
    }

    public String getHost() {
        return host;
    }

    public Jediser setHost(String host) {
        this.host = host;
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public Jediser setPort(Integer port) {
        this.port = port;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Jediser setPassword(String password) {
        this.password = password;
        return this;
    }

    public Integer getDatabase() {
        return database;
    }

    public Jediser setDatabase(Integer database) {
        this.database = database;
        return this;
    }

    public String getClientName() {
        return clientName;
    }

    public Jediser setClientName(String clientName) {
        this.clientName = clientName;
        return this;
    }

    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    public Jediser setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public Integer getSoTimeout() {
        return soTimeout;
    }

    public Jediser setSoTimeout(Integer soTimeout) {
        this.soTimeout = soTimeout;
        return this;
    }

    public Boolean getSsl() {
        return ssl;
    }

    public Jediser setSsl(Boolean ssl) {
        this.ssl = ssl;
        return this;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return sslSocketFactory;
    }

    public Jediser setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
        return this;
    }

    public SSLParameters getSslParameters() {
        return sslParameters;
    }

    public Jediser setSslParameters(SSLParameters sslParameters) {
        this.sslParameters = sslParameters;
        return this;
    }

    public HostnameVerifier getHostnameVerifier() {
        return hostnameVerifier;
    }

    public Jediser setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier;
        return this;
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public Jediser setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
        return this;
    }

    private Jediser() {}

    public Jediser init() {
        log.info("Try init Jediser[" + host + ":" + port + "]. ");
        if (jedisPoolConfig == null) {
            jedisPoolConfig = new JedisPoolConfig();
        }
        jedisPool = new JedisPool(jedisPoolConfig, host, port, connectionTimeout, soTimeout, password
                , database, clientName, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
        return this;
    }

    public Jedis getJedis() {
        if (jedisPool == null) {
            throw new JedisException("JedisPool must init first. ");
        }
        return jedisPool.getResource();
    }

    public Jediser setJedis(Jedis jedis) {
        // close will do "returnBrokenResource" or "returnResource"
        jedis.close();
        return this;
    }

    public String ping() {
        Jedis jedis = this.getJedis();
        try {
            return jedis.ping();
        }
        finally {
            jedis.close();
        }
    }

    public String select(int index) {
        Jedis jedis = this.getJedis();
        try {
            return jedis.select(index);
        }
        finally {
            jedis.close();
        }
    }

    public Long dbSize() {
        Jedis jedis = this.getJedis();
        try {
            return jedis.dbSize();
        }
        finally {
            jedis.close();
        }
    }

    public String flushDB() {
        Jedis jedis = this.getJedis();
        try {
            return jedis.flushDB();
        }
        finally {
            jedis.close();
        }
    }

    public String flushAll() {
        Jedis jedis = this.getJedis();
        try {
            return jedis.flushAll();
        }
        finally {
            jedis.close();
        }
    }

    public String set(String key, String value) {
        Jedis jedis = this.getJedis();
        try {
            return jedis.set(key, value);
        }
        finally {
            jedis.close();
        }
    }

    public String set(byte[] key, byte[] value) {
        Jedis jedis = this.getJedis();
        try {
            return jedis.set(key, value);
        }
        finally {
            jedis.close();
        }
    }

    public String setex(String key, int seconds, String value) {
        Jedis jedis = this.getJedis();
        try {
            return jedis.setex(key, seconds, value);
        }
        finally {
            jedis.close();
        }
    }

    public String setex(byte[] key, int seconds, byte[] value) {
        Jedis jedis = this.getJedis();
        try {
            return jedis.setex(key, seconds, value);
        }
        finally {
            jedis.close();
        }
    }

    public Long del(String key) {
        Jedis jedis = this.getJedis();
        Long del = jedis.del(key);
        jedis.close();
        return del;
    }

    public Long del(String... keys) {
        Jedis jedis = this.getJedis();
        Long del = jedis.del(keys);
        jedis.close();
        return del;
    }

    public String get(String key) {
        Jedis jedis = this.getJedis();
        try {
            return jedis.get(key);
        }
        finally {
            jedis.close();
        }
    }

    public Set<String> keys(String pattern) {
        Jedis jedis = this.getJedis();
        try {
            return jedis.keys(pattern);
        }
        finally {
            jedis.close();
        }
    }

    public Boolean exists(String key) {
        Jedis jedis = this.getJedis();
        try {
            return jedis.exists(key);
        }
        finally {
            jedis.close();
        }
    }

    public Boolean exists(byte[] key) {
        Jedis jedis = this.getJedis();
        try {
            return jedis.exists(key);
        }
        finally {
            jedis.close();
        }
    }

    public Long exists(String... keys) {
        Jedis jedis = this.getJedis();
        try {
            return jedis.exists(keys);
        }
        finally {
            jedis.close();
        }
    }

    public Long exists(byte[]... keys) {
        Jedis jedis = this.getJedis();
        try {
            return jedis.exists(keys);
        }
        finally {
            jedis.close();
        }
    }

}
