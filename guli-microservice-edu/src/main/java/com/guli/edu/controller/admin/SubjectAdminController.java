package com.guli.edu.controller.admin;

import com.guli.common.vo.R;
import com.guli.edu.entity.Subject;
import com.guli.edu.service.SubjectService;
import com.guli.edu.vo.SubJectNestedVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api(description = "课程分类管理")
@CrossOrigin //跨域
@RestController
@RequestMapping("/admin/edu/subject")
public class SubjectAdminController {

    @Autowired
    private SubjectService subjectService;

    @ApiOperation(value = "Excel批量导入")
    @PostMapping("import")
    public R addUser(
            @ApiParam(name = "file", value = "Excel文件", required = true)
            @RequestParam("file") MultipartFile file) throws Exception {

        List<String> msg = subjectService.batchImport(file);
        if(msg.size() == 0){
            return R.ok().message("批量导入成功");
        }else{
            return R.error().message("部分数据导入失败").data("messageList", msg);
        }
    }

    @ApiOperation(value = "获取嵌套数据列表")
    @GetMapping
    public R nestedList(){

        List<SubJectNestedVo> subJectNestedVoList =  subjectService.nestedList();
        return R.ok().data("items",subJectNestedVoList);
    }

    @ApiOperation(value = "根据id删除类别")
    @DeleteMapping("{id}")
    public R removeById(
            @ApiParam(name = "id",value = "类别id",required = true)
            @PathVariable String id) {
        boolean result = subjectService.removeSubjectById(id);
        if (result) {
            return R.ok();
        } else {
            return R.error().message("您删除的记录不存在");
        }
    }

    @ApiOperation(value = "保存一级分类")
    @PostMapping("save-level-one")
    public  R saveLevelOne(
            @ApiParam(name = "subject",value = "课程分类对象",required = true)
            @RequestBody Subject subject){

        boolean result = subjectService.saveLevelOne(subject);
        if (result) {
            return R.ok();
        } else {
            return R.error().message("保存失败");
        }
    }

    @ApiOperation(value = "新增二级分类")
    @PostMapping("save-level-two")
    public R saveLevelTwo(
            @ApiParam(name = "subject", value = "课程分类对象", required = true)
            @RequestBody Subject subject){

        boolean result = subjectService.saveLevelTwo(subject);
        if(result){
            return R.ok();
        }else{
            return R.error().message("保存失败");
        }
    }
}
