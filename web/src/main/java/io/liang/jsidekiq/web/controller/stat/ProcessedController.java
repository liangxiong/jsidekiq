package io.liang.jsidekiq.web.controller.stat;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import io.liang.jsidekiq.client.ClientManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 处理的统计信息
 * Created by zhangyouliang
 */
@Controller
@RequestMapping("/stat/processed")
public class ProcessedController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("index")
    private ModelAndView index(Integer days){
        ModelAndView mv = new ModelAndView("/stat/processed/index");
        ClientManager client = ClientManager.getInstance();

        if(days == null){
            days = 7;
        }

        mv.addObject("days",days);

        List<String> dayList = new ArrayList<String>();

        List<String> processedList = new ArrayList<String>();
        List<String> failList = new ArrayList<String>();

        Long now = System.currentTimeMillis();
        for (int i = days-1; i > -1; i--) {
            Date d = new Date(now - (i * 24 * 3600 * 1000L));
            String ymd = new SimpleDateFormat("yyyy-MM-dd").format(d);

            dayList.add(ymd.replace("-",""));

            String quantity = client.get("stat:processed" + ":" + ymd);
            processedList.add(StringUtils.isBlank(quantity) ? "0" : quantity);


            quantity = client.get("stat:failed" + ":" + ymd);
            failList.add(StringUtils.isBlank(quantity) ? "0" : quantity);
        }

        mv.addObject("dayList",dayList);
        mv.addObject("processedList",processedList);
        mv.addObject("failList",failList);

        return mv;
    }

//    @RequestMapping("test")
//    private String test(){
//        ClientManager client = ClientManager.getInstance();
//
//        client.test();
//
//        return "a";
//    }
}
