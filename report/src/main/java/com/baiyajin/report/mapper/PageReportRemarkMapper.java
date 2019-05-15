package com.baiyajin.report.mapper;

import com.baiyajin.entity.bean.PageReportRemark;
import com.baiyajin.entity.bean.ReportRemarkVo;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

public interface PageReportRemarkMapper extends BaseMapper<PageReportRemark> {
    List<ReportRemarkVo> findList(ReportRemarkVo reportRemarkVo);


    int removeByUserAndReport(@Param("reportId")String reportId,@Param("userId") String userId);
}
