package io.liang.jsidekiq.client.consumer;

import io.liang.jsidekiq.client.ClientManager;
import io.liang.jsidekiq.client.config.Config;
import io.liang.jsidekiq.client.consumer.thread.ConsumerThread;
import io.liang.jsidekiq.client.pojo.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.liang.jsidekiq.client.consumer.thread.ConsumerThreadPool;

import java.util.concurrent.*;

/**
 * Created by zhangyouliang on 17/4/16.
 */
public class ConsumerMain {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    private ClientManager clientManager;
    private ConsumerThreadPool objectPool = new ConsumerThreadPool();
    private ExecutorService fixedThreadPool;

    private ScheduleMain scheduleMain;


    public ConsumerMain(Config config){
        objectPool.build(config.getUrl());
        this.fixedThreadPool = Executors.newFixedThreadPool(config.getConcurrency());
        this.scheduleMain = new ScheduleMain(config);
    }

    public void start(){
        new Thread(){
            public void run(){
                work();
            }
        }.start();

        this.scheduleMain.start();
    }

    //工作线程
    public void work(){
        ClientManager clientManager = ClientManager.getInstance();

        for(;;){
            try {
//                Element element = clientManager.take();

                ConsumerThread thread = objectPool.borrowObject();

                if(thread != null){
                    Element element = clientManager.take();
                    if(element != null) {
                        thread.setElement(element);

                        fixedThreadPool.submit(thread);

                    }
                }
            }catch(Exception e){
                log.error(e.getMessage(),e);
            }
        }
    }


    public int busy(){
        return ((ThreadPoolExecutor)fixedThreadPool).getActiveCount();
    }

    public void stop(){
        fixedThreadPool.shutdown();

        this.scheduleMain.stop();
    }

    public void registerStop(){
        Thread t = new Thread(new ShutdownHook(), "Jsidekiq Consumer ShutdownHook-Thread");
        Runtime.getRuntime().addShutdownHook(t);

        this.scheduleMain.registerStop();
    }

    class ShutdownHook implements Runnable{
        @Override
        public void run() {
            log.info("Jsidekiq Consumer stop begin...");
            Long st = System.currentTimeMillis();

            fixedThreadPool.shutdown();

            log.info("Jsidekiq Consumer stop begin...  {}",(System.currentTimeMillis() - st));
        }
    }

    public void setClientManager(ClientManager clientManager){
        this.clientManager = clientManager;

        this.scheduleMain.setClientManager(clientManager);
    }

}
