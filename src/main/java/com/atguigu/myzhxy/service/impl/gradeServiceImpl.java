package com.atguigu.myzhxy.service.impl;

import com.atguigu.myzhxy.mapper.gradeMapper;
import com.atguigu.myzhxy.pojo.grade;
import com.atguigu.myzhxy.service.gradeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysql.cj.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class gradeServiceImpl extends ServiceImpl<gradeMapper, grade> implements gradeService {
    @Override
    public IPage<grade> getGradeByOpr(Page<grade> pageParam, String gradeName) {
        QueryWrapper<grade> queryWrapper=new QueryWrapper();
        if (!ObjectUtils.isEmpty(gradeName)) {
            queryWrapper.like("name",gradeName);
        }
        queryWrapper.orderByDesc("id");

        Page<grade> page = baseMapper.selectPage(pageParam, queryWrapper);
        return page;


    }

    @Override
    public List<grade> getGrades() {

        List<grade> grades = baseMapper.selectList(null);

        return grades;
    }
}
