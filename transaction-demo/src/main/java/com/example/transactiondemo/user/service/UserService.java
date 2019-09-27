package com.example.transactiondemo.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.transactiondemo.user.entity.User;

import java.util.List;

public interface UserService extends IService<User> {

    User getUserByUserId(int userId);

    List<User> getAllUser();
}
