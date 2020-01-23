package com.example.controller;

import com.example.mapper.TestMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/test")
public class TestController {
    @Autowired
    TestMapper testMapper;
    @Autowired
    TestService testService;


    @RequestMapping("toLogin")
    public String toLogin(){
        return "login-page";
    }

    //查看我的售出列表--后台管理
    @RequestMapping("/lalala")
    public String test(Model model){
        return "test";
    }

    @RequestMapping("toIndex")
    public String toIndex(){
        return "index";
    }

    @RequestMapping("/saleList")
    public String saleList(Model model,Integer pn){
        if(pn==null){
            pn=1;
        }
        PageHelper.startPage(pn,4);
        List<Map> saleList = testMapper.getSaleList();
        PageInfo<Map> info=new PageInfo<>(saleList);
        model.addAttribute("list",saleList);
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

    //根据id更改售出列表状态为已售出
    @RequestMapping("/deleteSale")
    @ResponseBody
    public Integer delSale(Integer id){
        int i = testMapper.updateStatus(id);
        return i;
    }

    //根据id删除售出列表
    @RequestMapping("/delSale")
    @ResponseBody
    public Integer delSa(Integer id){
        int i = testMapper.delSaleById(id);
        return i;
    }

    //词条管理
    @RequestMapping("/citiao")
    public String citiao(Model model,Integer pn){
        List<Map> fenye = testService.fenye(pn);
        model.addAttribute("list",fenye);
        return "leseList";
    }

    @RequestMapping("/leseAdd")
    public String leseAdd(){
        return "leseAdd";
    }

    //查询垃圾分类
    @RequestMapping("/lajifenlei")
    @ResponseBody
    public List<Map> lajifenlei(){
        List<Map> fenlei = testMapper.getFenlei();
        return fenlei;
    }

    //新增词条表单提交
    @RequestMapping("/addCT")
    public String addCT(String lajiname,Integer fenlei){
        int i = testMapper.insertCT(lajiname, fenlei);
        return "forward:citiao";
    }

    //删除词条
    @RequestMapping("/delCT")
    @ResponseBody
    public Integer delCT(Integer id){
        int i = testMapper.delLeseById(id);
        return i;
    }

    //垃圾分类管理
    @RequestMapping("/kind")
    public String kind(Model model,Integer pn){
        if(pn==null){
            pn=1;
        }
        PageHelper.startPage(pn,10);
        List<Map> fenlei = testMapper.getFenlei();
        PageInfo<Map> info=new PageInfo<>(fenlei);
        model.addAttribute("list",fenlei);
        return "kindList";
    }

    @RequestMapping("/kindAdd")
    public String kindAdd(){
        return "kindAdd";
    }

    @RequestMapping("/addFL")
    public String addFL(String kind){
        testMapper.insertFL(kind);
        return "forward:kind";
    }

    @RequestMapping("/delFL")
    @ResponseBody
    public Integer delFL(Integer id){
        int i = testMapper.delFL(id);
        return i;
    }

}
