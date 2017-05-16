package io.liang.jsidekiq.client.provider.spring.data.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.liang.jsidekiq.client.pojo.stat.RedisInfo;
import io.liang.jsidekiq.client.provider.Provider;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import io.liang.jsidekiq.client.pojo.Page;
import io.liang.jsidekiq.client.pojo.Process;
import io.liang.jsidekiq.client.pojo.stat.ProcessStat;
import io.liang.jsidekiq.client.common.Cons;
import io.liang.jsidekiq.client.config.Config;
import io.liang.jsidekiq.client.pojo.stat.QueueStat;
import io.liang.jsidekiq.client.pojo.stat.RunStat;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 待实现
 * Created by zhangyouliang on 17/3/28.
 */
public class SpringDataRedisProvider implements Provider {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    private RedisTemplate<String,String> redisTemplate;
    private Config config;

    @Override
    public void incrementProcessed(Long quantity) {
        String ymd = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        increment("stat:processed",quantity);
        increment("stat:processed"+":"+ymd,quantity);
    }

    @Override
    public void incrementFailed(Long quantity) {
        String ymd = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        increment("stat:failed",quantity);
        increment("stat:failed"+":"+ymd,quantity);
    }


    //加入异步队列
    public Boolean pushQueue(String queueName,String task){
        addQueueName("queues",queueName);

        listLeftPush(queueName,task);
        return true;
    }


    /**
     * 删除队列
     * @param name
     * @return
     */
    public boolean delQueueByName(String name) {
        redisTemplate.delete(makePath(name));
        delQueueName("queues",name);
        return true;
    }

    @Override
    public boolean delScheduleByName(String name) {
        redisTemplate.delete(makePath(name));
        return false;
    }



    //加入延时队列
    public Boolean addSchedule(String queue,long time,String task){
        return zsetAdd(queue,task,time);
    }


