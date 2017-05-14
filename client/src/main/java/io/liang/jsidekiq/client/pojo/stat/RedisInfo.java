package io.liang.jsidekiq.client.pojo.stat;

/**
 * redis 的信息
 * 参考sidekiq
 * {"redis_version"=>"2.8.13 redis 版本", "uptime_in_days"=>"0 上线天数", "connected_clients"=>"2 连接数", "used_memory_human"=>"996.58K  使用内存数", "used_memory_peak_human"=>"996.58K 内存占用峰值"}
 * Created by zhiping on 17/4/23.
 */
public class RedisInfo {

    private String redisVersion;
    private String uptimeInDays;
    private String connectedClients;
    private String usedMemoryHuman;
    private String usedMemoryPeakHuman;

    public String getRedisVersion() {
        return redisVersion;
    }

    public void setRedisVersion(String redisVersion) {
        this.redisVersion = redisVersion;
    }

    public String getUptimeInDays() {
        return uptimeInDays;
    }

    public void setUptimeInDays(String uptimeInDays) {
        this.uptimeInDays = uptimeInDays;
    }

    public String getConnectedClients() {
        return connectedClients;
    }

    public void setConnectedClients(String connectedClients) {
        this.connectedClients = connectedClients;
    }

    public String getUsedMemoryHuman() {
        return usedMemoryHuman;
    }

    public void setUsedMemoryHuman(String usedMemoryHuman) {
        this.usedMemoryHuman = usedMemoryHuman;
    }

    public String getUsedMemoryPeakHuman() {
        return usedMemoryPeakHuman;
    }

    public void setUsedMemoryPeakHuman(String usedMemoryPeakHuman) {
        this.usedMemoryPeakHuman = usedMemoryPeakHuman;
    }
}
