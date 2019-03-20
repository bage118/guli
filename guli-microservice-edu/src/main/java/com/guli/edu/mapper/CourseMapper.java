package com.guli.edu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guli.edu.entity.Course;
import com.guli.edu.vo.CoursePublishVo;
import com.guli.edu.vo.CourseWebVo;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author Wanba
 * @since 2019-02-23
 */
//@Repository
public interface CourseMapper extends BaseMapper<Course> {

//    @Select("SELECT c.id,c.`cover`,c.price,\n" +
//            "c.lesson_num AS 'lessonNum',c.title,\n" +
//            "l1.`title` AS 'subjectLevelOne',\n" +
//            "l2.`title` AS 'subjectLevelTwo',\n" +
//            "t.`name` AS 'teacherName' \n" +
//            "FROM edu_course c,edu_subject l1,edu_subject l2,edu_teacher t \n" +
//            "WHERE c.`subject_parent_id` = l1.`id` \n" +
//            "AND c.`subject_id` = l2.`id` \n" +
//            "AND c.`teacher_id` = t.`id` \n" +
//            "AND c.`id`=#{courseId}")
//    public CoursePublishVo getCoursePublishInfoById(String id);
        CoursePublishVo selectCoursePublishInfoById(String id);

        CourseWebVo selectInfoWebById(String courseId);
}
