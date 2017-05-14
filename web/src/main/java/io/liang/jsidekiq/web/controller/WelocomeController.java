package io.liang.jsidekiq.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import io.liang.jsidekiq.client.ClientManager;
import io.liang.jsidekiq.client.config.Config;

/**
 * Created by zhangyouliang
 */
@Controller
@RequestMapping("/welcome")
public class WelocomeController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("index")
    private ModelAndView index(){
        ModelAndView mv = new ModelAndView("/welcome/index");

        ClientManager client = ClientManager.getInstance();
        mv.addObject("redisInfo",client.getRedisInfo());
        mv.addObject("stat",client.getRunStat());

        mv.addObject("config", Config.getInstance());

        return mv;
    }
}
