package io.liang.jsidekiq.web.service.impl;

import org.springframework.stereotype.Service;
import io.liang.jsidekiq.client.aop.JSidekiqLabel;
import io.liang.jsidekiq.web.po.User;

/**
 * Created by zhiping on 17/3/28.
 */
@Service()
public class JsidekiqUserService2Impl{

    public Boolean register(User obj) {
        System.out.println("第1 2步：注册用户: "+obj.getName()+" 成功 持久化到数据库");
        sendMail(obj);
        return true;
    }

    @JSidekiqLabel(retry = 3,description = "异步发送注册邮件",queue = "default")
    public Boolean sendMail(User obj) {
        System.out.println("第2 2步：发送邮件：欢迎 "+obj.getName()+" 使用电视淘宝，1000块钱优惠券已加入你的钱包");
        try {
            //模拟发送邮件比较慢，需要调用外部api
            Thread.sleep(3000);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("第2 2步：发送邮件：欢迎 "+obj.getName()+" 完成  模拟非常耗时：3S");
        return true;
    }
}
