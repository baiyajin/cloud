package com.baiyajin.report.service;

import com.baiyajin.entity.bean.PageReportRemark;
import com.baiyajin.entity.bean.ReportRemarkVo;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

public interface PageReportRemarkInterface extends IService<PageReportRemark> {

    List<ReportRemarkVo> findList(ReportRemarkVo reportRemarkVo);

    int removeByUserAndReport(String reportId,String userId);
}
