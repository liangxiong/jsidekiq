package io.liang.jsidekiq.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.liang.jsidekiq.client.common.Cons;
import io.liang.jsidekiq.client.config.Config;
import io.liang.jsidekiq.client.consumer.ConsumerMain;
import io.liang.jsidekiq.client.consumer.task.Heartbeat;
import io.liang.jsidekiq.client.pojo.Element;
import io.liang.jsidekiq.client.pojo.Page;
import io.liang.jsidekiq.client.pojo.Process;
import io.liang.jsidekiq.client.pojo.stat.ProcessStat;
import io.liang.jsidekiq.client.pojo.stat.QueueStat;
import io.liang.jsidekiq.client.pojo.stat.RedisInfo;
import io.liang.jsidekiq.client.pojo.stat.RunStat;
import io.liang.jsidekiq.client.provider.Provider;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by zhangyouliang on 17/4/9.
 */
public class ClientManager {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    private Provider provider; // 持久化客户端

    private Config config; // 持久化客户端

    private ConsumerMain consumer; // 消费者

    private Heartbeat heartbeat; // 心跳

    private static ClientManager instance;


    private ClientManager(Provider provider,ConsumerMain consumer,Config config){
        this.provider = provider;
        this.config = config;
        this.consumer = consumer;

        this.heartbeat = new Heartbeat(this);

        this.consumer.setClientManager(this);

    }

    //只能实列化一次
    public static ClientManager instance(Provider provider,ConsumerMain consumer,Config config){
        if(instance == null){
            synchronized (ClientManager.class) {
                if (instance == null) {
                    instance = new ClientManager(provider,consumer,config);
                }
            }
        }
        return instance;
    }

    public static ClientManager getInstance(){
        return instance;
    }

    public volatile AtomicBoolean start = new AtomicBoolean(false);
    public  void start(){
        if(start.compareAndSet(false,true)) {
            consumer.start();
            heartbeat.start();
        }
    }

    public  void stop(){
        if(start.compareAndSet(true,false)) {
            consumer.stop();
            heartbeat.stop();
        }
    }

    /****************************************************************************************************
     * 删除逻辑信息
     *
     ****************************************************************************************************/
    /**
     * 根据内容 删除队列的条目
     * @param name
     * @param value
     * @return
     */
    public  Long ListRemByValue(String name,String value){
        return provider.listRem(name,value);
    }


    /**
     * 有序队列的删除
     * @param name
     * @param value
     * @return
     */
    public  Boolean delScheduleByValue(String key){
        boolean flag = false;
        String[] srr = key.split("_");

        String queueName = srr[0];
        Long score = Long.parseLong(srr[1]);
        String jid = srr[2];

        Set<String> set = provider.zRangeByScore(queueName,score,score,0,1000);

        if(set != null && set.size() > 0) {
            Iterator<String> ite = set.iterator();
            while (ite.hasNext()) {
                String str = ite.next();

                JSONObject json = JSON.parseObject(str); //反序列化 优化

                if(jid.equals(json.getString("jid"))){
                    if(provider.zRem(queueName,str) > 0){
                        flag = true;
                        break;
                    }
                }
            }
        }
        return flag;
    }

    /**
     * 删除队列
     * @param name
     * @return
     */
    public boolean delQueue(String name) {
        return provider.delQueueByName(name);
    }

    /**
     * 删除队列
     * @param name
     * @return
     */
    public boolean delSchedule(String name) {
        return provider.delScheduleByName(name);
    }


    //删除进程信息
    public void removeProcess(String identity) {
        provider.removeProcess(identity);
    }


    /****************************************************************************************************
     * 统计信息
     *
     ****************************************************************************************************/
    public RedisInfo getRedisInfo(){
        return provider.redisInfo();
    }

    public RunStat getRunStat(){
        return provider.getRunStat();
    }


    public String get(String key){
        return provider.get(key);
    }

    //增加已经处理数量
    public void incrementProcessed(Long quantity){
         provider.incrementProcessed(quantity);
    }

