package io.liang.jsidekiq.client.common;

import java.util.regex.Pattern;

/**
 * Created by zhangyouliang on 17/4/5.
 */
public class Cons {
    public static final Long ZEROL = 0L;
    public static final Integer ZEROI = 0;


    public static final String SCHEDULE_RETRY = "retry";

    public static final String SCHEDULE_SCHEDULE = "schedule";

    public static final String SCHEDULE_DEAD = "dead";

    //统计的超时时间
    public static final Long STAT_TTL =  180 * 24 * 60 * 60 * 1000L;

}
