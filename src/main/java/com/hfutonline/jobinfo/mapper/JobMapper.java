package com.hfutonline.jobinfo.mapper;

import com.hfutonline.jobinfo.entity.Job;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author chenliangliang
 * @date: 2017/12/11
 */
@Mapper
@Component
public interface JobMapper {

    int updateInfoById(@Param("info") String info, @Param("id") String id);

    int isExist(@Param("id") String id);

    int save(Job job);

    int count();

    int updateById(Job job);

    int updateClickAndInfo(@Param("click") String click, @Param("info") String info, @Param("id") String id);

    int updateClick(@Param("click") String click,@Param("id") String id);

    String findInfoById(@Param("id") String id);

    List<Job> findJobs();

    List<Job> findEnableJobs();
}
