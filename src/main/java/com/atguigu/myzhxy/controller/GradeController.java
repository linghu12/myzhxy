package com.atguigu.myzhxy.controller;

import com.atguigu.myzhxy.pojo.grade;
import com.atguigu.myzhxy.service.gradeService;
import com.atguigu.myzhxy.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.util.List;
@Api(tags = "年级控制器")
@RestController
@RequestMapping("/sms/gradeController")
public class GradeController {

    @Autowired
    private gradeService gradeService;

    @ApiOperation("获取全部的年级")
    @GetMapping("/getGrades")
    public Result getGrades(){

        List<grade> grades=gradeService.getGrades();

        return Result.ok(grades);
    }



    @ApiOperation("删除Grade信息")
    @DeleteMapping("/deleteGrade")
    public Result deleteGrade(
            @ApiParam("要删除的grade的id的JSON集合") @RequestBody List<Integer> ids){
//        调用服务层
        gradeService.removeByIds(ids);
        return Result.ok();

    }

    @ApiOperation("新增或修改grade")
    @PostMapping("/saveOrUpdateGrade")
    public Result saveOrUpdateGrade(
           @ApiParam("JSON的Grade对象，有id属性是修改") @RequestBody grade grade
    ){

//        接收参数

//        调用服务层方法增加或修改

        gradeService.saveOrUpdate(grade);
        return Result.ok();

    }

    @ApiOperation("根据年纪名称模糊查询 ,带分页")
    @GetMapping("/getGrades/{pageNo}/{pageSize}")
    public Result getGrades(
            @ApiParam("分页查询的页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("分页查询的页大小") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("分页查询模糊匹配的名称") String gradeName
    ){
//        分页 带条件查询

//        封装Result对象返回

        Page<grade> page=new Page<>(pageNo,pageSize);
        IPage<grade> pageRs=gradeService.getGradeByOpr(page,gradeName);


        return Result.ok(pageRs);


    }



}
