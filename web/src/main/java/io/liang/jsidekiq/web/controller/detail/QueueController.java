package io.liang.jsidekiq.web.controller.detail;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import io.liang.jsidekiq.client.ClientManager;
import io.liang.jsidekiq.client.pojo.Page;

/**
 * 队列信息
 * Created by zhangyouliang
 */
@Controller
@RequestMapping("/detail/queue")
public class QueueController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("index")
    private ModelAndView index(){
        ModelAndView mv = new ModelAndView("/detail/queue/index");

        ClientManager client = ClientManager.getInstance();

        mv.addObject("rows",client.getQueueStatList());

        return mv;
    }


    @RequestMapping("queue")
    private ModelAndView queue(String queue,Page page){
        ModelAndView mv = new ModelAndView("/detail/queue/queue");

        ClientManager client = ClientManager.getInstance();
        client.paginatorByList(queue,page);

        mv.addObject("page",page);

        mv.addObject("queue",queue);

        return mv;
    }

    @RequestMapping("del")
    @ResponseBody
    private JSONObject del(String name){
        ClientManager client = ClientManager.getInstance();
        client.delQueue(name);

        JSONObject json = new JSONObject();
        json.put("status",0);

        return json;
    }





//    list:
//    value :  ::Rack::Utils.escape_html(text)
//    LREM key count value


}
