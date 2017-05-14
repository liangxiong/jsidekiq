package io.liang.jsidekiq.client.provider.redis;

/**
 * 待实现
 * Created by zhiping on 17/3/28.
 */
//public class RedisProvider implements Provider {
//
//    public static String HOST = "127.0.0.1";
//    public static int PORT = 6379;
//
//    public static JedisPool jedisPool = new JedisPool(HOST, PORT);
//
//
//    private static final int DEFAULT_REDIS_PORT = 6379;
//
//    private final static String DEFAULT_ROOT = "dubbo";
////
////    private final ScheduledExecutorService expireExecutor = Executors.newScheduledThreadPool(1, new NamedThreadFactory("DubboRegistryExpireTimer", true));
////
////    private final ScheduledFuture<?> expireFuture;
//
//    private final String root;
//
//    private final Map<String, JedisPool> jedisPools = new ConcurrentHashMap<String, JedisPool>();
//
////    private final ConcurrentMap<String, Notifier> notifiers = new ConcurrentHashMap<String, Notifier>();
//
////    private final int reconnectPeriod;
//
//    private final int expirePeriod;
//
////    private volatile boolean admin = false;
//
//    private boolean replicate;
//
//    public RedisProvider(URL url) {
//
//        if (url.isAnyHost()) {
//            throw new IllegalStateException("registry address == null");
//        }
//        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
//        config.setTestOnBorrow(url.getParameter("test.on.borrow", true));
//        config.setTestOnReturn(url.getParameter("test.on.return", false));
//        config.setTestWhileIdle(url.getParameter("test.while.idle", false));
//        if (url.getParameter("max.idle", 0) > 0)
//            config.setMaxIdle(url.getParameter("max.idle", 0));
//        if (url.getParameter("min.idle", 0) > 0)
//            config.setMinIdle(url.getParameter("min.idle", 0));
//        if (url.getParameter("max.active", 0) > 0)
//            config.setMaxTotal(url.getParameter("max.total", 0));
//        if (url.getParameter("max.wait", url.getParameter("timeout", 0)) > 0)
//            config.setMaxWaitMillis(url.getParameter("max.wait.millis", url.getParameter("timeout", 0)));
//        if (url.getParameter("num.tests.per.eviction.run", 0) > 0)
//            config.setNumTestsPerEvictionRun(url.getParameter("num.tests.per.eviction.run", 0));
//        if (url.getParameter("time.between.eviction.runs.millis", 0) > 0)
//            config.setTimeBetweenEvictionRunsMillis(url.getParameter("time.between.eviction.runs.millis", 0));
//        if (url.getParameter("min.evictable.idle.time.millis", 0) > 0)
//            config.setMinEvictableIdleTimeMillis(url.getParameter("min.evictable.idle.time.millis", 0));
//
//        String cluster = url.getParameter("cluster", "failover");
//        if (! "failover".equals(cluster) && ! "replicate".equals(cluster)) {
//            throw new IllegalArgumentException("Unsupported redis cluster: " + cluster + ". The redis cluster only supported failover or replicate.");
//        }
//        replicate = "replicate".equals(cluster);
//
//        List<String> addresses = new ArrayList<>();
//        addresses.add(url.getAddress());
//        String[] backups = url.getParameter(Cons.BACKUP_KEY, new String[0]);
//        if (backups != null && backups.length > 0) {
//            addresses.addAll(Arrays.asList(backups));
//        }
//        for (String address : addresses) {
//            int i = address.indexOf(':');
//            String host;
//            int port;
//            if (i > 0) {
//                host = address.substring(0, i);
//                port = Integer.parseInt(address.substring(i + 1));
//            } else {
//                host = address;
//                port = DEFAULT_REDIS_PORT;
//            }
//            this.jedisPools.put(address, new JedisPool(config, host, port,
//                    url.getParameter(Cons.TIMEOUT_KEY, Cons.DEFAULT_TIMEOUT)));
//        }
//
////        this.reconnectPeriod = url.getParameter(Cons.REGISTRY_RECONNECT_PERIOD_KEY, Cons.DEFAULT_REGISTRY_RECONNECT_PERIOD);
//        String group = url.getParameter(Cons.GROUP_KEY, DEFAULT_ROOT);
//        if (! group.startsWith(Cons.PATH_SEPARATOR)) {
//            group = Cons.PATH_SEPARATOR + group;
//        }
//        if (! group.endsWith(Cons.PATH_SEPARATOR)) {
//            group = group + Cons.PATH_SEPARATOR;
//        }
//        this.root = group;
//
//        this.expirePeriod = url.getParameter(Cons.SESSION_TIMEOUT_KEY, Cons.DEFAULT_SESSION_TIMEOUT);
////        this.expireFuture = expireExecutor.scheduleWithFixedDelay(new Runnable() {
////            public void run() {
////                try {
////                    deferExpired(); // 延长过期时间
////                } catch (Throwable t) { // 防御性容错
////                    logger.error("Unexpected exception occur at defer expire time, cause: " + t.getMessage(), t);
////                }
////            }
////        }, expirePeriod / 2, expirePeriod / 2, TimeUnit.MILLISECONDS);
//    }
//
//
//
//    //加入队列
//    public boolean push(String queue,String task){
//        Jedis jedis = null;
//        try{
//            jedis = getResource();
//            jedis.lpush(queue, task);
//        }catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }finally {
//            close(jedis);
//        }
//        return true;
//    }
//
//
//    //加入队列
//    public boolean add(String queue,long time,String task){
//        Jedis jedis = null;
//        try{
//            jedis = getResource();
//            jedis.zadd(queue,time,task);
//        }catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }finally {
//            close(jedis);
//        }
//        return true;
//    }
//
//
//    //获取队列中元素
//    public String pop(int timeout,String[] queue){
//        Jedis jedis = null;
//        List<String> list = null;
//        try{
//            jedis = getResource();
//            list = jedis.brpop(timeout,queue);
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            close(jedis);
//        }
//
//        if(list != null && list.size() == 0){
//            return list.get(1);
//        }
//        return null;
//    }
//
//    public Jedis getResource(){
//        return jedisPool.getResource();
//    }
//
//    public void close(Jedis jedis){
//        jedisPool.returnResource(jedis);
//
//    }
//}
