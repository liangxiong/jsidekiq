package io.liang.jsidekiq.client.consumer;

import io.liang.jsidekiq.client.ClientManager;
import io.liang.jsidekiq.client.config.Config;
import io.liang.jsidekiq.client.consumer.thread.ConsumerThread;
import io.liang.jsidekiq.client.pojo.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.liang.jsidekiq.client.consumer.thread.ConsumerThreadPool;

import java.util.HashSet;
import java.util.Set;
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
    }

    class ShutdownHook implements Runnable{
        @Override
        public void run() {
            log.info("Jsidekiq Consumer stop begin...");
            Long st = System.currentTimeMillis();

            try {
                Set<Element> workes = ConsumerThread.getWorkes();
                log.error("Work still in progress.size: {}", workes.size());
                if (workes.size() > 0) {
                    log.error("Work still in progress: {}", workes);
                    for (Element element : workes) {
                        clientManager.push(element);
                    }
                }
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }

            fixedThreadPool.shutdown();

            scheduleMain.stop();

            log.info("Jsidekiq Consumer stop begin...  {}",(System.currentTimeMillis() - st));
        }
    }

    public void setClientManager(ClientManager clientManager){
        this.clientManager = clientManager;

        this.scheduleMain.setClientManager(clientManager);
    }

}
