package io.liang.jsidekiq.client.consumer.thread;

import com.alibaba.fastjson.JSON;
import io.liang.jsidekiq.client.exception.NoFoundClassOrMethodException;
import io.liang.jsidekiq.client.provider.common.utils.SpringUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by zhiping on 17/4/16.
 */
public class ConsumerThread extends Template implements Runnable {

    public void excute()throws NoFoundClassOrMethodException,NoSuchMethodException,InvocationTargetException,IllegalAccessException, IllegalArgumentException{
        String elementStr = JSON.toJSONStringWithDateFormat(element, "yyyy-MM-dd HH:mm:ss");


        Object obj = SpringUtil.getBean(element.getClassName());
        if (obj != null) {
            Method method = obj.getClass().getDeclaredMethod(element.getMethodName(), element.getParamTypes());

            if(method != null) {
                method.invoke(obj, element.getParams());
            }else{
                throw new NoFoundClassOrMethodException("class or method not found exception "+ elementStr);
            }
        }else{
            throw new NoFoundClassOrMethodException("class or method not found exception "+ elementStr);
        }

    }




    public void close(){
        threadPool.returnObject(this);
    }
}
