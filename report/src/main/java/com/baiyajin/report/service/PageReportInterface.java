package com.baiyajin.report.service;

import com.baiyajin.entity.bean.DataTempVo;
import com.baiyajin.entity.bean.Page;
import com.baiyajin.entity.bean.PageReport;
import com.baiyajin.entity.bean.ReportVo;
import com.baiyajin.util.u.Results;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;
import java.util.Map;

public interface PageReportInterface extends IService<PageReport> {

    Page<ReportVo> findList(Page<ReportVo> page, ReportVo reportVo);

    int getCount(ReportVo reportVo);

    int updateRemark(Map<String, Object> map);

    Map<String,Object> selectRemarkByReportId(Map<String, Object> map);

    int addRemark(Map<String, Object> map);

    PageReport selectRemark(Map<String, Object> map);

    Results getReportInfoById(String id);

    List<DataTempVo> findDataByReportId(DataTempVo dataTempVo);

    String getMaName(String id);

    int getTrend(DataTempVo dataTempVo);
}
