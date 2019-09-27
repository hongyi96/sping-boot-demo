package com.example.hcnetdemo.controller;

import com.example.hcnetdemo.ClientDemo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: hongyi
 * @date: 2019/8/7 20:19
 */

@RestController
@RequestMapping("test")
public class HikSdkController {
    @RequestMapping(method = RequestMethod.POST)
    public Map<String, Object> login(@RequestParam String username,
                                     @RequestParam String password,
                                     @RequestParam String m_sDeviceIP) {
        Map<String, Object> result = new HashMap<>(2);
        ClientDemo cd = new ClientDemo();
        //初始化
        cd.CameraInit();
        //注册
        result.put("result",cd.register(username, password, m_sDeviceIP));
        cd.SetupAlarmChan();
        cd.StartAlarmListen();
        System.out.println(Thread.currentThread().getName());

        return result;
    }

}