    //增加成功统计
    public void incrementFailed(Long quantity){
        provider.incrementFailed(quantity);
    }

    public Long scheduleSize(String name){
        return provider.scheduleSize(name);
    }

    //dead队列的数据
    public void paginatorByZset(String queue,Page<JSONObject> page){
        Set<String> set = provider.paginatorByZset(queue,page);

        List<JSONObject> list = new ArrayList<JSONObject>();
        if(set != null && !set.isEmpty()){
            for (String str : set) {
                JSONObject json = JSON.parseObject(str); //反序列化 优化

                list.add(json);
            }
            page.setRows(list);
        }
    }


    //queue分页
    public void paginatorByList(String queue,Page<JSONObject> page){
        List<String> set = provider.paginatorByList(queue,page);

        List<JSONObject> list = new ArrayList<JSONObject>();
        if(set != null && !set.isEmpty()){
            for (String str : set) {
                JSONObject json = JSON.parseObject(str); //反序列化 优化

                list.add(json);
            }
            page.setRows(list);
        }
    }


    //获取进程数量
    public Long getProcessSize() {
        return provider.getProcessSize();
    }

    public List<ProcessStat> getProcessStatList() {
        return provider.getProcessStatList();
    }


    public List<QueueStat> getQueueStatList() {
        return provider.getQueueStatList();
    }





    /****************************************************************************************************
     * 正常业务处理信息
     *
     ****************************************************************************************************/

    /**
     * 放入队列
     * @param queue
     * @param timeout
     * @return
     */
    public boolean push(Element element) {
        Long now = System.currentTimeMillis();

        String scheduleName = null;

        if(element.getFailedAt() != null ){//放入dead
            long score = element.getFailedAt().getTime();

            return  provider.addMorgue(score,decodeElement(element));
        }else  if(element.getAt() != null && (element.getAt().getTime() > now)) { // 定时执行,但是时间已经过期
            long score = element.getAt().getTime();
            element.setNextRunTime(score);

            return  provider.addSchedule(Cons.SCHEDULE_SCHEDULE,score,decodeElement(element));
        }else if(element.getRetriedAt() != null && (element.getRetriedAt().getTime() > now)){//重试
            long score = element.getRetriedAt().getTime();
            element.setNextRunTime(score);

             //重试次数多的直接放入dead
            return  provider.addSchedule(Cons.SCHEDULE_RETRY,score,decodeElement(element));
        }else{//异步任务
            String queue = element.getQueue();
            return provider.pushQueue(queue, decodeElement(element));
        }
    }


    public Element take(){
        for(;;){
            Element element = pop();
            if(element != null){
                return element;
            }
        }
    }


    /**
     * 获取队列里待处理的元素
     * @param queue
     * @param timeout
     * @return
     */
    public Element pop() {
        Set<String> set = config.getConsumerQueue();
        if(set == null || set.isEmpty()){
          set = provider.getQueueName("queues");
        }

        if(set == null || set.size() == 0) {
            return null;
        }

        String[] queues = new String[set.size()];
        Iterator<String> ite = set.iterator();

        int i = 0;
        while (ite.hasNext()) {
            queues[i++] = ite.next();
        }
        String value = provider.pop(10, queues);
        Element element = encodeElement(value);
        return element;
    }


    /**
     * 把延迟任务 立即加入队列中
     * @param processesKey
     * @return
     */
    public Boolean retry(String key){
        Boolean flag = false;
        String[] srr = key.split("_");

        String queueName = srr[0];
        Long score = Long.parseLong(srr[1]);
        String jid = srr[2];

        Set<String> set = provider.zRangeByScore(queueName,score,score,0,1000);

        if(set != null && set.size() > 0) {
            Iterator<String> ite = set.iterator();
            while (ite.hasNext()) {
                String str = ite.next();

                JSONObject json = JSON.parseObject(str); //反序列化 优化

                if(jid.equals(json.getString("jid"))){
                    if(provider.zRem(queueName,str) > 0){

                        pushQueue(str);

                        flag = true;
                    }
                }
            }
        }
        return flag;
    }

