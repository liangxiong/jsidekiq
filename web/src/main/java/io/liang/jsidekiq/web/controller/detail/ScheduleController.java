package io.liang.jsidekiq.web.controller.detail;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import io.liang.jsidekiq.client.ClientManager;
import io.liang.jsidekiq.client.common.Cons;
import io.liang.jsidekiq.client.pojo.Page;
import io.liang.jsidekiq.client.pojo.stat.QueueStat;

import java.util.ArrayList;
import java.util.List;

/**
 * 队列信息
 * Created by zhangyouliang
 */
@Controller
@RequestMapping("/detail/schedule")
public class ScheduleController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("index")
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView("/detail/schedule/index");

        ClientManager client = ClientManager.getInstance();

        List rows = new ArrayList<>();

        QueueStat stat = null;

        stat = new QueueStat();
        stat.setQueue(Cons.SCHEDULE_SCHEDULE);
        stat.setQuantity(client.scheduleSize(stat.getQueue()));
        rows.add(stat);

        stat = new QueueStat();
        stat.setQueue(Cons.SCHEDULE_RETRY);
        stat.setQuantity(client.scheduleSize(stat.getQueue()));
        rows.add(stat);


        stat = new QueueStat();
        stat.setQueue(Cons.SCHEDULE_DEAD);
        stat.setQuantity(client.scheduleSize(stat.getQueue()));
        rows.add(stat);

        mv.addObject("rows",rows);
        return mv;
    }


    @RequestMapping("schedule")
    public ModelAndView queue(String name,Page page){
        ModelAndView mv = new ModelAndView("/detail/schedule/schedule");

        ClientManager client = ClientManager.getInstance();
        client.paginatorByZset(name,page);

        mv.addObject("page",page);

        mv.addObject("name",name);

        return mv;
    }

    @RequestMapping("delSet")
    @ResponseBody
    public JSONObject delSet(String name){
        ClientManager client = ClientManager.getInstance();
        client.delQueue(name);

        JSONObject json = new JSONObject();
        json.put("status",0);

        return json;
    }

    @RequestMapping("delTask")
    @ResponseBody
    public JSONObject delTask(String key) {
        ClientManager client = ClientManager.getInstance();
        boolean flag = client.delScheduleByValue(key);

        JSONObject json = new JSONObject();

        int status = 0;

        json.put("status", 0);

        return json;
    }

    @RequestMapping("retryTask")
    @ResponseBody
    public JSONObject retryTask(String key) {
        ClientManager client = ClientManager.getInstance();
        boolean flag = client.retry(key);

        log.debug("retry key:{} flag:{}",key,flag);

        JSONObject json = new JSONObject();

        int status = 0;

        json.put("status", 0);

        return json;
    }
}
