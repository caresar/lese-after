package com.example.controller;

import com.example.mapper.WxMapper;
import com.example.model.OpenIdJson;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;


@RestController
@RequestMapping("/weixin")
public class WxController {

    //微信code换取open_id

    private String appID = "wxb44d77c8ddc418c6";
    private String appSecret = "5fd925d791680d3444b96db6c7c9da32";

    @RequestMapping("/getOpenId")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public String getOpenId(@RequestParam("code") String code) throws JsonProcessingException {
        String result = "";
        try{//请求微信服务器，用code换取openid。HttpUtil是工具类，后面会给出实现，Configure类是小程序配置信息，后面会给出代码
//            result = HttpUtil.doGet(
//                    , null);
            String url="https://api.weixin.qq.com/sns/jscode2session?appid="
                    + "wxb44d77c8ddc418c6" + "&secret="
                    + "5fd925d791680d3444b96db6c7c9da32" + "&js_code="
                    + code
                    + "&grant_type=authorization_code";

            RequestConfig rc = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
            CloseableHttpClient httpclient = HttpClients.createDefault();
            if(url!=null){
                try {
                    // 创建HttpGet对象，将URL通过构造方法传入HttpGet对象
                    HttpGet httpGet=new HttpGet(url);
                    // 将配置好请求信息附加到http请求中
                    httpGet.setConfig(rc);
                    // 执行DefaultHttpClient对象的execute方法发送GET请求，通过CloseableHttpResponse接口的实例，可以获取服务器返回的信息
                    CloseableHttpResponse response = httpclient.execute(httpGet);
                    try {
                        // 得到返回对象
                        HttpEntity entity = response.getEntity();
                        if(entity!=null){
                            // 获取返回结果
                            result = EntityUtils.toString(entity);

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {
                        // 关闭到客户端的连接
                        response.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        // 关闭http请求
                        httpclient.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OpenIdJson openIdJson = mapper.readValue(result, OpenIdJson.class);
        System.out.println(result.toString());
        String tenderId = openIdJson.getOpenid();
        login(tenderId);
        return result;
    }

    @Autowired
    WxMapper wxMapper;

    //查询小程序登录用户信息进行登录
    private void login(String tenderId){
        Map map = wxMapper.selectWxuser(tenderId);
        if(map!=null){

        }else{
            wxMapper.insertWxuser(tenderId);
        }
    }

    //查询垃圾分类
    @RequestMapping("getFenlei")
    @ResponseBody
    public String getFenlei(String content){
        if(content==null || content==""){
            return "查询不许为空！";
        }else{
            String fenlei = wxMapper.getFenlei(content);
            if(fenlei!=null && fenlei!=""){
                return fenlei;
            }else {
                return "词条不足！";
            }
        }
    }

    //查询积分和能量值
    @RequestMapping("/getjifen")
    @ResponseBody
    public List<Map> getjifen(String tenderId){
        List<Map> getjifen = wxMapper.getjifen(tenderId);
        return getjifen;
    }

    //上传图片
    @RequestMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam(value = "pic") MultipartFile pic, @RequestParam Map param, Model model) throws ParseException {
        if(pic.isEmpty()){
            return "上传文件不可为空";
        }
        String filename = pic.getOriginalFilename();
        String suffixName = filename.substring(filename.lastIndexOf("."));
        System.out.println("后缀名："+suffixName);
        String filePath="D:\\suibian\\lese-after\\src\\main\\resources\\static\\assets";
        filename= UUID.randomUUID()+suffixName;
        System.out.println("随机文件名："+filename);
        File does=new File(filePath+filename);
        try {
            pic.transferTo(does);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String tenderid=String.valueOf(param.get("tenderid")).substring(1,String.valueOf(param.get("tenderid")).length()-1);
        Map map=new HashMap();
        map.put("tenderid",String.valueOf(param.get("tenderid")).substring(1,String.valueOf(param.get("tenderid")).length()-1));
        map.put("phone",String.valueOf(param.get("phone")).substring(1,String.valueOf(param.get("phone")).length()-1));
        map.put("locations",String.valueOf(param.get("locations")).substring(1,String.valueOf(param.get("locations")).length()-1));
        map.put("filepath","http://localhost:8080/assets"+filename);
        int i = wxMapper.insertSale(map);
        wxMapper.updateWxuser(tenderid);

        String nengliang = wxMapper.selectNL(tenderid);
        System.out.println("能量值："+nengliang);
        //25能量值=1等级
        if(Integer.valueOf(nengliang)>20){
            wxMapper.updateNL(tenderid);
        }
        if(i>0){
            return "success";
        }else{
            return "false";
        }
    }

    //查看我的售出列表
    @RequestMapping("/mysale")
    @ResponseBody
    public List<Map> getsale(String tenderid){
        List<Map> saleList = wxMapper.getSaleList(tenderid);
        return saleList;
    }

    //查看朋友圈（所有）
    @RequestMapping("findPyq")
    @ResponseBody
    public List<Map> findPyq(){
        List<Map> pyq = wxMapper.getPyq();

        for (int i = 0; i < pyq.size(); i++) {
            Integer pinglun_id = (Integer) pyq.get(i).get("id");
            if(pinglun_id!=null){
                List<Map> pinglun = wxMapper.getpinglun(pinglun_id);
                pyq.get(i).put("pinglunlist",pinglun);
                int count = wxMapper.getplCount(pinglun_id);
                pyq.get(i).put("pingluncount",count);
            }else{
                pyq.get(i).put("pinglunlist",new ArrayList<>());
                pyq.get(i).put("pingluncount",0);
            }

        }
        System.out.println(pyq);
        return pyq;
    }

    //发布动态
    @RequestMapping("/fbdt")
    @ResponseBody
    public Integer fbdt(@RequestParam Map map){
        System.out.println("动态参数："+map);
        int fbdt = wxMapper.fbdt(map);
        return fbdt;
    }

    //点赞
    @RequestMapping("/dianzan")
    @ResponseBody
    public Integer dianzan(@RequestParam("id") Integer id,@RequestParam("tenderid") String tenderid){
        System.out.println(id);
        System.out.println(tenderid);
        List<Map> getzanlist = wxMapper.getzanlist(id, tenderid);
        int zan = 0;
        if(getzanlist.size()>0){
            zan = wxMapper.findZan(id);
        }
        else{
            int dianzan = wxMapper.dianzan(id);
            wxMapper.insertzanlist(id,tenderid);
            zan = wxMapper.findZan(id);
        }

        return zan;
    }

    //发表评论
    @RequestMapping("/fbpinglun")
    @ResponseBody
    public Integer fbpinglun(@RequestParam String name,@RequestParam String content,@RequestParam Integer id){
        int fbpinglun = wxMapper.fbpinglun(name, content, id);
        return fbpinglun;
    }
}
