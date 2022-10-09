package com.atguigu.myzhxy.service;

import com.atguigu.myzhxy.pojo.LoginForm;
import com.atguigu.myzhxy.pojo.student;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface studentService extends IService<student> {
    student login(LoginForm loginForm);

    student getStudentById(Long userId);

    IPage<student> getStudentByOpr(Page pageParam, student student);
}
