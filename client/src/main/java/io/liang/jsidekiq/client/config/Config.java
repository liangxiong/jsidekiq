package io.liang.jsidekiq.client.config;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import io.liang.jsidekiq.client.common.URL;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by zhangyouliang on 17/4/13.
 */
public class Config {
    private  String DEFAULT_NAME_SPACE = "JSIDEKIQ::QUEUE";//default JSIDEKIQ::QUEUE
    public static final String NAMESPACE_SEPARATOR = ":";
    public static final String DEFAULT_QUEUE = "DEFAULT";

    private URL url;
    private boolean admin;
    private String nameSpace;
    private Set<String> consumerQueue;
    private Integer concurrency;
    private String provider;
    //重试次数
    private Integer maxRetry;

    //dead time out
    private Long deadTimeout;
    //存放最大job数
    private Integer deadMaxJob;

    //延迟任务轮询的平均时间 单位：毫秒
    private Long scheduleIntervalTime;


    //管理员的账户
    private String adminName;

    //管理员的密码
    private String adminPassword;


    private static Config instance = new Config();

    private Config(){
    }

    public static Config getInstance(){
        return instance;
    }

    public void parse(String urlStr){
        this.url = URL.getURL(urlStr);
        this.nameSpace = url.getParameter("nameSpace",DEFAULT_NAME_SPACE);
        String str = url.getParameter("consumerQueue");
        if(StringUtils.isNotBlank(str)) {
            Set<String> set = new HashSet<String>();
            CollectionUtils.addAll(set,str.split(","));
            this.consumerQueue = set;
        }
        this.maxRetry = url.getParameter("maxRetry", 3);
        this.concurrency = url.getParameter("maxTotal", 10);
        this.provider = url.getParameter("provider");
        this.scheduleIntervalTime = url.getParameter("scheduleIntervalTime",15000L);

        this.deadTimeout = url.getParameter("deadTimeout",180 * 24 * 60 * 60 * 1000L);
        this.deadMaxJob = url.getParameter("deadMaxJob",10000)+1;

        this.admin = url.getParameter("admin",false);

        this.adminName = url.getParameter("adminName");
        this.adminPassword = url.getParameter("adminPassword");
    }

    public URL getUrl() {
        return url;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public Set<String> getConsumerQueue() {
        return consumerQueue;
    }

    public Integer getConcurrency() {
        return concurrency;
    }

    public String getProvider() {
        return provider;
    }

    public Long getScheduleIntervalTime() {
        return scheduleIntervalTime;
    }

    public Integer getMaxRetry() {
        return maxRetry;
    }

    public Long getDeadTimeout() {
        return deadTimeout;
    }

    public Integer getDeadMaxJob() {
        return deadMaxJob;
    }

    public boolean isAdmin() {
        return admin;
    }

    public String getAdminName() {
        return adminName;
    }

    public String getAdminPassword() {
        return adminPassword;
    }
}
