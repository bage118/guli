package com.guli.edu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.edu.entity.Course;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.edu.form.CourseInfoForm;
import com.guli.edu.query.CourseQuery;
import com.guli.edu.vo.CoursePublishVo;
import com.guli.edu.vo.CourseWebVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author Wanba
 * @since 2019-02-23
 */
public interface CourseService extends IService<Course> {

    boolean getCountBySubjectId(String subjectId);

    String saveCourseInfo(CourseInfoForm courseInfoForm);

    CourseInfoForm getCourseInfoFormById(String id);

    void updateCourseInfoById(CourseInfoForm courseInfoForm);

    void pageQuery(Page<Course> pageParam, CourseQuery courseQuery);


    boolean removeCourseById(String id);

    CoursePublishVo getCoursePublishInfoById(String id);

    boolean publishCourse(String id);

    List<Course> selectByTeacherId(String teacherId);

    Map<String,Object> pageListWeb(Page<Course> pageParam);

    //获取课程信息
    CourseWebVo selectInfoWebById(String id);

    //更新课程浏览量
    void updatePageViewCount(String id);
}
