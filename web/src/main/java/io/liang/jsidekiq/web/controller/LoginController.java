package io.liang.jsidekiq.web.controller;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by cuizheng on 2017/2/17.
 */
@Controller
@RequestMapping("/login")
public class LoginController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("")
    private ModelAndView index(){
        ModelAndView mv = new ModelAndView("/main/login");
        mv.addObject("username","zyl");
        return mv;
    }

    // 登录
    @RequestMapping("/in")
    @ResponseBody
    public JSONObject login(String username, String pass,HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug("login username : {}  pass : {}",username,pass);

        JSONObject json = new JSONObject();

        json.put("status", 0);
        return json;
    }

    // 退出
    @RequestMapping("/out")
    public ModelAndView out(){
        ModelAndView mv = new ModelAndView("/main/login");
        mv.addObject("username","zyl");
        return mv;
    }

}
