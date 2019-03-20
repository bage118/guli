package com.guli.edu.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.vo.R;
import com.guli.edu.entity.Course;
import com.guli.edu.service.ChapterService;
import com.guli.edu.service.CourseService;
import com.guli.edu.vo.ChapterVo;
import com.guli.edu.vo.CourseWebVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author Wanba
 * @since 2019-02-23
 */
@Api(description = "课程模块")
@CrossOrigin
@RestController
@RequestMapping("/edu/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private ChapterService chapterService;

    @ApiOperation(value = "分页列表课程")
    @GetMapping("{page}/{limit}")
    public R pageList(
            @ApiParam(name = "page",value = "当前页码",required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit",value = "每页记录数",required = true)
            @PathVariable Long limit){

        Page<Course> pageParam = new Page<>(page, limit);
        Map<String,Object> map = courseService.pageListWeb(pageParam);

        return R.ok().data(map);
    }

    @ApiOperation(value = "根据id查询课程")
    @GetMapping("{courseId}")
    public R getByid(
            @ApiParam(name = "courseId",value = "课程id",required = true)
            @PathVariable String courseId){

        //查询课程信息和讲师信息
        CourseWebVo courseWebVo = courseService.selectInfoWebById(courseId);

        //查询当前课程的章节信息
        List<ChapterVo> chapterVoList = chapterService.nestedList(courseId);

        return R.ok().data("course",courseWebVo).data("chapterVoList",chapterVoList);
    }
}

