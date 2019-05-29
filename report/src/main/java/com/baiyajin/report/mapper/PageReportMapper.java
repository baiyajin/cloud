package com.baiyajin.report.mapper;



import com.baiyajin.entity.bean.DataTempVo;
import com.baiyajin.entity.bean.PageReport;
import com.baiyajin.entity.bean.ReportVo;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface PageReportMapper extends BaseMapper<PageReport> {
    List<ReportVo> findList(ReportVo reportVo);

    int getCount(ReportVo reportVo);

    int updateRemark(Map<String, Object> map);

    Map<String,Object> selectRemarkByReportId(Map<String, Object> map);

    int addRemark(Map<String, Object> map);

    PageReport selectRemark(Map<String, Object> map);

    ReportVo getReportInfoById(String id);

    List<DataTempVo> findDataByReportId(DataTempVo dataTempVo);

    String getMaName(String id);

    Map<String,BigDecimal> getTrend(DataTempVo dataTempVo);
}
