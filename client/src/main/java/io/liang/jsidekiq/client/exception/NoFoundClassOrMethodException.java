package io.liang.jsidekiq.client.exception;

/**
 * jsidekiq 未发现类或方法的异常
 * Created by zhangyouliang on 17/4/12.
 */
public class NoFoundClassOrMethodException extends RuntimeException {
    private static final long serialVersionUID = 7832616742616332703L;


    public NoFoundClassOrMethodException(String message) {
        super(message);
    }
}
