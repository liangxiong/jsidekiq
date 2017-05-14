package io.liang.jsidekiq.client.aop;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * jsidekiq 标签
 * Created by 张有良 on 17/3/5.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface JSidekiqLabel {

    /**
     * 业务描述
     */
    String description()  default "";

    /**
     * 队列
     */
    String queue()  default "";

    //延迟多少秒后执行 0 表示立即执行
    long at() default 0;

    int retry() default -1;
}
