package com.example.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName TestMapper.java
 * @Description TODO
 * @createTime 2020年01月15日 13:14:00
 */
public interface TestMapper {
    @Select("select * from user where account=#{account} and password=#{password}")
    Map selectUser(@Param("account") String account,@Param("password") String password);
}
