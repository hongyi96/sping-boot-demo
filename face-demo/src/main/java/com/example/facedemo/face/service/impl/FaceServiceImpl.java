package com.example.facedemo.face.service.impl;

import com.example.facedemo.face.service.FaceService;
import com.example.facedemo.face.utils.FaceUtils;
import com.example.facedemo.face.utils.ImageUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

/**
 * @author: hongyi
 * @date: 2019/8/15 9:35
 */
@Service
public class FaceServiceImpl implements FaceService {

    @Override
    public boolean checkSmile(String image) {
        boolean flag = false;
        String url = ImageUtils.image2Base64(image);
        JSONObject res = FaceUtils.checkFace(url);
        try {
            String type = res.getJSONObject("result").getJSONArray("face_list").getJSONObject(0)
                    .getJSONObject("expression").getString("type");
            flag = ("smile".equals(type) || "laugh".equals(type));
        } catch (JSONException e) {
            return false;
        }
        return flag;
    }

    @Override
    public Integer searchFace(String image) {
        Integer userId;
        String url = ImageUtils.image2Base64(image);
        JSONObject res = FaceUtils.searchFace(url, null);
        try {
            JSONObject result = res.getJSONObject("result");
            userId = result.getJSONArray("user_list").getJSONObject(0).getInt("user_id");
        } catch (JSONException e) {
            return null;
        }
        return userId;
    }

    @Override
    public boolean addFace(String image, String userId, String groupId) {
        String url = ImageUtils.image2Base64(image);
        JSONObject res = FaceUtils.addFace(url, userId, groupId);
        try {
            JSONObject result = res.getJSONObject("result");
            return result != null;

        } catch (JSONException e) {
            return false;
        }
    }
}