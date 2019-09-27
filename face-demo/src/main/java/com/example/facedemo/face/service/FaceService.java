package com.example.facedemo.face.service;

/**
 * @author: hongyi
 * @date: 2019/8/15 9:24
 */
public interface FaceService {
    /**
     * 符合要求(笑的)的人脸图片
     *
     * @param imageUrl
     * @return
     */
    boolean checkSmile(String imageUrl);

    /**
     * 检测是否是用户的人脸图片
     *
     * @param imageUrl
     * @return
     */
     Integer searchFace(String imageUrl);

    /**
     * 添加用户人脸图片
     *
     * @param image
     * @param userId
     * @param groupId
     * @return
     */
     boolean addFace(String image, String userId, String groupId) ;

}
