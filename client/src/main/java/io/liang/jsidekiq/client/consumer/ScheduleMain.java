package io.liang.jsidekiq.client.consumer;

import io.liang.jsidekiq.client.ClientManager;
import io.liang.jsidekiq.client.common.Cons;
import io.liang.jsidekiq.client.config.Config;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 延迟任务
 * Created by zhiping on 17/4/16.
 */
public class ScheduleMain {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    private ClientManager clientManager;
    private Config config;
    //延迟任务
    private Thread scheduleThread;

    //重试任务
    private Thread retryThread;

    //延迟任务



    public ScheduleMain(Config config){
        this.config = config;
    }

    public void start(){
        this.scheduleThread = new Thread(){
            public void run(){
                scheduleEnqueueJobs();
            }
        };
        this.scheduleThread.start();

        this.retryThread = new Thread(){
            public void run(){
                retryEnqueueJobs();
            }
        };
        this.retryThread.start();
    }

    //重试任务入队列
    public void retryEnqueueJobs(){
        for(;;) {
            try {
                clientManager.transferExpiredSchedule("retry");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            try {
                Thread.sleep(randomPollInterval());
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
        }
    }

    //延迟任务入队列
    public void scheduleEnqueueJobs(){
        for(;;) {
            try {
                clientManager.transferExpiredSchedule("schedule");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            try {
                Thread.sleep(randomPollInterval());
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
        }
    }

    /**
     * 抄写 sidekiq 的注解
     * # We do our best to tune the poll interval to the size of the active Sidekiq
     # cluster.  If you have 30 processes and poll every 15 seconds, that means one
     # Sidekiq is checking Redis every 0.5 seconds - way too often for most people
     # and really bad if the retry or scheduled sets are large.
     #
     # Instead try to avoid polling more than once every 15 seconds.  If you have
     # 30 Sidekiq processes, we'll poll every 30 * 15 or 450 seconds.
     # To keep things statistically random, we'll sleep a random amount between
     # 225 and 675 seconds for each poll or 450 seconds on average.  Otherwise restarting
     # all your Sidekiq processes at the same time will lead to them all polling at
     # the same time: the thundering herd problem.
     #
     # We only do this if poll_interval is unset (the default).
     * @return
     */
    public Long randomPollInterval(){
        Long processSize = clientManager.getProcessSize();
        if(processSize == null || Cons.ZEROL.equals(processSize)){
            processSize = 1L;
        }

        Long scheduleIntervalTime = config.getScheduleIntervalTime();
        int rand = RandomUtils.nextInt(0,100);
        Long time = (scheduleIntervalTime * rand / 100 + scheduleIntervalTime / 2);


        return time;
    }

    public void setClientManager(ClientManager clientManager){
        this.clientManager = clientManager;
    }

    public void stop(){
        scheduleThread.interrupt();
        retryThread.interrupt();
    }

    public void registerStop(){
        Thread t = new Thread(new ShutdownHook(), "Jsidekiq Schedule ShutdownHook-Thread");
        Runtime.getRuntime().addShutdownHook(t);
    }

    class ShutdownHook implements Runnable{
        @Override
        public void run() {
            log.info("Jsidekiq Schedule stop begin...");
            Long st = System.currentTimeMillis();

            stop();

            log.info("Jsidekiq Schedule stop begin...  {}",(System.currentTimeMillis() - st));
        }
    }

}
