package com.example.transactiondemo.user.controller;

import com.example.transactiondemo.user.entity.User;
import com.example.transactiondemo.user.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取用户列表
     *
     * @return userList
     * @role admin
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @JsonView(User.UserView.class)
    public Map<String, Object> getAllUsers() {
        Map<String, Object> result = new HashMap<>(2);
        List<User> userList = userService.getAllUser();
        result.put("count", userList.size());
        result.put("items", userList);
        return result;
    }


}