package io.liang.jsidekiq.web.controller.detail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import io.liang.jsidekiq.client.ClientManager;

/**
 * 进程信息
 * Created by zhangyouliang
 */
@Controller
@RequestMapping("/detail/processes")
public class ProcessController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("index")
    private ModelAndView index(){
        ModelAndView mv = new ModelAndView("/detail/processes/index");

        ClientManager client = ClientManager.getInstance();

        mv.addObject("rows",client.getProcessStatList());

        return mv;
    }
}
