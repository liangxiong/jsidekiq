package io.liang.jsidekiq.web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import io.liang.jsidekiq.web.service.DemoService;

/**
 * Created by zhangyouliang
 **/
@Controller
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private DemoService demoService;


    /**
     * http://127.0.0.1:8080/jsidekiq/demo/asyn?name=zyl
     * 异步队列
     * @param name
     * @return
     */
    @RequestMapping("/asyn")
    @ResponseBody
    private String asyn(@RequestParam("name") final String name){
        for(int i=0;i<1;i++) {
            demoService.sendAsynMail(name);
        }
        return "ok";
    }

    /**
     * http://127.0.0.1:8080/jsidekiq/demo/schedule?name=zyl
     * 延迟任务
     * @param name
     * @return
     */
    @RequestMapping("/schedule")
    @ResponseBody
    private String schedule(@RequestParam("name") final String name){
        for(int i=0;i<1;i++) {
            demoService.sendScheduleMail(name);
        }

        return "ok";
    }

    /**
     * http://127.0.0.1:8080/jsidekiq/demo/dead?name=zyl
     * 异常队列
     * @param name
     * @return
     */
    @RequestMapping("/dead")
    @ResponseBody
    private String dead(@RequestParam("name") final String name){
        try {
            for(int i=0;i<5;i++) {
                demoService.sendDeadMail(name);
                Thread.sleep(6000L);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ok";
    }

}
