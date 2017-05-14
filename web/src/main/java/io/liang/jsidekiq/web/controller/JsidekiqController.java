package io.liang.jsidekiq.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.liang.jsidekiq.web.po.User;
import io.liang.jsidekiq.web.service.JsidekiqUserService;
import io.liang.jsidekiq.web.service.impl.JsidekiqUserService2Impl;

/**
 * Created by cuizheng on 2017/2/17.
 */
@RestController
@RequestMapping("/jsidekiq")
@Api(tags={"jsidekiq测试API"})
public class JsidekiqController {

    @Autowired
    private JsidekiqUserService jsidekiqUserService;

    @Autowired
    private JsidekiqUserService2Impl jsidekiqUserService2Impl;



    //http://127.0.0.1:8080/jsidekiq/asyn?name=zyl
    @GetMapping("/asyn")
    @ApiOperation(value="asyn", notes="返回内容是输入的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(name="name", value="名称", required = true, dataType = "String", paramType = "query")
    })
    private User asyn(@RequestParam("name") final String name){
        User u = new User();
        u.setName(name);
        u.setSex("男");

        for(int i=0;i<1;i++) {
            jsidekiqUserService.sendMail(u);
        }

        return u;
    }


    //http://127.0.0.1:8080/jsidekiq/schedule?name=zyl
    @GetMapping("/schedule")
    @ApiOperation(value="schedule", notes="返回内容是输入的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(name="name", value="名称", required = true, dataType = "String", paramType = "query")
    })
    private User schedule(@RequestParam("name") final String name){
        User u = new User();
        u.setName(name);
        u.setSex("男");

        for(int i=0;i<1;i++) {
            jsidekiqUserService.sendScheduleMail(u);
        }

        return u;
    }

    //http://127.0.0.1:8080/jsidekiq/dead?name=zyl
    @GetMapping("/dead")
    @ApiOperation(value="dead", notes="返回内容是输入的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(name="name", value="名称", required = true, dataType = "String", paramType = "query")
    })
    private User dead(@RequestParam("name") final String name){
        User u = new User();
        try {
            for(int i=0;i<5;i++) {
                u.setSex("男");
                u.setName(name+i);
                jsidekiqUserService.sendDeadMail(u);
                Thread.sleep(6000L);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return u;
    }

}
