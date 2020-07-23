package com.zdmoney.controller;/**
 * Created by pc05 on 2017/11/21.
 */


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述 :
 *
 * @author : huangcy
 * @create : 2017-11-21 9:52
 * @email : huangcy01@zendaimoney.com
 **/
@RestController
public class ApplicationController {

    @RequestMapping("/")
    @ResponseBody
    public String test(){
        return "<h1><div style='font-family:Georgia,serif;'>lcb_gateway</div></h1>";
    }
}
