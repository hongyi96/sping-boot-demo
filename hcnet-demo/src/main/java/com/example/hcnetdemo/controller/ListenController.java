package com.example.hcnetdemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

/**
 * @author hongyi
 * @date 2019/9/11 19:41
 */

@RestController
public class ListenController {
    @RequestMapping(value = "listen", method = RequestMethod.POST)
    public String saveFile(@RequestParam(name = "alarm_image") MultipartFile file) {
        System.out.println("------start------");
        String time = String.valueOf(System.currentTimeMillis());
        if (!file.isEmpty()) {
            try {
                BufferedOutputStream out = new BufferedOutputStream(
                        new FileOutputStream(new File("f:\\face\\"  + time + ".jpg")));
                System.out.println(file.getName());
                out.write(file.getBytes());
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return "上传失败," + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "上传失败," + e.getMessage();
            }
            return "上传成功";

        } else {
            return "上传失败，因为文件是空的.";
        }
    }
}