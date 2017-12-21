package com.hfutonline.jobinfo.service;

import com.github.pagehelper.PageInfo;
import com.hfutonline.jobinfo.entity.Job;

/**
 * @author chenliangliang
 * @date: 2017/12/20
 */
public interface JobService {


    PageInfo<Job> getJobs(int pageNum);

    String getJobInfo(String id);

    PageInfo<Job> getEnableJobs(int pageNum);
}
