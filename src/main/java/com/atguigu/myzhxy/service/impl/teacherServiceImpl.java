package com.atguigu.myzhxy.service.impl;


import com.atguigu.myzhxy.mapper.TeacherMapper;
import com.atguigu.myzhxy.pojo.Admin;
import com.atguigu.myzhxy.pojo.LoginForm;
import com.atguigu.myzhxy.pojo.teacher;
import com.atguigu.myzhxy.service.teacherService;
import com.atguigu.myzhxy.util.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysql.cj.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Queue;

@Service("teacherService")
@Transactional
public class teacherServiceImpl extends ServiceImpl<TeacherMapper, teacher> implements teacherService {
    @Override
    public teacher login(LoginForm loginForm) {
        QueryWrapper<teacher> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("name",loginForm.getUsername());
        queryWrapper.eq("password", MD5.encrypt(loginForm.getPassword()));
        teacher teacher = baseMapper.selectOne(queryWrapper);
        return teacher;
    }

    @Override
    public teacher getteacherById(Long userId) {
        QueryWrapper<teacher> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("id",userId);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public IPage<teacher> getTeachersByOpr(Page<teacher> paraParam, teacher teacher) {
        QueryWrapper<teacher> queryWrapper=new QueryWrapper<>();
        if (!ObjectUtils.isEmpty(teacher.getName())){
            queryWrapper.like("name",teacher.getName());
        }
        if(!ObjectUtils.isEmpty(teacher.getClazzName())){
            queryWrapper.eq("clazz_name",teacher.getClazzName());
        }
        queryWrapper.orderByDesc("id");

        Page page=baseMapper.selectPage(paraParam,queryWrapper);
        return  page;



    }
}
