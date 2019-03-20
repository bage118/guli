package com.guli.edu.service;

import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.edu.entity.Video;
import com.guli.edu.form.VideoInfoForm;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author Wanba
 * @since 2019-02-23
 */
public interface VideoService extends IService<Video> {

    boolean getCountByChapterId(String chapterId);

    void saveVideoInfo(VideoInfoForm videoInfoForm);

    VideoInfoForm getVideoInfoFormById(String id);

    void updateVideoInfoFormById(VideoInfoForm videoInfoForm);

    boolean removeByCourseId(String courseId);

    boolean removeVideoById(String id);


}
