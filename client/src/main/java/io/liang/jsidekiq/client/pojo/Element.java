package io.liang.jsidekiq.client.pojo;

import org.apache.commons.lang3.StringUtils;
import io.liang.jsidekiq.client.config.Config;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by zhangyouliang on 17/3/26.
 */
public class Element {
    private static final long serialVersionUID = 7832616742616332703L;

    /**
     * 接口
     */
    private String interfaceClassName;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 实现类
     */
    private String className;

    private Class[] paramTypes;
    private Object[] params;
    private String jid;

    /**
     * 创建时间
     */
    private Date createdAt;

    private String queue;
    //最大重试次数
    private Integer maxRetry;
    private  String description;
    private Date at;

    /**
     * 重试时间
     */
    private Date retriedAt;
    /**
     * 重试次数
     */
    private Integer retry;

    /**
     * 失败时间
     */
    private Date failedAt;

    private String errorMessage;


    /**
     * 下一次运行的时间
     */
    private Long nextRunTime;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getNextRunTime() {
        return nextRunTime;
    }

    public void setNextRunTime(Long nextRunTime) {
        this.nextRunTime = nextRunTime;
    }

    public Date getRetriedAt() {
        return retriedAt;
    }

    public void setRetriedAt(Date retriedAt) {
        this.retriedAt = retriedAt;
    }


    public Integer getMaxRetry() {
        return maxRetry;
    }

    public void setMaxRetry(Integer maxRetry) {
        this.maxRetry = maxRetry;
    }

    public Date getFailedAt() {
        return failedAt;
    }

    public void setFailedAt(Date failedAt) {
        this.failedAt = failedAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getAt() {
        return at;
    }

    public void setAt(Date at) {
        this.at = at;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getInterfaceClassName() {
        return interfaceClassName;
    }

    public void setInterfaceClassName(String interfaceClassName) {
        this.interfaceClassName = interfaceClassName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Class[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Class[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getQueue() {
        this.queue = StringUtils.isBlank(queue) ? Config.DEFAULT_QUEUE : queue;
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public Integer getRetry() {
        return retry;
    }

    public void setRetry(Integer retry) {
        this.retry = retry;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }




    @Override
    public String toString() {
        return "Element{" +
                "interfaceClassName='" + interfaceClassName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", className='" + className + '\'' +
                ", paramTypes=" + Arrays.toString(paramTypes) +
                ", params=" + Arrays.toString(params) +
                ", jid='" + jid + '\'' +
                ", createdAt=" + createdAt +
                ", queue='" + queue + '\'' +
                ", maxRetry=" + maxRetry +
                ", description='" + description + '\'' +
                ", at=" + at +
                ", retriedAt=" + retriedAt +
                ", retry=" + retry +
                ", failedAt=" + failedAt +
                ", errorMessage=" + errorMessage +
                '}';
    }
}
