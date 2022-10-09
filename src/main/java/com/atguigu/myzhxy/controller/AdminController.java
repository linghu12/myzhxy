package com.atguigu.myzhxy.controller;


import com.atguigu.myzhxy.pojo.Admin;
import com.atguigu.myzhxy.service.AdminService;
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
@RequestMapping("/sms/adminController")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @ApiOperation("分页带条件查询管理员信息")
    @GetMapping("/getAllAdmin/{pageNo}/{pageSize}")
    public Result getAllAdmin(
            @ApiParam("页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("页大小") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("管理员名字") String adminName
            ){

        Page<Admin> pageParam=new Page<>(pageNo,pageSize);

        IPage<Admin> iPage=adminService.getAdminsByOpr(pageParam,adminName);

        return Result.ok(iPage);
    }


    @ApiOperation("增加或者修改管理员信息")
    @PostMapping("/saveOrUpdateAdmin")
    public Result saveOrUpdateAdmin(
            @ApiParam("JSON格式的Admin对象")@RequestBody Admin admin
    ){
        Integer id = admin.getId();
        if(id==null||0==id){
            admin.setPassword(MD5.encrypt(admin.getPassword()));
        }
        adminService.saveOrUpdate(admin);
        return Result.ok();

    }


    @DeleteMapping("/deleteAdmin")
    public Result deleteAdmin(
        @ApiParam("要删除的管理员的多个ID的JSON集合") @RequestBody List<Integer> ids
    ){
        adminService.removeByIds(ids);
        return Result.ok();
    }



}