    /**
     * 转移到期的任务到执行队列中
     * while job = conn.zrangebyscore(sorted_set, '-inf', now, :limit => [0, 1]).first do

     # Pop item off the queue and add it to the work queue. If the job can't be popped from
     # the queue, it's because another processes already popped it so we can move on to the
     # next one.
     if conn.zrem(sorted_set, job)
     Sidekiq::Client.push(Sidekiq.load_json(job))
     Sidekiq::Logging.logger.debug { "enqueued #{sorted_set}: #{job}" }
     end
     end
     * @param processesKey
     * @return
     */
    public void transferExpiredSchedule(String queueName){
        try {
            for (; ; ) {
                Integer quantity = 0;
                Long max = System.currentTimeMillis();
                Set<String> set = provider.zRangeByScore(queueName,0,max,0,100);

                if(set != null && set.size() > 0) {
                    Iterator<String> ite = set.iterator();
                    while (ite.hasNext()) {
                        String str = ite.next();

                        long count = provider.zRem(queueName, str);
                        if (count > 0) {
                            log.debug("transferExpiredSchedule job：{}", str);

                            pushQueue(str);

                            quantity++;
                        }
                    }
                }

                if (Cons.ZEROI.equals(quantity)) {
                    return;
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }



    /*******************************************
     * 工具
     *
     ******************************************/
    //把任务的描述字符串放入队列中
    private void pushQueue(String str) {
        JSONObject task = JSON.parseObject(str);
        String queue = task.getString("queue");
        queue = StringUtils.isBlank(queue) ? Config.DEFAULT_QUEUE : queue;
        provider.pushQueue(queue, str);
    }


    public String decodeElement(Element element){
        if(StringUtils.isBlank(element.getJid())){
            element.setJid(UUID.randomUUID().toString().replace("-",""));
        }

        Object[] params = element.getParams();
        element.setParams(null);
        for(int i=0;i<params.length;i++){
            if(params[i] != null) {
                params[i] = toJSONStr(params[i]);
            }
        }
        element.setParams(params);


        String task = toJSONStr(element);//序列化 优化
        return task;
    }

    private String toJSONStr(Object obj){
        return JSONObject.toJSONStringWithDateFormat(obj,"yyyy-MM-dd HH:mm:ss");
    }


    private Element encodeElement(String str) {
        if(StringUtils.isBlank(str)){
            return null;
        }
        JSONObject json = JSON.parseObject(str); //反序列化 优化

        Element element = JSON.parseObject(str,Element.class);

        JSONArray paramJsonArray = json.getJSONArray("params");

        if(paramJsonArray != null && paramJsonArray.size() > 0) {
            Object[] params = null;
            params = new Object[paramJsonArray.size()];
            for(int i=0;i<element.getParams().length;i++){
                String source = paramJsonArray.getString(i);
                Class sourceClass = element.getParamTypes()[i];
                if(source != null) {
                    params[i] = JSON.parseObject(source, sourceClass);
                }
            }

            element.setParams(params);
        }
        return element;
    }

    //heartbeat进程信息
    public void heartbeatProcess(String identity) {
        int busy = consumer.busy();

        Process process = provider.getProcess(identity);
        if(process == null){
            process = new Process();
            process.setIdentity(identity);
        }

        provider.heartbeatProcess(identity,busy,process);
    }

    public  void regiestShutDown(){
        if(start.compareAndSet(true,false)) {//已经启动

            consumer.registerStop();
            heartbeat.registerStop();
        }
    }

    class ShutdownHook implements Runnable{
        @Override
        public void run() {
            System.out.println("ShutdownHook execute start...");
            System.out.print("Netty NioEventLoopGroup shutdownGracefully...");

            consumer.registerStop();
            heartbeat.stop();

            System.out.println("ShutdownHook execute end...");
            System.out.println("Sytem shutdown over, the cost time is 10000MS");
        }
    }
}
