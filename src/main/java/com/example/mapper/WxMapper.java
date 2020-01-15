package com.example.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

public interface WxMapper {
    //小程序第一次进入插入用户信息
    @Insert(value = "insert into wxuser(tenderid,level,nengliang) values (#{tenderId},0,0)")
    int insertWxuser(String tenderId);

    //以后进入就查询是否存在
    @Select(value = "select * from wxuser where tenderid=#{tenderId}")
    Map selectWxuser(String tenderId);
}
