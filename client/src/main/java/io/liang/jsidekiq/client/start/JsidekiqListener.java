package io.liang.jsidekiq.client.start;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import io.liang.jsidekiq.client.provider.common.utils.SpringUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by zhiping on 17/4/11.
 */
@Component
public class JsidekiqListener implements ServletContextListener {

    //http://jsidekiq?provider=springDataRedis&minIdle=1&maxTotal=2&blockWhenExhausted=true&maxWaitMillis=10000
    @Value("${jsidekiq.configUrl}")
    private String configUrl;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext context = event.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
        SpringUtil.setApplicationContext(applicationContext);

//        String[] names = applicationContext.getBeanDefinitionNames();
//
//        for(int i=0;i<names.length;i++) {
//            System.out.println(names[i]);
//        }

        Start.start(configUrl);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}

