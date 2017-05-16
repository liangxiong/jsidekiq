package io.liang.jsidekiq.client.provider.factory;

import io.liang.jsidekiq.client.exception.ConfigException;
import io.liang.jsidekiq.client.provider.Provider;
import io.liang.jsidekiq.client.provider.spring.data.redis.SpringDataRedisProviderFactory;

/**
 * Created by zhangyouliang on 17/4/9.
 */
public abstract class ProviderFactory {
    public abstract Provider getProvider();

    public static Provider getProviderByConfig(String name)throws ConfigException {
        ProviderFactory factory = null;
        if("springDataRedis".equals(name)) {
            factory = new SpringDataRedisProviderFactory();
        }

        if(factory != null){
            return factory.getProvider();
        }else{
            throw new ConfigException("please config provider name so such: springDataRedis");
        }
    }

}
