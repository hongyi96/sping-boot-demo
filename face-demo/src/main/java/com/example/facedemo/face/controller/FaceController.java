package com.example.facedemo.face;

import com.example.facedemo.face.entity.Image;
import com.example.facedemo.face.service.FaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: hongyi
 * @date: 2019/8/8 16:02
 */

@RestController
@RequestMapping("faces")
public class FaceController {
    @Autowired
    private FaceService faceService;


    @RequestMapping(method = RequestMethod.POST)
    public Map<String, Object> checkSmile(@RequestBody Image image,
                                          @RequestParam String userId,
                                          @RequestParam String groupId) {
        Map<String, Object> result = new HashMap<>(2);
        boolean flag = faceService.addFace(image.getImage(), userId, groupId);
        if (flag) {
            result.put("result", "success");
        } else {
            result.put("error", "fail");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Map<String, Object> searchFace(@RequestBody Image image) {
        Map<String, Object> result = new HashMap<>(2);
        Integer userId = faceService.searchFace(image.getImage());
        if (null != userId) {
            result.put("userId", userId);
        } else {
            result.put("error", "no such user");
        }
        return result;
    }

    @RequestMapping(value = "check", method = RequestMethod.POST)
    public Map<String, Object> checkSmile(@RequestBody Image image) {
        Map<String, Object> result = new HashMap<>(2);
        boolean flag = faceService.checkSmile(image.getImage());
        if (flag) {
            result.put("result", "success");
        } else {
            result.put("error", "fail");
        }
        return result;
    }

}
