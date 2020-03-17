package com.example.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//在Spring Boot 1.5版本都是靠重写WebMvcConfigurerAdapter的方法来添加自定义拦截器，消息转换器等
@Configuration
public class Configure implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //springboot中配置addResourceHandler和addResourceLocations，使得可以从磁盘中读取图片、视频、音频等
        //通过localhost:8080/test/filename.jpg就可以访问本地磁盘的文件
        registry.addResourceHandler("/test/**").addResourceLocations("file:D:/");
    }
}
