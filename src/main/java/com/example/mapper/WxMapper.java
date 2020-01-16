package com.example.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

    //出售（上传图片）
    @Insert(value = "insert into sale (tenderid,phone,locations,filepath) values (#{tenderid},#{phone},#{locations},#{filepath})")
    int insertSale(Map map);

    //每次上传一次照片能量+5
    @Update(value = "update wxuser set nengliang=nengliang+5 where tenderid=#{tenderid}")
    int updateWxuser(String tenderid);

    //如果能量>20 能量值清零  等级level+1
    //1,查询能量值
    @Select(value = "select nengliang from wxuser where tenderid=#{tenderid}")
    String selectNL(String tenderid);
    //2,能量值清零，等级level+1
    @Update(value = "update wxuser set level=level+1,nengliang=0 where tenderid=#{tenderid}")
    int updateNL(String tenderid);

    //我的售出列表
    @Select(value = "select * from sale where tenderid=#{tenderid}")
    List<Map> getSaleList(String tenderid);
}
