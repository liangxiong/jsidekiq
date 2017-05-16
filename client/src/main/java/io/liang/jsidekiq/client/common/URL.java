package io.liang.jsidekiq.client.common;



import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.net.*;
import java.util.*;

/**
 * 参考dubbo
 * Created by zhangyouliang on 17/4/5.
 */
public class URL implements Serializable {
    private static final long serialVersionUID = 7832616742616332703L;

    private String protocol;

    private String username;

    private String password;

    private String host;

    private int port = 0;

    private String path;

    private String query;

    private Map<String, String> parameters = new HashMap<String, String>();

    private static volatile transient Map<String, URL> urls = new HashMap<String, URL>();

    public static URL getURL(String str){
        URL url = urls.get(str);
        if(url == null){
            url = new URL(str);
            urls.put(str,url);
        }
        return url;
    }

    private URL() {

    }

    private URL(String url){
        try {
            java.net.URL netUrl = new java.net.URL(url);

            this.protocol = netUrl.getProtocol();
            this.host = netUrl.getHost();
            this.port = netUrl.getPort();

            this.path = netUrl.getPath();
            this.query = netUrl.getQuery();


            if(org.apache.commons.lang3.StringUtils.isNotBlank(this.query)){
                String[] params = this.query.split("&");
                for(int i=0;i<params.length;i++){
                    String[] param = params[i].split("=");
                    if(param.length == 2){
                        this.parameters.put(param[0],param[1]);
                    }else{
                        throw new RuntimeException("parse url "+url +" params ="+params[i]+" error");
                    }
                }
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {
        }
    }
    public String getParameter(String key){
        return this.parameters.get(key);
    }

    public String getParameter(String key,String def){
        String val = getParameter(key);
        String ret = def;
        if(StringUtils.isNotBlank(val)){
            ret = val;
        }
        return ret;
    }

    public Integer getParameter(String key,Integer def){
        String val = getParameter(key);
        Integer ret = def;
        if(StringUtils.isNotBlank(val)){
            ret = Integer.parseInt(val);
        }
        return ret;
    }

    public Long getParameter(String key,Long def){
        String val = getParameter(key);
        Long ret = def;
        if(StringUtils.isNotBlank(val)){
            ret = Long.parseLong(val);
        }
        return ret;
    }

    public boolean getParameter(String key,boolean def){
        String val = getParameter(key);
        boolean ret = def;
        if(StringUtils.isNotBlank(val)){
            ret = Boolean.parseBoolean(val);
        }
        return ret;
    }


    public String getProtocol() {
        return protocol;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getPath() {
        return path;
    }

    public String getQuery() {
        return query;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public static void main(String[] args)throws  Exception {
//        java.net.URL url = new java.net.URL("http://wwww.baidu.com/user/regiest?name=zyl&pwd=123");

//        System.out.println(url.getProtocol());
//        System.out.println(url.getHost());
//        System.out.println(url.getPort());
//        System.out.println(url.getDefaultPort());
//
//        System.out.println(url.getPath());
//
//        System.out.println(url.getQuery());
//        System.out.println(url.getFile());
//        System.out.println(url.getRef());
//        System.out.println(url.getContent());

        String str = "http://wwww.baidu.com/user/regiest?name=zyl&pwd=123";
        URL url = new URL(str);
        System.out.println(url.getProtocol());
        System.out.println(url.getHost());
        System.out.println(url.getPort());

        System.out.println(url.getPath());

        System.out.println(url.getQuery());
        System.out.println(url.getParameter("pwd",1));




    }


}