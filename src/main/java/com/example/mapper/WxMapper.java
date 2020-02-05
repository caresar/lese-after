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
    @Insert(value = "insert into sale (tenderid,phone,locations,filepath,status,date) values (#{tenderid},#{phone},#{locations},#{filepath},'待出售',now())")
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
    @Select(value = "select * from sale where tenderid=#{tenderid} order by date desc")
    List<Map> getSaleList(String tenderid);

    //查询朋友圈
    @Select(value = "select * from pyq order by date desc")
    List<Map> getPyq();

    //发布动态
    @Insert(value = "insert into pyq(name,txurl,content,zan_num,pinglun_num,date,tenderid) values (#{name},#{txUrl},#{content},0,0,now(),#{tenderid})")
    int fbdt(Map map);

    //点赞同时，插入点赞列表（谁点的赞，只允许点赞一次）
    @Insert(value = "insert into zanlist(pyqid,tenderid) values(#{id},#{tenderid})")
    int insertzanlist(@Param("id") Integer id,@Param("tenderid") String tenderid);

    //查询该用户是否点过赞
    @Select(value = "select * from zanlist where pyqid=#{id} and tenderid=#{tenderid}")
    List<Map> getzanlist(@Param("id") Integer id,@Param("tenderid") String tenderid);

    //点赞
    @Update(value = "update pyq set zan_num=zan_num+1 where id=#{id}")
    int dianzan(Integer id);
    //查询点赞数量
    @Select(value = "select zan_num from pyq where id=#{id}")
    int findZan(Integer id);

    //查询评论
    @Select(value = "select b.name,b.content from pyq a,pl_list b where a.id=b.pinglun_id and a.id=#{id} order by b.date asc")
    List<Map> getpinglun(Integer id);

    //查询评论数量
    @Select(value = "select count(*) from pyq a,pl_list b where a.id=b.pinglun_id and a.id=#{id}")
    int getplCount(Integer id);

    //发表评论
    @Insert(value = "insert into pl_list(name,content,pinglun_id,date) values (#{name},#{content},#{id},now())")
    int fbpinglun(@Param("name") String name,@Param("content") String content,@Param("id") Integer id);
}
