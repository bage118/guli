package com.guli.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.common.constants.PriceConstants;
import com.guli.common.exception.GuliException;
import com.guli.edu.constans.CourseStatusConstants;
import com.guli.edu.entity.Course;
import com.guli.edu.entity.CourseDescription;
import com.guli.edu.form.CourseInfoForm;
import com.guli.edu.mapper.CourseMapper;
import com.guli.edu.query.CourseQuery;
import com.guli.edu.service.ChapterService;
import com.guli.edu.service.CourseDescriptionService;
import com.guli.edu.service.CourseService;
import com.guli.edu.service.VideoService;
import com.guli.edu.vo.CoursePublishVo;
import com.guli.edu.vo.CourseWebVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author Wanba
 * @since 2019-02-23
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Autowired
    private CourseDescriptionService courseDescriptionService;
    @Autowired
    private VideoService videoService;
    @Autowired
    private ChapterService chapterService;

    @Override
    public boolean getCountBySubjectId(String subjectId) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("subject_id",subjectId);
        Integer count = baseMapper.selectCount(queryWrapper);
        return null != count && count > 0;
    }

    @Transactional
    @Override
    public String saveCourseInfo(CourseInfoForm courseInfoForm) {

        //保存课程基本信息
        Course course = new Course();
        course.setStatus(CourseStatusConstants.COURSE_DRAFT);
        BeanUtils.copyProperties(courseInfoForm,course);
        boolean resultCourseInfo = this.save(course);
        if (!resultCourseInfo){
            throw new GuliException(20001,"课程信息保存失败");
        }

        //保存课程详情
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseInfoForm.getDescription());
        courseDescription.setId(course.getId());
        boolean resultDescripition = courseDescriptionService.save(courseDescription);
        if (!resultDescripition){
            throw new GuliException(20001,"课程详情保存失败");
        }
        return course.getId();
    }

    @Override
    public CourseInfoForm getCourseInfoFormById(String id) {

        //1、获取course数据
        Course course = this.getById(id);
        if (course == null){
            throw new GuliException(20001,"数据不存在");
        }
        //2、获取courseDescription数据
        CourseDescription courseDescription = courseDescriptionService.getById(id);
        if (courseDescription == null){
           courseDescription = new CourseDescription();
           courseDescription.setDescription("");
        }
        //3、拼成courseInfoForm数据
        CourseInfoForm courseInfoForm = new CourseInfoForm();
        courseInfoForm.setDescription("");
        BeanUtils.copyProperties(course,courseInfoForm);
        BeanUtils.copyProperties(courseDescription,courseInfoForm);

        //设置显示精度：舍弃多余的位数
        courseInfoForm.setPrice(courseInfoForm.getPrice().setScale(PriceConstants.DISPLAY_SCALE, BigDecimal.ROUND_FLOOR));

        return courseInfoForm;
    }

    @Override
    public void updateCourseInfoById(CourseInfoForm courseInfoForm) {
        //保存课程基本信息
        Course course = new Course();
        //存储
        BeanUtils.copyProperties(courseInfoForm,course);
        boolean resultCouseInfo = this.updateById(course);
        if (!resultCouseInfo){
            throw new GuliException(20001,"课程信息保存失败");
        }
        //保存课程详情信息
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseInfoForm.getDescription());
        //上面已经从courseInfoForm复制到了course，即拿到了id
        courseDescription.setId(course.getId());
        boolean resultDescription = courseDescriptionService.updateById(courseDescription);
        if (!resultDescription){
            //throw new GuliException(20001,"课程详情信息保存失败");
            courseDescriptionService.save(courseDescription);
        }

    }

    @Override
    public void pageQuery(Page<Course> pageParam, CourseQuery courseQuery) {

        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_create");

        if (courseQuery == null){
            baseMapper.selectPage(pageParam,queryWrapper);
            return;
        }

        String title = courseQuery.getTitle();
        String teacherId = courseQuery.getTeacherId();
        String subjectParentId = courseQuery.getSubjectParentId();
        String subjectId = courseQuery.getSubjectId();

        if (!StringUtils.isEmpty(title)){
            queryWrapper.like("title",title);
        }

        if (!StringUtils.isEmpty(teacherId)){
            queryWrapper.eq("teacher_id",teacherId);
        }

        if (!StringUtils.isEmpty(subjectParentId)){
            queryWrapper.ge("subject_parent_id",subjectParentId);
        }

        if (!StringUtils.isEmpty(subjectId)){
            queryWrapper.ge("subject_id",subjectId);
        }

        baseMapper.selectPage(pageParam,queryWrapper);
    }

    @Transactional
    @Override
    public boolean removeCourseById(String id) {

        //根据id删除所有视频
        videoService.removeByCourseId(id);

        //根据id删除所有章节
        chapterService.removeByCourseId(id);

        //根据id删除视频详情表
        courseDescriptionService.removeById(id);

        //删除封面 TODO 独立完成

        //根据id删除课时
        Integer result = baseMapper.deleteById(id);
        return null != result && result > 0;
    }

    @Override
    public CoursePublishVo getCoursePublishInfoById(String id) {
        return baseMapper.selectCoursePublishInfoById(id);
    }

    @Override
    public boolean publishCourse(String id) {

        Course course = new Course();
        course.setId(id);
        course.setStatus(CourseStatusConstants.COURSE_NORMAL);

        Integer count = baseMapper.updateById(course);
        return null != count && count > 0;
    }

    //根据讲师id查询当前讲师的课程列表
    @Override
    public List<Course> selectByTeacherId(String teacherId) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("teacher_id",teacherId);
        //按照最后更新的时间倒序排列
        queryWrapper.orderByDesc("gmt_modified");

        List<Course> courses = baseMapper.selectList(queryWrapper);

        return courses;
    }

    @Override
    public Map<String, Object> pageListWeb(Page<Course> pageParam) {

        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_modified");

        baseMapper.selectPage(pageParam,queryWrapper);

        List<Course> records = pageParam.getRecords();
        long current = pageParam.getCurrent();
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
        boolean hasNext = pageParam.hasNext();
        boolean hasPrevious = pageParam.hasPrevious();

        Map<String, Object> map = new HashMap<>();
        map.put("items",records);
        map.put("current",current);
        map.put("pages",pages);
        map.put("size",size);
        map.put("total",total);
        map.put("hasNext",hasNext);
        map.put("hasPrevious",hasPrevious);

        return map;
    }

    //获取课程信息
    @Transactional
    @Override
    public CourseWebVo selectInfoWebById(String id) {
        this.updatePageViewCount(id);
        return baseMapper.selectInfoWebById(id);
    }

    //更新课程浏览量
    @Override
    public void updatePageViewCount(String id) {

        Course course = baseMapper.selectById(id);
        course.setViewCount(course.getViewCount()+1);
        baseMapper.updateById(course);
    }

    /*@Autowired
    private CourseMapper courseMapper;

    @Override
    public CoursePublishVo getCoursePublishInfoById(String id) {
        CoursePublishVo coursePublishVo = courseMapper.getCoursePublishInfoById(id);
        return coursePublishVo;
    }

    @Override
    public void publishCourse(String id) {
        Course course = courseMapper.selectById(id);
        if(CourseStatusConstants.COURSE_NORMAL.equals(course.getStatus())){
            throw new GuliException(20001, "课程已发布，不要重复发布");
        }
        course.setStatus(CourseStatusConstants.COURSE_NORMAL);
        courseMapper.updateById(course);
    }*/

}
