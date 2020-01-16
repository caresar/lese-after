package com.example.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface WxMapper {
    //小程序第一次进入插入用户信息
    @Insert(value = "insert into wxuser(tenderid,level,nengliang) values (#{tenderId},1,0)")
    int insertWxuser(String tenderId);

    //以后进入就查询是否存在
    @Select(value = "select * from wxuser where tenderid=#{tenderId}")
    Map selectWxuser(String tenderId);

    //查询垃圾属于哪个分类
    @Select(value = "select b.fenlei from lese a,fenlei b where a.fenid=b.id and a.lajiname like concat('%',#{content},'%')")
    String getFenlei(String content);

    //查询积分和能量值
    @Select(value = "select level,nengliang from wxuser where tenderid=#{tenderId}")
    List<Map> getjifen(String tenderId);
}
