package io.liang.jsidekiq.client.exception;

/**
 * jsidekiq 配置异常
 * Created by zhangyouliang on 17/4/12.
 */
public class ConfigException extends RuntimeException {
    private static final long serialVersionUID = 7832616742616332703L;

    public ConfigException(String message) {
        super(message);
    }
}
