package com.guli.edu.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.constants.ResultCodeEnum;
import com.guli.common.exception.GuliException;
import com.guli.common.vo.R;
import com.guli.edu.entity.Teacher;
import com.guli.edu.query.TeacherQuery;
import com.guli.edu.service.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "讲师管理")
@CrossOrigin //跨域
@RestController
@RequestMapping("/admin/edu/teacher")
public class TeacherAdminController
{
    @Autowired
    private TeacherService teacherService;

    @ApiOperation(value = "所有讲师列表")
    @GetMapping
    public R list(){
        List<Teacher> list = teacherService.list(null);
        return  R.ok().data("items",list);
    }

    @ApiOperation(value = "根据id删除讲师")
    @DeleteMapping("{id}")
    public R removeById(
            @ApiParam(name = "id",value = "讲师id",required = true)
            @PathVariable String id){
        boolean result = teacherService.removeById(id);
        if (result){

            return R.ok();
        }else {
            return R.error().message("您删除的记录不存在");
        }
        //return R.setResult(ResultCodeEnum.PARAM_ERROR);
        //return R.ok().message("保存成功").code(8888);
    }

    @ApiOperation(value = "讲师分页列表")
    @GetMapping("{page}/{limit}")
    public R pageList(
            @ApiParam(name = "page",value = "当前页码",required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit",value = "每页记录数",required = true)
            @PathVariable Long limit,
            @ApiParam(name = "teacherQuery",value = "讲师综合查询对象",required = false)
            //如果是PostMapping要加@RequestBody
            TeacherQuery teacherQuery
    ){
        if (page <= 0 || limit <=0){
//            throw new GuliException(21003,"参数错误");写死不好
            throw new GuliException((ResultCodeEnum.PARAM_ERROR));
        }
        Page<Teacher> pageParam = new Page<>(page, limit);
        teacherService.pageQuery(pageParam,teacherQuery);
        List<Teacher> records = pageParam.getRecords();
        long total = pageParam.getTotal();

        return R.ok().data("rows",records).data("total",total);

    }

    //新增
    @ApiOperation(value = "新增讲师")
    @PostMapping
    public R save(
            @ApiParam(name = "teacher",value = "讲师对象",required = true)
            @RequestBody Teacher teacher
    ){
        boolean result = teacherService.save(teacher);
        //result = false;//模拟跳转到失败
        if (result){

        return R.ok();

        }else{
            return R.error().message("保存失败");
        }
    }

    @ApiOperation(value = "根据id查询讲师")
    @GetMapping("{id}")
    public R getById(
            @ApiParam(name = "id",value = "讲师id",required = true)
            @PathVariable String id
    ){
        Teacher teacher = teacherService.getById(id);
        return R.ok().data("item",teacher);
    }

    @ApiOperation(value = "根据id修改讲师")
    @PutMapping("{id}")
    public R updateById(
            @ApiParam(name = "id",value = "讲师id",required = true)
            @PathVariable String id,

            @ApiParam(name = "teacher",value = "讲师对象",required = true)
            @RequestBody Teacher teacher
    ){
        teacher.setId(id);
        teacherService.updateById(teacher);
        return R.ok().data("item",teacher);
    }
}
