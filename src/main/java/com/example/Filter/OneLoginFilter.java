package com.example.Filter;

import com.example.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName OneLoginFilter.java
 * @Description TODO
 * @createTime 2020年01月22日 17:09:00
 */
@Component
public class OneLoginFilter {

    @Autowired
    TestMapper testMapper;

    @Scheduled(fixedRate=5000)
    public void oneLogin(){
        List<Map> test = testMapper.test();
        System.out.println(test);
    }

}
