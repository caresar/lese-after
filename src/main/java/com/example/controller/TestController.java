package com.example.controller;

import com.example.mapper.TestMapper;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/test")
public class TestController {
    @Autowired
    TestMapper testMapper;


    @RequestMapping("toLogin")
    public String toLogin(){
        return "login-page";
    }

    @RequestMapping("/test1")
    public String test(){
        return "test";
    }

    @RequestMapping("toIndex")
    public String toIndex(){
        return "index";
    }

    @RequestMapping("/saleList")
    public String saleList(){
        return "saleList";
    }

    //前台登录
    @RequestMapping("/login")
    public String login(String account, String password, HttpSession session, Model model){
        Map map = testMapper.selectUser(account, password);
        if(map!=null){
            session.setAttribute("account",map.get("account"));
            model.addAttribute("account",account);
            return "forward:/test/toIndex";
        }else{
            return "forward:/test/toLogin";
        }
    }

    //退出
    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("account");
        return "forward:toLogin";
    }





}
