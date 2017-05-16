package io.liang.jsidekiq.client.consumer.thread;

import io.liang.jsidekiq.client.ClientManager;
import io.liang.jsidekiq.client.common.URL;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * Created by zhangyouliang on 17/4/16.
 */
public class ConsumerThreadPool implements PooledObjectFactory<ConsumerThread> {
    private static GenericObjectPoolConfig config;
    private static GenericObjectPool<ConsumerThread> pool;


    public void build(URL url) {
        config = new GenericObjectPoolConfig();
        config.setMinIdle(url.getParameter("minIdle", 1));
        config.setMaxTotal(url.getParameter("maxTotal", 1));

        pool = new GenericObjectPool<ConsumerThread>(this, config);
    }

    public int getNumActive(){
        return pool.getNumActive();
    }

    public ConsumerThread borrowObject()throws Exception{
        return pool.borrowObject();
    }

    public void returnObject(ConsumerThread thread){
        pool.returnObject(thread);
    }


    @Override
    public PooledObject<ConsumerThread> makeObject() throws Exception {
        ConsumerThread t = new ConsumerThread();

        t.setClientManager(ClientManager.getInstance());
        t.setThreadPool(this);


        return new DefaultPooledObject<ConsumerThread>(t);
    }


    @Override
    public void destroyObject(PooledObject<ConsumerThread> p) throws Exception {

    }

    @Override
    public boolean validateObject(PooledObject<ConsumerThread> p) {

        return true;
    }

    /**
     * 被使用时候
     * @param p
     * @throws Exception
     */
    @Override
    public void activateObject(PooledObject<ConsumerThread> p) throws Exception {

    }

    /**
     * 用完放回池里
     * @param p
     * @throws Exception
     */
    @Override
    public void passivateObject(PooledObject<ConsumerThread> p) throws Exception {

    }
}
