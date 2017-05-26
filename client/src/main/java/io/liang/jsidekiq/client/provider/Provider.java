package io.liang.jsidekiq.client.provider;


import com.alibaba.fastjson.JSONObject;
import io.liang.jsidekiq.client.pojo.Element;
import io.liang.jsidekiq.client.pojo.stat.RedisInfo;
import io.liang.jsidekiq.client.pojo.Page;
import io.liang.jsidekiq.client.pojo.Process;
import io.liang.jsidekiq.client.pojo.stat.ProcessStat;
import io.liang.jsidekiq.client.pojo.stat.QueueStat;
import io.liang.jsidekiq.client.pojo.stat.RunStat;

import java.util.List;
import java.util.Set;

/**
 * Created by zhangyouliang on 17/3/26.
 */
public interface Provider {
    //增加任务
    Boolean pushQueue(String queue,String task);

    //增加dead
    Boolean addMorgue(long time,String task);

    //增加定时任务
    Boolean addSchedule(String queue,long time,String task);

    //获取队列信息
    Set<String> getQueueName(String queue);


    String pop(int timeout,String... queue);

    void removeProcess(String identity);

    Process getProcess(String identity) ;

    void heartbeatProcess(String identity,int busy, Process process);

    //有序队列分页
    Set<String> paginatorByZset(String queueName,Page<JSONObject> page);

    //队列分页
    List<String> paginatorByList(String queueName,Page<JSONObject> page);

    //延迟任务的长度
    Long scheduleSize(String name);

    //增加统计数量
    void incrementProcessed(Long quantity);

    //减少统计数量
    void incrementFailed(Long quantity);

    //删除队列
    boolean delQueueByName(String name);

    //删除排期
    boolean delScheduleByName(String name);

    //根据值删除队列
    Long listRem(String name,String value);

    //根据分值返回有序队列内容
    Set<String> zRangeByScore(String name, double min, double max, long offset, long count);

    //根据内容删除有序队列
    Long zRem(String key,String val);

    String get(String key);

    //删除已经完成的工作任务
    void removWorkers(String identity, String threadId);

    //增加正在工作的任务信息
    void addWorkers(String identity,String threadId, String element);

    //获取所有正在工作的任务信息
    public List<Object> getAllWorkers(String identity);


    /*************************************************************************************
     * 统计信息 逻辑
     * @return
     ***********************************************************************************/
    //显示redis统计信息
    RedisInfo redisInfo();

    //运行的统计信息
    RunStat getRunStat();

    //获取进程数量
    Long getProcessSize();

    //队列统计
    List<QueueStat> getQueueStatList();

    //进程统计
    List<ProcessStat> getProcessStatList();

}
