package com.example.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


public interface TestMapper {
    @Select("select * from user where account=#{account} and password=#{password}")
    Map selectUser(@Param("account") String account,@Param("password") String password);
}
