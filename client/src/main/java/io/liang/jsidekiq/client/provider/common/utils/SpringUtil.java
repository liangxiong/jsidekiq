package io.liang.jsidekiq.client.provider.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;

/**
 * Created by zhangyouliang on 17/4/11.
 */
public class SpringUtil {
    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext applicationContext){
        SpringUtil.applicationContext = applicationContext;
    }


    public static <T> T getBean(Class<T> cls){
        String beanName = StringUtils.uncapitalize(cls.getSimpleName());
        Object obj = applicationContext.getBean(beanName);
        return (T)obj;
    }

    public static Object getBean(String className){
        try {
            return getBean(Class.forName(className));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
