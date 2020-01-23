package com.example.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;
import java.util.Map;


public interface TestMapper {
    @Select("select * from user where account=#{account} and password=#{password}")
    Map selectUser(@Param("account") String account,@Param("password") String password);

    @Select(value = "select * from sale")
    List<Map> getSaleList();

    @Update(value = "update sale set status='已售出' where id=#{id}")
    int updateStatus(Integer id);

    @Delete(value = "delete from sale where id=#{id}")
    int delSaleById(Integer id);

    @Select(value = "select * from lese a,fenlei b where a.fenid=b.id")
    List<Map> getLeseList();

    @Select(value = "select * from fenlei")
    List<Map> getFenlei();

    @Insert(value = "insert into lese(lajiname,fenid) values(#{lajiname},#{fenlei})")
    int insertCT(@Param("lajiname") String lajiname, @Param("fenlei") Integer fenlei);

    @Delete(value = "delete from lese where id=#{id}")
    int delLeseById(Integer id);

    @Insert(value = "insert into fenlei(fenlei) values (#{kind})")
    int insertFL(String kind);

    @Delete(value = "delete from fenlei where id=#{id}")
    int delFL(Integer id);


    //测试调用存储过程
    @Select("call pro_c_getAddressById()")
    @Options(statementType= StatementType.CALLABLE )
    List<Map> test();

}
