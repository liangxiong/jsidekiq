package io.liang.jsidekiq.web.service;


/**
 * Created by zhangyouliang on 17/3/28.
 */
public interface DemoService {
    public void sleep();

    public void sleep(String name,int time);

    public void sleep(Long time);

    public Boolean register(String name);

    public Boolean sendAsynMail(String name);

    public Boolean sendScheduleMail(String name);

    public Boolean sendDeadMail(String name)throws Exception;
}
