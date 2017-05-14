package io.liang.jsidekiq.client.consumer.task;

import io.liang.jsidekiq.client.ClientManager;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.liang.jsidekiq.client.common.utils.ProcessUtils;
import io.liang.jsidekiq.client.common.utils.NetUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhiping on 17/4/22.
 */
public class Heartbeat {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    private String identity;
    private ClientManager manager;
    private ScheduledExecutorService pool;

    public Heartbeat(ClientManager manager){
        this.identity = NetUtils.getHostName() + ":" + ProcessUtils.pid()+":"+ RandomUtils.nextInt(100000,999999);
        this.manager = manager;
    }

    public void start(){
        pool = Executors.newScheduledThreadPool(1);

        Runnable startHeartbeat = new Runnable(){
            public void run(){
                startHeartbeat();
            }
        };
        pool.scheduleWithFixedDelay(startHeartbeat, 10, 5, TimeUnit.SECONDS); //10秒后执行，每隔5秒心跳
    }


    public void stop(){
        stopHeartbeat();
        pool.shutdown();

    }

    public void startHeartbeat(){
        try {
            manager.heartbeatProcess(identity);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }

    public void stopHeartbeat(){
        try {
            log.info("stopHeartbeat");
            manager.removeProcess(identity);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }

    public void registerStop(){
        Thread t = new Thread(new ShutdownHook(), "Jsidekiq Heartbeat ShutdownHook-Thread");
        Runtime.getRuntime().addShutdownHook(t);
    }

    class ShutdownHook implements Runnable{
        @Override
        public void run() {
            try {
                log.info("Jsidekiq heartbeat stop begin...");
                Long st = System.currentTimeMillis();

                stop();

                log.info("Jsidekiq heartbeat stop begin...  {}", (System.currentTimeMillis() - st));
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
        }
    }

}
