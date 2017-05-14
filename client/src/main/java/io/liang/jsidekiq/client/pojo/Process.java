package io.liang.jsidekiq.client.pojo;

import io.liang.jsidekiq.client.common.utils.NetUtils;
import io.liang.jsidekiq.client.common.utils.ProcessUtils;
import io.liang.jsidekiq.client.config.Config;
import org.apache.commons.lang3.StringUtils;
import io.liang.jsidekiq.client.common.utils.DateUtils;

import java.util.Date;

/**
 * Created by zhiping on 17/3/26.
 */
public class Process {
    private String hostname;
    private String startedAt;
    private String pid;
    private String tag;
    private Integer concurrency;
    private String queues;
    private String labels;
    private String identity;

    public Process(){
        this.hostname = NetUtils.getHostName();
        this.pid = ProcessUtils.pid();
        this.concurrency = Config.getInstance().getConcurrency();
        this.queues = StringUtils.join(Config.getInstance().getConsumerQueue(),",");
        this.startedAt = DateUtils.getDateTime(new Date());
    }

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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}
