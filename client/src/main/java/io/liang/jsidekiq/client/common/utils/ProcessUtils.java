package io.liang.jsidekiq.client.common.utils;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

/**
 * 进程号
 * Created by zhiping on 17/4/5.
 */
public class ProcessUtils {

    public static String pid() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.split("@")[0];
        return pid;

    }
}
