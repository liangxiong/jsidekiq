package io.liang.jsidekiq.client.start;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import io.liang.jsidekiq.client.provider.common.utils.SpringUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by zhangyouliang on 17/4/11.
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

        String str = context.getInitParameter("jsidekiq.configUrl");
        str = StringUtils.isBlank(str) ? configUrl : str;

        Start.start(str);

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}

