package io.liang.jsidekiq.web.service;


import io.liang.jsidekiq.web.po.User;

/**
 * Created by zhiping on 17/3/28.
 */
public interface JsidekiqUserService {
    public Boolean register(User obj);

    public Boolean sendMail(User obj);

    public Boolean sendScheduleMail(User obj);


    public Boolean sendDeadMail(User obj)throws Exception;
}
