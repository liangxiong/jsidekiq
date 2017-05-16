package io.liang.jsidekiq.client.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhangyouliang on 17/4/5.
 */
public class StringUtils {
    protected static Logger log = LoggerFactory.getLogger(StringUtils.class);

    private static final Pattern KVP_PATTERN = Pattern.compile("([_.a-zA-Z0-9][-_.a-zA-Z0-9]*)[=](.*)"); //key value pair pattern.

    /**
     * parse query string to Parameters.
     *
     * @param qs query string.
     * @return Parameters instance.
     */
    public static Map<String, String> parseQueryString(String qs)
    {
        if( qs == null || qs.length() == 0 )
            return new HashMap<String, String>();
        return parseKeyValuePair(qs, "\\&");
    }

    /**
     * parse key-value pair.
     *
     * @param str string.
     * @param itemSeparator item separator.
     * @return key-value map;
     */
    private static Map<String, String> parseKeyValuePair(String str, String itemSeparator)
    {
        String[] tmp = str.split(itemSeparator);
        Map<String, String> map = new HashMap<String, String>(tmp.length);
        for(int i=0;i<tmp.length;i++)
        {
            Matcher matcher = KVP_PATTERN.matcher(tmp[i]);
            if( matcher.matches() == false )
                continue;
            map.put(matcher.group(1), matcher.group(2));
        }
        return map;
    }

    /**
     * 返回异常的堆栈
     * @param e
     * @return
     */
    public static String getExceptionStack(Exception e) {
        StringWriter sw = null;
        PrintWriter pw = null;
        String str = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);

            e.printStackTrace(pw);

            str = sw.toString();
        }catch (Exception e1) {
            log.error(e1.getMessage(),e1);
        }finally {
            if(pw != null) {
                pw.close();
            }

            if(sw != null) {
                try {
                    sw.close();
                }catch (Exception e2){
                    log.error(e2.getMessage(),e2);
                }
            }
        }

        return str;
    }


}
