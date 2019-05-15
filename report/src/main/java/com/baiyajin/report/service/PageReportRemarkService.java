package com.baiyajin.report.service;

import com.baiyajin.entity.bean.PageReportRemark;
import com.baiyajin.entity.bean.ReportRemarkVo;
import com.baiyajin.report.mapper.PageReportRemarkMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PageReportRemarkService extends ServiceImpl<PageReportRemarkMapper, PageReportRemark> implements PageReportRemarkInterface {
    @Override
    public List<ReportRemarkVo> findList(ReportRemarkVo reportRemarkVo) {
        return baseMapper.findList(reportRemarkVo);
    }

    @Override
    public int removeByUserAndReport(String reportId, String userId) {
        return baseMapper.removeByUserAndReport(reportId,userId);
    }
}
