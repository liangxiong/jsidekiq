package io.liang.jsidekiq.client.pojo.stat;

/**
 * 队列的统计
 * Created by zhangyouliang on 17/4/23.
 */
public class ProcessStat {

    private String hostname;
    private String startedAt;
    private String pid;
    private Integer concurrency;
    private String queues;
    private String identity;

    private Long busy;
    private String beat;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Integer getConcurrency() {
        return concurrency;
    }

    public void setConcurrency(Integer concurrency) {
        this.concurrency = concurrency;
    }

    public String getQueues() {
        return queues;
    }

    public void setQueues(String queues) {
        this.queues = queues;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public Long getBusy() {
        return busy;
    }

    public void setBusy(Long busy) {
        this.busy = busy;
    }

    public String getBeat() {
        return beat;
    }

    public void setBeat(String beat) {
        this.beat = beat;
    }
}
