package com.example.lalala;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = "com.example")
@MapperScan("com.example.mapper")
public class LalalaApplication {

    public static void main(String[] args) {
        SpringApplication.run(LalalaApplication.class, args);
    }


    /**
    最后总结：Filter过滤器（放行静态资源）
            微信登录wx.login
    */
}
