package io.liang.jsidekiq.web.service.impl;


import io.liang.jsidekiq.web.service.DemoService;
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
 * Created by zhangyouliang on 17/3/28.
 */
@Service
public class DemoServiceImpl implements DemoService {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @JSidekiqLabel(retry = 3,description = "sleep",queue = "demo")
    public void sleep(Long time){
        Long start = System.currentTimeMillis();
        log.debug("sleep start sleep time:{} ",time);

        try {
            Thread.sleep(time);
        }catch (Exception e){
            e.printStackTrace();
        }
        log.debug("sleep end time:{} ",(System.currentTimeMillis() - start));
    }

    @Override
    public Boolean register(String name) {
        log.debug("第1步：注册用户: "+name+" 成功 持久化到数据库");
        sendAsynMail(name);
        return true;
    }

    @JSidekiqLabel(retry = 3,description = "异步发送注册邮件",queue = "demo")
    public Boolean sendAsynMail(String name) {
        log.debug("sendMail start");
        try {
            Thread.sleep(3000);
        }catch (Exception e){
            e.printStackTrace();
        }
        log.debug("发送 sendMail 完成 "+ DateUtils.getDateTime(new Date()));
        return true;
    }

    @JSidekiqLabel(retry = 3,description = "延迟1分钟发送邮件",queue = "demo",at = 60000)
    public Boolean sendScheduleMail(String name) {
        log.debug("sendScheduleMail start");
        try {
            Thread.sleep(3000);
        }catch (Exception e){
            e.printStackTrace();
        }
        log.debug("发送 sendScheduleMail 完成"+ DateUtils.getDateTime(new Date()));
        return true;
    }

    @JSidekiqLabel(description = "肯定执行失败,死亡任务",queue = "demo")
    public Boolean sendDeadMail(String name)throws Exception {
        log.debug("sendDeadMail start");

        OkHttpClient client = new OkHttpClient();
        String url = "http://zyl.com:3308";

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        String str =  response.body().string();

        log.debug("发送 sendDeadMail 完成: {}"+ DateUtils.getDateTime(new Date()),str);
        return true;
    }
}
