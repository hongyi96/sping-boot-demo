package com.example.facedemo.face.utils;


import com.baidu.aip.face.AipFace;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * @author: hongyi
 * @date: 2019/8/8 14:58
 */
public class FaceUtils {
    public static final String APP_ID = "16973641";
    public static final String API_KEY = "v6LpoUl6dvwQMLQP5xbcuIGw";
    public static final String SECRET_KEY = "lDhw21C02uTCol1EvVpFws7mZ5cCtm9v";
    private static final AipFace client = new AipFace(APP_ID, API_KEY, SECRET_KEY);
    private static final String GROUPID_LIST = "test_users";


    private FaceUtils() {
    }

    private static class FaceUtilsHolder {
        private static final FaceUtils INSTANCE = new FaceUtils();

    }


    public static FaceUtils getInstance() {
        return FaceUtilsHolder.INSTANCE;
    }

    public static JSONObject checkFace(String image) {

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        /*// 可选：设置代理服务器地址, http和socket二选一，或者均不设置
        // 设置http代理
        client.setHttpProxy("proxy_host", proxy_port);
        // 设置socket代理
        client.setSocketProxy("proxy_host", proxy_port);*/

        //调用接口
        String imageType = "BASE64";
        HashMap<String, String> options = new HashMap<String, String>(8);
        options.put("face_field", "age,beauty,expression,gender");
        options.put("max_face_num", "2");
        options.put("face_type", "LIVE");
        options.put("liveness_control", "LOW");

        // 人脸检测
        JSONObject res = client.detect(image, imageType, options);

        System.out.println(res.toString(2));
        return res;
    }

    public static JSONObject searchFace(String image, String groupId) {
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>(8);
        options.put("max_face_num", "1");
        options.put("match_threshold", "70");
        options.put("quality_control", "NORMAL");
        options.put("liveness_control", "LOW");
        options.put("max_user_num", "1");

        String imageType = "BASE64";
        if (groupId == null) {
            groupId = GROUPID_LIST;
        }

        // 人脸搜索
        JSONObject res = client.search(image, imageType, groupId, options);
        System.out.println(res.toString(2));
        return res;
    }

    public static JSONObject addFace(String image, String userId, String groupId) {
        HashMap<String, String> options = new HashMap<String, String>(16);
        options.put("user_info", "user's info");
        options.put("quality_control", "NORMAL");
        options.put("liveness_control", "LOW");
        options.put("action_type", "REPLACE");
        String imageType = "BASE64";

        // 人脸注册
        JSONObject res = client.addUser(image, imageType, groupId, userId, options);
        return res;

    }


}
