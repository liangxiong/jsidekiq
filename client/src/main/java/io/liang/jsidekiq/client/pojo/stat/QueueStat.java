package io.liang.jsidekiq.client.pojo.stat;

/**
 * 队列的统计
 * Created by zhiping on 17/4/23.
 */
public class QueueStat {

    //队列名称
    private String queue;
    //数量
    private Long quantity;

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
