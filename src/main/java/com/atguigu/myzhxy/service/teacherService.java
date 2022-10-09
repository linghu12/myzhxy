package com.atguigu.myzhxy.service;

import com.atguigu.myzhxy.mapper.TeacherMapper;
import com.atguigu.myzhxy.pojo.LoginForm;
import com.atguigu.myzhxy.pojo.teacher;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface teacherService extends IService<teacher> {
    teacher login(LoginForm loginForm);

    teacher getteacherById(Long userId);

    IPage<teacher> getTeachersByOpr(Page<teacher> paraParam, teacher teacher);
}
