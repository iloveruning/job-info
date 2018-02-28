package com.hfutonline.jobinfo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author chenliangliang
 * @date 2017/12/21
 */
@Controller
public class MainController {

    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @GetMapping("/content")
    public String content(){
        return "content";
    }
}
