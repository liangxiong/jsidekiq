package io.liang.jsidekiq.client.aop;

/**
 * jsidekiq 配置信息
 * Created by zhiping on 17/3/5.
 */
public class JSidekiqRole {
    //默认 true：表示工作线程
    public static ThreadLocal<String> ROLE = new ThreadLocal<String>();

    /**
     * 申明该线程为 jsidekiq的消费者
     */
    public static void setRoleConsumer(){
        ROLE.set("CONSUMER");
    }


    public static boolean isConsumer(){
        return "CONSUMER".equals(ROLE.get());
    }

    public static String getRole(){
        return ROLE.get() + " "+Thread.currentThread().getName();
    }


}
