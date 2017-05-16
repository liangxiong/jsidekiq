package io.liang.jsidekiq.client.pojo.stat;

import java.util.ArrayList;
import java.util.List;

/**
 * pipe1_res = Sidekiq.redis do |conn|
     conn.pipelined do
     conn.get('stat:processed'.freeze)
     conn.get('stat:failed'.freeze)
     conn.zcard('schedule'.freeze)#执行中
     conn.zcard('retry'.freeze)#重试
     conn.zcard('dead'.freeze)
     conn.scard('processes'.freeze)
     conn.lrange('queue:default'.freeze, -1, -1)
     conn.smembers('processes'.freeze) #7 set 正在处理中
     conn.smembers('queues'.freeze)    #8 set 队列的名字集合
     end
     end

     pipe2_res = Sidekiq.redis do |conn|
     conn.pipelined do
     pipe1_res[7].each {|key| conn.hget(key, 'busy'.freeze) }
     pipe1_res[8].each {|queue| conn.llen("queue:#{queue}") }
     end
     end
     s = pipe1_res[7].size
     workers_size = pipe2_res[0...s].map(&:to_i).inject(0, &:+)
     enqueued     = pipe2_res[s..-1].map(&:to_i).inject(0, &:+)

     default_queue_latency = if (entry = pipe1_res[6].first)
     Time.now.to_f - Sidekiq.load_json(entry)['enqueued_at'.freeze]
     else
     0
     end
     @stats = {
     processed:             pipe1_res[0].to_i, #已处理
     failed:                pipe1_res[1].to_i, #已失败
     scheduled_size:        pipe1_res[2],      #已计划
     retry_size:            pipe1_res[3],      #重试
     dead_size:             pipe1_res[4],      #已停滞
     processes_size:        pipe1_res[5],      #执行中

     default_queue_latency: default_queue_latency,
     workers_size:          workers_size,
     enqueued:              enqueued           #已进入队列
     }
 * Created by zhangyouliang on 17/4/23.
 */
public class RunStat {

    //已处理
    private String processedSize;
    //已失败
    private String failedSize;
    //执行中
    private Long processesSize;
    //已进行队列
    private Long enqueuedSize;
    //重试
    private Long retrySize;
    //已计划
    private Long scheduledSize;
    //已死亡
    private Long deadSize;


    public String getProcessedSize() {
        return processedSize;
    }

    public void setProcessedSize(String processedSize) {
        this.processedSize = processedSize;
    }

    public String getFailedSize() {
        return failedSize;
    }

    public void setFailedSize(String failedSize) {
        this.failedSize = failedSize;
    }

    public Long getProcessesSize() {
        return processesSize;
    }

    public void setProcessesSize(Long processesSize) {
        this.processesSize = processesSize;
    }

    public Long getEnqueuedSize() {
        return enqueuedSize;
    }

    public void setEnqueuedSize(Long enqueuedSize) {
        this.enqueuedSize = enqueuedSize;
    }

    public Long getRetrySize() {
        return retrySize;
    }

    public void setRetrySize(Long retrySize) {
        this.retrySize = retrySize;
    }

    public Long getScheduledSize() {
        return scheduledSize;
    }

    public void setScheduledSize(Long scheduledSize) {
        this.scheduledSize = scheduledSize;
    }

    public Long getDeadSize() {
        return deadSize;
    }

    public void setDeadSize(Long deadSize) {
        this.deadSize = deadSize;
    }
}
