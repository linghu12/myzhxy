package com.atguigu.myzhxy.controller;

import com.atguigu.myzhxy.pojo.teacher;
import com.atguigu.myzhxy.service.teacherService;
import com.atguigu.myzhxy.util.MD5;
import com.atguigu.myzhxy.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.util.List;

@RestController
@RequestMapping("/sms/teacherController")
public class TeacherController {

    @Autowired
    private teacherService teacherService;


    @ApiOperation("分页获取教师信息")
    @GetMapping("/getTeachers/{pageNo}/{pageSize}")
    public Result getTeachers(
            @ApiParam("页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("页大小") @PathVariable("pageSize") Integer pageSize,
            teacher teacher

    ){
        Page<teacher> paraParam=new Page<>(pageNo,pageSize);
        IPage<teacher> page=teacherService.getTeachersByOpr(paraParam,teacher);
        return Result.ok(page);

    }

    @ApiOperation("添加或者修改教师信息")
    @PostMapping("/saveOrUpdateTeacher")
    public Result saveOrUpdateTeacher(
            @ApiParam("要保存或者修改的JSON格式的teacher对象")@RequestBody teacher teacher
    ){
//        如果新增 密码加密
        if(teacher.getId()==null||teacher.getId()==0){
            teacher.setPassword(MD5.encrypt(teacher.getPassword()));
        }
        teacherService.saveOrUpdate(teacher);
        return Result.ok();

    }

    @ApiOperation("删除单个或者多个教师信息")
    @DeleteMapping("/deleteTeacher")
    public Result  deleteTeacher(
         @ApiParam("要删除的教师信息id的JSON集合")   @RequestBody List<Integer> ids
    ){
        teacherService.removeByIds(ids);
        return Result.ok();

    }





}
