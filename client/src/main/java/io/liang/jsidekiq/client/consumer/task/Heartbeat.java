package io.liang.jsidekiq.client.consumer.task;

import io.liang.jsidekiq.client.ClientManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangyouliang on 17/4/22.
 */
public class Heartbeat {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private ClientManager manager;
    private ScheduledExecutorService pool;

    public Heartbeat(ClientManager manager){
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
            manager.heartbeatProcess();
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }

    public void stopHeartbeat(){
        try {
            log.info("stopHeartbeat");
            manager.removeProcess();
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