    /**
     *         now = Time.now.to_f
     *        Sidekiq.redis do |conn|
     *        conn.multi do
     *                conn.zadd('dead', now, payload)
     *                conn.zremrangebyscore('dead', '-inf', now - DeadSet.timeout)  移除有序集合中给定的分数区间的所有成员
     *                conn.zremrangebyrank('dead', 0, -DeadSet.max_jobs)            移除有序集合中给定的排名区间的所有成员
     *        end
     *                end
     *        dead_max_jobs: 10_000,
     *        dead_timeout_in_seconds: 180 * 24 * 60 * 60 # 6 months
     * @param time
     * @param task
     * @return
     */
    public Boolean addMorgue(final long time,final String task){
        final RedisSerializer<String> serializer = redisTemplate.getStringSerializer();


        final byte[] queue = serializer.serialize(makePath(Cons.SCHEDULE_DEAD));

        return redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection conn)
                    throws DataAccessException {
                conn.multi();

                conn.zAdd(queue,time,serializer.serialize(task));

                Long now = System.currentTimeMillis();
                Long count = 0L;
                count = conn.zRemRangeByScore(queue,0,now - config.getDeadTimeout());

                count = conn.zRemRange(queue,0, -config.getDeadMaxJob());

                conn.exec();

                return true;
            }
        });
    }


    //增加队列名称
    private Boolean addQueueName(String queue,String values) {
        setAdd(queue,values);
        return true;
    }

    //增加队列名称
    private Boolean delQueueName(String queue,String values) {
        sRem(queue,values);
        return true;
    }


    //获取所有的队列信息
    @Override
    public Set<String> getQueueName(String queue) {
        return sMembers(queue);
    }


    //获取队列中元素
    public String pop(final int timeout, final String... queue){
        return redisTemplate.execute(new RedisCallback<String>() {
            public String doInRedis(RedisConnection connection)
                    throws DataAccessException {

                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[][] keys = new byte[queue.length][];

                for(int i=0;i<queue.length;i++) {
                    keys[i] = serializer.serialize(makePath(queue[i]));
                }
                List<byte[]> list =  connection.bLPop(timeout,keys);

                if(list != null && !list.isEmpty()) {
                    List<String> values = new ArrayList<String>();

                    for (int i = 0; i < list.size(); i++) {
                        String value = serializer.deserialize(list.get(i));
                        values.add(value);
                    }
                    return values.get(1);//0
                }
                return null;
            }
        });
    }

    /**
     * 删除进程
     * @param processes
     * @param identity
     * @param workers
     */
    @Override
    public void removeProcess(final String identity) {
        final String processesKey = makePath("processes");
        final String workersKey = makePath(identity,"workers");

        redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {

                connection.openPipeline();

                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();

                connection.sRem(serializer.serialize(makePath(processesKey)),serializer.serialize(identity));
                connection.del(serializer.serialize(makePath(workersKey)));

                connection.closePipeline();

                return true;
            }
        });
    }


    @Override
    public Process getProcess(String identity) {
        Process process = null;
        String str = (String)hGet(identity, "info");
        if(StringUtils.isNotBlank(str)){
            process = JSON.parseObject(str,Process.class);
        }
        return process;
    }


    /**
     * sidekiq 源码
     * conn.sadd('processes', key)
       conn.hmset(key, 'info', json, 'busy', @busy.size, 'beat', Time.now.to_f)
       conn.expire(key, 60)
        conn.rpop("#{key}-signals")
     * @param processesKey
     * @param identity
     * @param busy
     * @param process
     * @param workerskey
     * @param signalsKey
     */
    @Override
    public void heartbeatProcess(final String identity,final int busy, final Process process) {
        redisTemplate.execute(new RedisCallback<Boolean>() {
            public Boolean doInRedis(RedisConnection connection)
                    throws DataAccessException {
                String workersKey = makePath(identity,"workers");
                String identityKey = makePath(identity);

                connection.openPipeline();

                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();

                //进程列表信息
                connection.sAdd(serializer.serialize(makePath("processes")),serializer.serialize(identity));

                //本进程详情
                Map<byte[],byte[]> map = new HashMap<byte[],byte[]>();
                map.put(serializer.serialize("info"),serializer.serialize(JSONObject.toJSONStringWithDateFormat(process,"yyyy-MM-dd HH:mm:ss")));
                map.put(serializer.serialize("busy"),serializer.serialize(String.valueOf(busy)));
                map.put(serializer.serialize("beat"),serializer.serialize(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));

                connection.hMSet(serializer.serialize(identityKey),map);
                connection.expire(serializer.serialize(identityKey),60);

                //本进程的信号量 quiet stop dump 分别是 USR1 TERM TTIN
                connection.rPop(serializer.serialize(makePath(identity,"-","signals")));

                connection.closePipeline();

                return true;
            }
        });
    }

    @Override
    public Long getProcessSize() {
        return sCard("processes");
    }



    /**********************************************************
     * dashboard
     *
     * ********************************************************/
    /**
     *
     //        //已处理
     //        private String processedSize;
     //        //已失败
     //        private String failedSize;
     //        //执行中
     //        private String processesSize;
     //        //已进行队列
     //        private String enqueuedSize;
     //        //重试
     //        private String retrySize;
     //        //已计划
     //        private String scheduledSize;
     //        //已死亡
     //        private String deadSize;
     * @return
     */
    public RunStat getRunStat(){
        RunStat stat = new RunStat();

        String str = get("stat:processed");
        stat.setProcessedSize(StringUtils.isNotBlank(str) ? str : "0");

        str = get("stat:failed");
        stat.setFailedSize(StringUtils.isNotBlank(str) ? str : "0");


        Long busySum = 0L;
        Set<String> set = sMembers("processes");
        for (String identity : set) {
            Process process = getProcess(identity);

            String busy = (String)hGet(identity,"busy");
            if(StringUtils.isNotBlank(busy)){
                busySum += Long.parseLong(busy);
            }
        }

        stat.setProcessesSize(busySum);

        Long enqueuedSize = 0L;
        //队列信息
        set = sMembers("queues");
        if(set != null && !set.isEmpty()){
            for (String queue : set) {
                enqueuedSize += listLen(queue);
            }
        }

        stat.setEnqueuedSize(enqueuedSize);

        stat.setRetrySize(zCard(Cons.SCHEDULE_RETRY));

        stat.setDeadSize(zCard(Cons.SCHEDULE_DEAD));

        stat.setScheduledSize(zCard("schedule"));

        return stat;
    }

    //队列统计
    public List<QueueStat> getQueueStatList(){
        List<QueueStat> list = new ArrayList<QueueStat>();

        Set<String> set = sMembers("queues");
        if(set != null && !set.isEmpty()){
            for (String queue : set) {
                Long quantity = listLen(queue);

                QueueStat stat = new QueueStat();
                stat.setQuantity(quantity);
                stat.setQueue(queue);

                list.add(stat);
            }
        }

        return list;
    }


    //进程统计
    public List<ProcessStat> getProcessStatList(){
        List<ProcessStat> list = new ArrayList<ProcessStat>();

        Set<String> set = sMembers("processes");
        for (String identity : set) {
            ProcessStat processStat = new ProcessStat();
            Process process = getProcess(identity);

            if(process != null){
                processStat.setIdentity(identity);
                processStat.setHostname(process.getHostname());
                processStat.setStartedAt(process.getStartedAt());
                processStat.setPid(process.getPid());
                processStat.setConcurrency(process.getConcurrency());
                processStat.setQueues(process.getQueues());

                String busy = (String)hGet(identity,"busy");
                if(StringUtils.isNotBlank(busy)){
                    processStat.setBusy(Long.parseLong(busy));
                }

                processStat.setBeat((String)hGet(identity,"beat"));

                list.add(processStat);
            }else{
//                sRem("processes",identity);
            }
        }
        return list;
    }



    //显示统计信息
    public RedisInfo redisInfo() {
        Properties map =  redisTemplate.execute(new RedisCallback<Properties>() {
            public Properties doInRedis(RedisConnection connection)
                    throws DataAccessException {
            return connection.info();
            }
        });

//        {"redis_version"=>"2.8.13 redis 版本", "uptime_in_days"=>"0 上线天数", "connected_clients"=>"2 连接数", "used_memory_human"=>"996.58K  使用内存数", "used_memory_peak_human"=>"996.58K 内存占用峰值"}

        RedisInfo info = new RedisInfo();
        info.setRedisVersion(map.getProperty("redis_version"));
        info.setUptimeInDays(map.getProperty("uptime_in_days"));
        info.setConnectedClients(map.getProperty("connected_clients"));
        info.setUsedMemoryHuman(map.getProperty("used_memory_human"));
        info.setUsedMemoryPeakHuman(map.getProperty("used_memory_peak_human"));


        return info;
    }

    /**
     * @param time
     * @param task
     * @return
     */
    public Set<String> paginatorByZset(final String queueName,Page<JSONObject> page){
        Long count  = zCard(queueName);
        page.setTotalRow(count);

        final int starting = page.getPageNum() * page.getNumPerPage();
        final int ending = starting + page.getNumPerPage() - 1;


//        redisTemplate.opsForZSet().reverseRange(queueName, starting, ending);
        return redisTemplate.execute(new RedisCallback<Set<String>>() {
            public Set<String> doInRedis(RedisConnection connection)
                    throws DataAccessException {
                Set<String> data = new HashSet<String>();

                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();


                Set<byte[]>  set = connection.zRevRange(serializer.serialize(makePath(queueName)),starting,ending);

                if(set != null && !set.isEmpty()){
                    Iterator<byte[]> ite = set.iterator();
                    while(ite.hasNext()){
                        data.add(serializer.deserialize(ite.next()));
                    }
                }

                return data;
            }
        });
    }

    /**
     * @param time
     * @param task
     * @return
     */
    public List<String> paginatorByList(String queueName,Page<JSONObject> page){
        Long count  = listLen(queueName);
        page.setTotalRow(count);

        queueName = makePath(queueName);

        int starting = page.getPageNum() * page.getNumPerPage();
        int ending = starting + page.getNumPerPage() - 1;

        return redisTemplate.opsForList().range(queueName, starting, ending);
    }


    /**********************************************************
     * list 队列 的操作
     *
     * ********************************************************/
    /* @param key
     * @param values
     */
    private Long listLeftPush(String key,String value){
        key = makePath(key);
        return redisTemplate.opsForList().leftPush(key,value);
    }

    private Long listLen(String key){
        key = makePath(key);
        return redisTemplate.opsForList().size(key);
    }

    public Long listRem(String key,String value){
        key = makePath(key);
        return redisTemplate.opsForList().remove(key,1,value);
    }


    /**********************************************************
     * sorted set 有序队列 的操作
     *
     * ********************************************************/
    /* @param key
     * @param values
     */
    private Boolean zsetAdd(String key,String value,long time){
        key = makePath(key);
        return redisTemplate.opsForZSet().add(key,value,time);
    }

    public Set<String> zRangeByScore(String key, double min, double max, long offset, long count) {
        key = makePath(key);
        if (offset > -1) {
            return redisTemplate.opsForZSet().rangeByScore(key, min, max, offset, count);
        }else{
            return redisTemplate.opsForZSet().rangeByScore(key, min, max);
        }
    }

    public Long zRem(String key,String val){
        key = makePath(key);
        return redisTemplate.opsForZSet().remove(key,val);
    }


    //成员数量
    private Long zCard(String key){
        key = makePath(key);
        return redisTemplate.opsForZSet().zCard(key);
    }

    //延迟任务的长度
    public Long scheduleSize(String name){
        return zCard(name);
    }


    private Set<String> zReverseRange(String key,int starting,int ending){
        key = makePath(key);
        return redisTemplate.opsForZSet().reverseRange(key, starting, ending);
    }


    /**********************************************************
     * set 的操作
     *
     * ********************************************************/
    /* @param key
     * @param values
     */
    private Long setAdd(String key,String...values){
        key = makePath(key);
        return redisTemplate.opsForSet().add(key,values);
    }

    /* @param key
     * @param values
     */
    private Long sCard(String key){
        key = makePath(key);
        return redisTemplate.opsForSet().size(key);
    }

    /* @param key
    * @param values
    */
    private Set<String> sMembers(String key){
        key = makePath(key);
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 删除
     * @param key
     * @param values
     * @return
     */
    private Long sRem(String key,String... values){
        key = makePath(key);
        return redisTemplate.opsForSet().remove(key,values);
    }

    /**********************************************************
     * hash 的操作
     *
     * ********************************************************/
    private Object hGet(String key,String field){
        key = makePath(key);
        return redisTemplate.opsForHash().get(key,field);
    }

    /* @param key
     * @param values
     */
    public String get(String key){
        key = makePath(key);
        return redisTemplate.opsForValue().get(key);
    }

    /* @param key
     * @param values
     */
    private Long increment(String key,Long quantity){
        key = makePath(key);

        redisTemplate.expire(key,Cons.STAT_TTL, TimeUnit.MILLISECONDS);
        return redisTemplate.opsForValue().increment(key,quantity);
    }



    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setConfig(Config config) {
        this.config = config;
    }


    /**
     * 参考 zookeper curator
     * Validate the provided znode path string
     * @param path znode path string
     * @return The given path if it was valid, for fluent chaining
     * @throws IllegalArgumentException if the path is invalid
     */
    protected  String makePath(String... child) throws IllegalArgumentException {
        StringBuilder path = new StringBuilder();

        path.append(config.getNameSpace());
        for(int i=0;i<child.length;i++){
            path.append(":").append(child[i]);
        }
        return path.toString();
    }

}