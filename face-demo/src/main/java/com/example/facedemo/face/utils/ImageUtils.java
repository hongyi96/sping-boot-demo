package com.example.facedemo.face.utils;


import org.apache.commons.codec.binary.Base64;
import org.springframework.util.StringUtils;

import java.io.*;

/**
 * @author: hongyi
 * @date: 2019/8/8 16:15
 */
public class ImageUtils {

    public static String image2Base64(String imgPath) {
        InputStream input = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            input = new FileInputStream(imgPath);
            data = new byte[input.available()];
            input.read(data);
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 返回Base64编码过的字节数组字符串
        return Base64.encodeBase64String(data);
    }

    public static boolean base64ToImageFile(String imgStr, String imgFilePath) {
        // 图像数据为空
        if (StringUtils.isEmpty(imgStr)) {
            return false;
        }

        try {
            // Base64解码
            byte[] bytes = Base64.decodeBase64(imgStr);
            for (int i = 0; i < bytes.length; ++i) {
                // 调整异常数据
                if (bytes[i] < 0) {
                    bytes[i] += 256;
                }
            }
            OutputStream output = new FileOutputStream(imgFilePath);
            BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
            bufferedOutput.write(bytes);
            bufferedOutput.flush();
            bufferedOutput.close();

            return true;
        } catch (Exception e) {
            System.out.println("file:" + e.getMessage());
            return false;
        }
    }

    public static InputStream base64ToInputStream(String imgStr) {
        // 图像数据为空
        if (StringUtils.isEmpty(imgStr)) {
            return null;
        }

        try {
            // Base64解码
            byte[] byt = Base64.decodeBase64(imgStr);
            for (int i = 0; i < byt.length; ++i) {
                // 调整异常数据
                if (byt[i] < 0) {
                    byt[i] += 256;
                }
            }
            InputStream in = new ByteArrayInputStream(byt);
            return in;
        } catch (Exception e) {
            System.out.println("InputStream error:" + e.getMessage());
            return null;
        }
    }

}
