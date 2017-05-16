package io.liang.jsidekiq.client.start;

import io.liang.jsidekiq.client.ClientManager;
import io.liang.jsidekiq.client.config.Config;
import io.liang.jsidekiq.client.consumer.ConsumerMain;
import io.liang.jsidekiq.client.provider.Provider;
import io.liang.jsidekiq.client.provider.factory.ProviderFactory;

/**
 * Created by zhangyouliang on 17/5/1.
 */
public class Start {

    public static void start(String configUrl){
        //加载配置信息
        Config config = Config.getInstance();
        config.parse(configUrl);

        //配置provider
        Provider provider = ProviderFactory.getProviderByConfig(config.getProvider());

        //配置counsumer
        ConsumerMain consumer = new ConsumerMain(config);
        ClientManager manager = ClientManager.instance(provider,consumer,config);
        if(!config.isAdmin()) {
            manager.start();
            manager.regiestShutDown();
        }
    }
}
