package com.atguigu.myzhxy.controller;

import com.atguigu.myzhxy.pojo.Admin;
import com.atguigu.myzhxy.pojo.LoginForm;
import com.atguigu.myzhxy.pojo.student;
import com.atguigu.myzhxy.pojo.teacher;
import com.atguigu.myzhxy.service.AdminService;
import com.atguigu.myzhxy.service.studentService;
import com.atguigu.myzhxy.service.teacherService;
import com.atguigu.myzhxy.util.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/sms/system")
public class SystemController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private studentService studentService;
    @Autowired
    private teacherService teacherService;


    @ApiOperation("更新用户密码的方法")
    @PostMapping("/updatePwd/{oldPwd}/{newPwd}")
    public Result updatePwd(
            @ApiParam("token口令") @RequestHeader("token") String token,
            @ApiParam("旧密码") @PathVariable("oldPwd") String oldPwd,
            @ApiParam("新密码") @PathVariable("newPwd") String newPwd
    ){
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration){
            return Result.fail().message("token失效，请重新登录后修改密码");
        }

        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        oldPwd= MD5.encrypt(oldPwd);
        newPwd=MD5.encrypt(newPwd);
        switch (userType){
            case 1:
                QueryWrapper<Admin> queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("id",userId.intValue());
                queryWrapper.eq("password",oldPwd);
                Admin admin=adminService.getOne(queryWrapper);
                if (admin!=null){
                    admin.setPassword(newPwd);
                    adminService.saveOrUpdate(admin);
                }else {
                    return Result.fail().message("原密码有误");
                }
                break;
            case 2:
                QueryWrapper<student> queryWrapper2=new QueryWrapper<>();
                queryWrapper2.eq("id",userId.intValue());
                queryWrapper2.eq("password",oldPwd);
                student student=studentService.getOne(queryWrapper2);
                if (student!=null){
                    student.setPassword(newPwd);
                    studentService.saveOrUpdate(student);
                }else {
                    return Result.fail().message("原密码有误");
                }
                break;
            case 3:
                QueryWrapper<teacher> queryWrapper3=new QueryWrapper<>();
                queryWrapper3.eq("id",userId.intValue());
                queryWrapper3.eq("password",oldPwd);
                teacher teacher=teacherService.getOne(queryWrapper3);
                if (teacher!=null){
                    teacher.setPassword(newPwd);
                    teacherService.saveOrUpdate(teacher);
                }else {
                    return Result.fail().message("原密码有误");
                }
                break;

        }
        return Result.ok();
    }


    @ApiOperation("文件上传统一路口")
    @PostMapping("/headerImgUpload")
    public Result headerImgUpload(
            @ApiParam("要上传的头像文件") @RequestPart("multipartFile") MultipartFile multipartFile
,           HttpServletRequest request
            ){
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase(Locale.ROOT);
        String originalFilename = multipartFile.getOriginalFilename();
        int i=originalFilename.lastIndexOf(".");
        String newFilename =uuid + originalFilename.substring(i);


//        保存文件
        String portaitPath="E:/myzhxy/target/classes/public/upload/".concat(newFilename);

        try {
            multipartFile.transferTo(new File(portaitPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String path="upload/".concat(newFilename);

        return Result.ok(path);

    }



    @GetMapping("/getInfo")
    public Result getInfoByToken(@RequestHeader("token") String token){
        boolean expiration = JwtHelper.isExpiration(token);

        if (expiration){
            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }
//        从token中解析出用户的id和用户的类型
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        Map<String,Object> map=new LinkedHashMap<>();
        switch(userType){
            case 1:
                Admin admin=adminService.getAdminById(userId);
                map.put("userType",1);
                map.put("user",admin);
                break;
            case 2:
                student student=studentService.getStudentById(userId);
                map.put("userType",2);
                map.put("user",student);
                break;
            case 3:
                teacher teacher=teacherService.getteacherById(userId);
                map.put("userType",3);
                map.put("user",teacher);
                break;
        }

        return Result.ok(map);
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginForm loginForm,HttpServletRequest request){
        //验证码问题
        HttpSession session = request.getSession();
        String verifiCode = (String)session.getAttribute("verifiCode");
        String loginFormVerifiCode = loginForm.getVerifiCode();
        if("".equals(verifiCode)||null==verifiCode){
            return Result.fail().message("验证码失效，请刷新后重试");
        }
        if(!verifiCode.equalsIgnoreCase(loginFormVerifiCode)){
            return Result.fail().message("验证码有误，请小心输入后重试");
        }
        //从session域移除现有验证码
        session.removeAttribute("verifiCode");
        //分用户类型


        //准备一个map用户存放响应的数据
        Map<String,Object> map=new LinkedHashMap<>();
        switch(loginForm.getUserType()){
            case 1:
                try {
                    Admin admin= adminService.login(loginForm);
                    if (null!=admin) {
                        //用户类型和用户id转换成一个密文 以tokeen的名称向客户端反馈
                        String token = JwtHelper.createToken(admin.getId().longValue(), 1);
                        map.put("token",token);

                    }else{
                        throw new RuntimeException("用户名或者密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }

            case 2:
                try {
                    student student= studentService.login(loginForm);
                    if (null!=student) {
                        //用户类型和用户id转换成一个密文 以tokeen的名称向客户端反馈
                        String token = JwtHelper.createToken(student.getId().longValue(), 2);
                        map.put("token",token);

                    }else{
                        throw new RuntimeException("用户名或者密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 3:
                try {
                    teacher teacher= teacherService.login(loginForm);
                    if (null!=teacher) {
                        //用户类型和用户id转换成一个密文 以tokeen的名称向客户端反馈
                        String token = JwtHelper.createToken(teacher.getId().longValue(), 3);
                        map.put("token",token);

                    }else{
                        throw new RuntimeException("用户名或者密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
        }
        return Result.fail().message("查无此用户");

    }


    @GetMapping("/getVerifiCodeImage")
    public void getVerifiCodeImage(HttpServletRequest request, HttpServletResponse response){
        //获取图片
        BufferedImage verifiCodeImage = CreateVerifiCodeImage.getVerifiCodeImage();
        //获取图片上的验证码
        String verifiCode = new String(CreateVerifiCodeImage.getVerifiCode());
        //将验证码文本放入session域，为下一次验证做准备
        HttpSession session = request.getSession();
        session.setAttribute("verifiCode",verifiCode);
        //将验证码图片响应给浏览器]
        try {
            ImageIO.write(verifiCodeImage,"JPEG",response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
