package com.example.controller;

import com.example.mapper.TestMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName TestService.java
 * @Description TODO
 * @createTime 2020年01月22日 15:02:00
 */
@Service
public class TestService {
    @Autowired
    TestMapper testMapper;

    public List<Map> fenye(Integer pn){
        if(pn==null){
            pn=1;
        }
        if(pn<1){
            pn=1;
        }
        PageHelper.startPage(pn,10);
        List<Map> leseList = testMapper.getLeseList();
        PageInfo<Map> info=new PageInfo<>(leseList);


        return leseList;
    }
}
