package com.example.transactiondemo.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.transactiondemo.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserDao extends BaseMapper<User> {


    @Select("SELECT * FROM user WHERE user_id=#{userId};")
    User getUserByUserId(int userId);


    @Select("SELECT user_id,username,password,phone,nick_name,reg_time FROM user")
    List<User> getAllUser();

}