package io.liang.jsidekiq.client.provider.common.load.spring.boot;

import org.springframework.stereotype.Component;

/**
 * spring boot 的 jsidekiq加载
 * Created by zhiping on 17/4/9.
 */
@Component
public class JsidekiqConfig {
    private String nameSpace;
    private String defaultQueue;

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public String getDefaultQueue() {
        return defaultQueue;
    }

    public void setDefaultQueue(String defaultQueue) {
        this.defaultQueue = defaultQueue;
    }

}