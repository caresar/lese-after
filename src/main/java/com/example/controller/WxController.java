package com.example.controller;

import com.example.mapper.WxMapper;
import com.example.model.OpenIdJson;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;


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

}
