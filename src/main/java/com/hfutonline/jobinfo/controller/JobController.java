package com.hfutonline.jobinfo.controller;

import com.github.pagehelper.PageInfo;
import com.hfutonline.jobinfo.crawler.Crawler;
import com.hfutonline.jobinfo.entity.Job;
import com.hfutonline.jobinfo.mapper.JobMapper;
import com.hfutonline.jobinfo.service.JobService;
import com.hfutonline.jobinfo.utils.Result;
import com.hfutonline.jobinfo.utils.ResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

/**
 * @author chenliangliang
 * @date 2017/12/20
 */
@RestController
@RequestMapping(value = "/job")
public class JobController {

    private JobService jobService;

    private JobMapper jobMapper;

    protected JobController(JobService jobService, JobMapper jobMapper) {
        this.jobService = jobService;
        this.jobMapper = jobMapper;
    }




    @GetMapping("/info/{id}")
    @CrossOrigin(origins = "*",methods = RequestMethod.GET)
    public Result getJobInfo(@PathVariable(value = "id") String id) {
        Job info = jobService.getJobInfo(id);
        if (info == null) {
            return ResultUtil.fail("id错误");
        }
        return ResultUtil.OK(info);
    }

    @GetMapping("/{pageNum}")
    @CrossOrigin(origins = "*",methods = RequestMethod.GET)
    public Result getJobs(@PathVariable(value = "pageNum") int pageNum) {
        if (pageNum <= 0) {
            pageNum = 1;
        }
        PageInfo<Job> jobPageInfo = jobService.getJobs(pageNum);
        return ResultUtil.OK(jobPageInfo);
    }

    @GetMapping("/control/{user}/{pwd}")
    @CacheEvict(value = {"read","test"})
    public Result crawler(@PathVariable("user") String user,
                          @PathVariable("pwd") String pwd) {
        if (!(StringUtils.equals(user, "admin") && StringUtils.equals(pwd, "hfut_online"))) {
            return ResultUtil.fail("权限不足!");
        }
        try {
            Crawler crawler=new Crawler(jobMapper);
            crawler.run();
            int up=crawler.getUpdate();
            int ins=crawler.getInsert();
            return ResultUtil.OK("插入 "+ins+" 条数据，更新 "+up+" 条数据");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }

    }


    @GetMapping("/enable/{pageNum}")
    @CrossOrigin(origins = "*",methods = RequestMethod.GET)
    public Result getEnableJobs(@PathVariable(value = "pageNum") int pageNum) {
        if (pageNum <= 0) {
            pageNum = 1;
        }
        PageInfo<Job> jobPageInfo = jobService.getEnableJobs(pageNum);
        return ResultUtil.OK(jobPageInfo);
    }

}
