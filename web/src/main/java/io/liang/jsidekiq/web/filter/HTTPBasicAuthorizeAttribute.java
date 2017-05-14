package io.liang.jsidekiq.web.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.util.Base64;
import io.liang.jsidekiq.client.config.Config;
import sun.misc.BASE64Decoder;


/**
 * Created by zhiping on 17/5/14.
 */
public class HTTPBasicAuthorizeAttribute implements Filter {

    private static String Name = "test";
    private static String Password = "test";

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {


    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)res;

        if(!checkHeaderAuth(request, response)){//重新登录
            response.setStatus(401);
            response.setHeader("Cache-Control", "no-store");
            response.setDateHeader("Expires", 0);
            response.setHeader("WWW-authenticate", "Basic Realm=\"\"");
        }else{
            chain.doFilter(req, res);
        }

    }

    private boolean checkHeaderAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String auth = request.getHeader("Authorization");

        if ((auth != null) && (auth.length() > 6)) {
            auth = auth.substring(6, auth.length());

            String decodedAuth = new String(Base64.decodeFast(auth));

            String[] srr = decodedAuth.split(":");
            if(srr.length > 1){
                Config config = Config.getInstance();

                if(srr[0].equals(config.getAdminName()) && srr[1].equals(config.getAdminPassword())){
                    return true;
                }
            }
        }
        return false;

    }

    public void nextStep(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter pw = response.getWriter();
        pw.println("<html> next step, authentication is : " + request.getSession().getAttribute("auth") + "<br>");
        pw.println("<br></html>");
    }

    public static void main(String[] args) {
        System.out.println(new String());
    }

}