package com.hfutonline.jobinfo.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hfutonline.jobinfo.entity.Job;
import com.hfutonline.jobinfo.mapper.JobMapper;
import com.hfutonline.jobinfo.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author chenliangliang
 * @date 2017/12/20
 */
@Service
public class JobServiceImpl implements JobService {


    private JobMapper jobMapper;

    @Autowired
    protected JobServiceImpl(JobMapper jobMapper) {
        this.jobMapper = jobMapper;
    }

    @Override
    @Cacheable(value = "read",key = "'getJobs_'+#pageNum")
    public PageInfo<Job> getJobs(int pageNum) {
        System.out.println("pageNum: "+pageNum);
        PageHelper.startPage(pageNum, 20);
        List<Job> jobs = jobMapper.findJobs();
        Date date = new Date();
        jobs.forEach(it ->
                it.setExpired(it.getDate().before(date))
        );
        return new PageInfo<>(jobs);
    }

    @Override
    @Cacheable(value = "info",key = "'getJobInfo'+#id")
    public String getJobInfo(String id) {
        return jobMapper.findInfoById(id);
    }

    @Override
    @Cacheable(value = "test",key = "'getEnableJobs'+#pageNum")
    public PageInfo<Job> getEnableJobs(int pageNum) {
        PageHelper.startPage(pageNum,20);
        List<Job> jobs=jobMapper.findEnableJobs();
        return new PageInfo<>(jobs);
    }
}
