package com.guli.edu.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.vo.R;
import com.guli.edu.entity.Course;
import com.guli.edu.form.CourseInfoForm;
import com.guli.edu.query.CourseQuery;
import com.guli.edu.service.CourseService;
import com.guli.edu.service.SubjectService;
import com.guli.edu.service.TeacherService;
import com.guli.edu.vo.CoursePublishVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "课程管理")
@CrossOrigin //跨域
@RestController
@RequestMapping("/admin/edu/course")
public class CourseAdminController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private SubjectService subjectService;

    @ApiOperation(value = "新增课程")
    @PostMapping("save-course-info")
    public R saveCourseInfoForm(
            @ApiParam(name = "courseInfoForm", value = "课程基本信息表单对象", required = true)
            @RequestBody CourseInfoForm courseInfoForm) {

        String courseId = courseService.saveCourseInfo(courseInfoForm);
        if (!StringUtils.isEmpty(courseId)) {
            return R.ok().data("courseId", courseId);
        } else {
            return R.error().message("保存失败");
        }
    }

    @ApiOperation(value = "根据id查询课程")
    @GetMapping("course-info/{id}")
    public R getById(
            @ApiParam(name = "id", value = "课程id", required = true)
            @PathVariable String id) {


        CourseInfoForm courseInfoForm = courseService.getCourseInfoFormById(id);
        return R.ok().data("item", courseInfoForm);
    }

    @ApiOperation(value = "更新课程")
    @PutMapping("update_course_in/{id}")
    public R updateCourseInfoForm(
            @ApiParam(name = "CourseInfoForm", value = "课程基本信息", required = true)
            @RequestBody CourseInfoForm courseInfoForm,

            @ApiParam(name = "id", value = "课程ID", required = true)
            @PathVariable String id) {

        courseService.updateCourseInfoById(courseInfoForm);

        return R.ok();
    }

    @ApiOperation(value = "分页课程列表")
    @GetMapping("{page}/{limit}")
    public R pageQuery(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            @ApiParam(name = "courseQuery", value = "查询对象", required = false)
                    CourseQuery courseQuery) {

        Page<Course> pageParam = new Page<>(page, limit);

        courseService.pageQuery(pageParam, courseQuery);
        List<Course> records = pageParam.getRecords();

        long total = pageParam.getTotal();

        return R.ok().data("total", total).data("rows", records);
    }

    @ApiOperation(value = "根据ID删除课程")
    @DeleteMapping("{id}")
    public R removeById(
            @ApiParam(name = "id", value = "课程ID", required = true)
            @PathVariable String id) {

        boolean result = courseService.removeCourseById(id);
        if (result) {
            return R.ok();
        } else {
            return R.error().message("删除失败");
        }
    }


    @ApiOperation(value = "根据课程id获取课程发布基本信息")
    @GetMapping("course-publish-info/{id}")
    public R getCoursePublishById(
            @ApiParam(name = "id", value = "课程ID", required = true)
            @PathVariable String id) {

        CoursePublishVo coursePublishVo = courseService.getCoursePublishInfoById(id);
        return R.ok().data("item", coursePublishVo);
    }

    @ApiOperation(value="根据课程id,发布课程")
    @PutMapping("publish-course/{id}")
    public R publishCourse(
            @ApiParam(name = "id", value = "课程ID", required = true)
            @PathVariable String id){
        courseService.publishCourse(id);
        return R.ok().message("课程发布成功");
    }
}
