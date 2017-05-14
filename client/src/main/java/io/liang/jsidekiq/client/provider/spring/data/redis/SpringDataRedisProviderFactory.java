package io.liang.jsidekiq.client.provider.spring.data.redis;

import io.liang.jsidekiq.client.provider.Provider;
import org.springframework.data.redis.core.StringRedisTemplate;
import io.liang.jsidekiq.client.config.Config;
import io.liang.jsidekiq.client.provider.common.utils.SpringUtil;
import io.liang.jsidekiq.client.provider.factory.ProviderFactory;

/**
 * 创建spring data 的客户端
 * Created by zhiping on 17/3/28.
 */
public class SpringDataRedisProviderFactory extends ProviderFactory {

    public Provider getProvider(){
        SpringDataRedisProvider client = new SpringDataRedisProvider();

        client.setRedisTemplate(SpringUtil.getBean(StringRedisTemplate.class));
        client.setConfig(Config.getInstance());
//        client.setStringRedisTemplate(SpringUtil.getBean(StringRedisTemplate.class));

        return client;
    }
}
