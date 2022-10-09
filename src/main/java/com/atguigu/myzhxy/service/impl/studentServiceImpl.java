package com.atguigu.myzhxy.service.impl;

import com.atguigu.myzhxy.mapper.studentMapper;
import com.atguigu.myzhxy.pojo.Admin;
import com.atguigu.myzhxy.pojo.LoginForm;
import com.atguigu.myzhxy.pojo.student;
import com.atguigu.myzhxy.service.studentService;
import com.atguigu.myzhxy.util.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("studentService")
@Transactional
public class studentServiceImpl extends ServiceImpl<studentMapper, student> implements studentService {
    @Override
    public student login(LoginForm loginForm) {
        QueryWrapper<student> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("name",loginForm.getUsername());
        queryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));
        student student = baseMapper.selectOne(queryWrapper);
        return student;
    }

    @Override
    public student getStudentById(Long userId) {
        QueryWrapper<student> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("id",userId);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public IPage<student> getStudentByOpr(Page pageParam, student student) {
        QueryWrapper<student> studentQueryWrapper =new QueryWrapper<>();
        if (!StringUtils.isEmpty(student.getName())){
            studentQueryWrapper.like("name",student.getName());
        }
        if (!StringUtils.isEmpty(student.getClazzName())){
            studentQueryWrapper.like("clazz_name",student.getClazzName());
        }
        studentQueryWrapper.orderByDesc("id");
        Page page = baseMapper.selectPage(pageParam, studentQueryWrapper);

        return page;
    }
}
