package io.liang.jsidekiq.web.controller;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by zhangyouliang
 */
@Controller
@RequestMapping("/home")
public class HomeController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("index")
    private ModelAndView index(){
        ModelAndView mv = new ModelAndView("/index");
        return mv;
    }
}
