package com.guli.vod.service;

import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import com.aliyuncs.vod.model.v20170321.RefreshUploadVideoResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoService {

    //文件上传
    String uploadVideo(MultipartFile file);

    //删除视频
    void removeVideo(String videoId);

    void removeVideoList(List<String> videoIdList);

    //后端获取上传地址和凭证
    CreateUploadVideoResponse getUploadAuthAndAddress(String title , String fileName);

   // 后端刷新上传凭证
    RefreshUploadVideoResponse refreshUploadAuthAndAddress(String videoId);
}
