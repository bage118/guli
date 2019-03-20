package com.guli.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.guli.common.exception.GuliException;
import com.guli.common.vo.R;
import com.guli.vod.util.AliyunVodSDKUtils;
import com.guli.vod.util.ConstantPropertiesUtil;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

@Api(description = "阿里云视频点播微服务")
@CrossOrigin
@RestController
@RequestMapping("/vod/video")
public class VideoController {


    @GetMapping("get-play-auth/{videoId}")
    public R getVideoPlayAuth(@PathVariable("videoId") String videoId){

        try {
            //获取阿里云存储相关常量
            //1、创建客户端，初始化
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(
                    ConstantPropertiesUtil.ACCESS_KEY_ID,
                    ConstantPropertiesUtil.ACCESS_KEY_SECRET);

            //2、创建请求对象
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            request.setVideoId(videoId);

            //3、创建响应对象，发送请求并获取响应结果
            GetVideoPlayAuthResponse response = client.getAcsResponse(request);

            //4、得到播放凭证
            String playAuth = response.getPlayAuth();

            //5、返回结果
            return R.ok().data("playAuth",playAuth).message("获取播放凭证成功");
        } catch (Exception e) {
            throw new GuliException(20001,"获取播放凭证失败");
        }
    }

}
