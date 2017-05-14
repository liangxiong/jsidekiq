package io.liang.jsidekiq.web.service.impl;


import io.liang.jsidekiq.web.po.User;
import io.liang.jsidekiq.web.service.JsidekiqUserService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import io.liang.jsidekiq.client.aop.JSidekiqLabel;
import io.liang.jsidekiq.client.common.utils.DateUtils;

import java.util.Date;

/**
 * Created by zhiping on 17/3/28.
 */
@Service()
public class JsidekiqUserServiceImpl implements JsidekiqUserService {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public Boolean register(User obj) {
        log.debug("第1步：注册用户: "+obj.getName()+" 成功 持久化到数据库");
        sendMail(obj);
        return true;
    }

    @JSidekiqLabel(retry = 3,description = "异步发送注册邮件",queue = "userSendMailQueue")
    public Boolean sendMail(User obj) {
        log.debug("sendMail start");
        try {
            Thread.sleep(3000);
        }catch (Exception e){
            e.printStackTrace();
        }
        log.debug("发送 sendMail"+ DateUtils.getDateTime(new Date()));
        return true;
    }

    @JSidekiqLabel(retry = 3,description = "异步发送注册邮件,延迟1分钟",queue = "userSendMailSchedule",at = 600000)
    public Boolean sendScheduleMail(User obj) {
        log.debug("sendScheduleMail start");
        try {
            Thread.sleep(3000);
        }catch (Exception e){
            e.printStackTrace();
        }
        log.debug("发送 sendScheduleMail"+ DateUtils.getDateTime(new Date()));
        return true;
    }

    @JSidekiqLabel(description = "肯定执行失败",retry = 3,at = 6000)
    public Boolean sendDeadMail(User obj)throws Exception {
        log.debug("sendDeadMail start");

        OkHttpClient client = new OkHttpClient();
        String url = "http://zyl.com:3308";

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        String str =  response.body().string();

        log.debug("发送 sendDeadMail: {}"+ DateUtils.getDateTime(new Date()),str);
        return true;
    }
}
