package io.liang.jsidekiq.client.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangyouliang on 17/4/5.
 */
public class CollectionUtils {

    public static Map<String, String> toStringMap(String... pairs) {
        Map<String, String> parameters = new HashMap<String, String>();
        if (pairs.length > 0) {
            if (pairs.length % 2 != 0) {
                throw new IllegalArgumentException("pairs must be even.");
            }
            for (int i = 0; i < pairs.length; i = i + 2) {
                parameters.put(pairs[i], pairs[i + 1]);
            }
        }
        return parameters;
    }


}
