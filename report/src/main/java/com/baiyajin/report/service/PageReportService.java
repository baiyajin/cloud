package com.baiyajin.report.service;

import com.baiyajin.entity.bean.DataTempVo;
import com.baiyajin.entity.bean.Page;
import com.baiyajin.entity.bean.PageReport;
import com.baiyajin.entity.bean.ReportVo;
import com.baiyajin.report.mapper.PageReportMapper;
import com.baiyajin.util.u.DateFormatUtil;
import com.baiyajin.util.u.Results;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PageReportService extends ServiceImpl<PageReportMapper, PageReport> implements PageReportInterface {
    @Override
    public Page<ReportVo> findList(Page<ReportVo> page, ReportVo reportVo) {
        List<ReportVo> reportVoList = baseMapper.findList(reportVo);
        // 设置分页参数
        reportVo.setPage(page);
        // 执行分页查询
        page.setList(reportVoList);
        return page;
    }

    @Override
    public int getCount(ReportVo reportVo) {
        return baseMapper.getCount(reportVo);
    }

    @Override
    public int updateRemark(Map<String, Object> map) {
        return baseMapper.updateRemark(map);
    }

    @Override
    public Map<String, Object> selectRemarkByReportId(Map<String, Object> map) {
        return baseMapper.selectRemarkByReportId(map);
    }

    @Override
    public int addRemark(Map<String, Object> map) {
        return baseMapper.addRemark(map);
    }

    @Override
    public PageReport selectRemark(Map<String, Object> map) {
        return baseMapper.selectRemark(map);
    }


    @Override
    public List<DataTempVo> findDataByReportId(DataTempVo dataTempVo) {
        return baseMapper.findDataByReportId(dataTempVo);
    }

    @Override
    public String getMaName(String id) {
        return baseMapper.getMaName(id);
    }

    @Override
    public int getTrend(DataTempVo dataTempVo) {
        return baseMapper.getTrend(dataTempVo);
    }

    @Cacheable(value ="getReportInfoById")
    @Override
    public Results getReportInfoById (String id) {
        ReportVo reportVo = baseMapper.getReportInfoById(id);
        if (reportVo == null) {
            return new Results(1, "该报告不存在");
        }
        DataTempVo dataTempVo = new DataTempVo();
        dataTempVo.setTimeInterval(reportVo.getTimeInterval());
        String[] maIds = reportVo.getMaterialClassID().split(",");
        String[] maNames = reportVo.getMaterialName().split(",");
        String dataType = reportVo.getDataType();
        String type = reportVo.getType();
        Map<String, Integer> map = new HashMap<>();

        if ("1".equals(dataType)) {
            dataTempVo.setType("0");
            try {
                map = DateFormatUtil.getYearByDate( DateFormatUtil.stringToDate((reportVo.getTimeInterval() + "-01 00:00:00"),"yyyy-MM-dd HH:mm:ss"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int year = map.get("year");
            int month = map.get("month");
            dataTempVo.setTimeIntervalYear(year);
            dataTempVo.setTimeIntervalMonth(month);
            List<String> titleList = new ArrayList<>();
            for (String s : maNames) {
                titleList.add(year + "年" + month + "月," + s + "月度数据报告");
            }
            reportVo.setTitleList(titleList);
        }
        if ("2".equals(dataType)) {
            dataTempVo.setType("1");
            try {
                map = DateFormatUtil.getYearByDate(reportVo.getEndTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int year = map.get("year");
            List<String> titleList = new ArrayList<>();
            for (String s : maNames) {
                titleList.add(year + "年第" + reportVo.getTimeInterval() + "季度" + s + "季度数据报告");
            }
            reportVo.setTitleList(titleList);
        }
        if ("3".equals(dataType)) {
            dataTempVo.setType("2");
            try {
                map = DateFormatUtil.getYearByDate(reportVo.getEndTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int year = map.get("year");
            List<String> titleList = new ArrayList<>();
            for (String s : maNames) {
                titleList.add(year + "年" + s + "年度数据报告");
            }
            reportVo.setTitleList(titleList);
        }
        dataTempVo.setMaterialClassID(reportVo.getMaterialClassID());
        dataTempVo.setContrastRegionID(reportVo.getContrastRegionID());
        dataTempVo.setStartTimeStr(reportVo.getStartTimeStr());
        dataTempVo.setEndTimeStr(reportVo.getEndTimeStr());

        if ("1".equals(type)){
            dataTempVo.setContrastRegionID("53");
            dataTempVo.setType("0");
            int year = map.get("year");
            int month = map.get("month");
            dataTempVo.setTimeIntervalYear(year);
            dataTempVo.setTimeIntervalMonth(month);
            List<Map<String,Object>> mapList = new ArrayList<>();
            dataTempVo.setMaterialClassID(reportVo.getMaterialClassID());
            Map<String,Object> map3 = new HashMap<>();
            List<DataTempVo> dataTempVoList1 = baseMapper.findDataByReportId(dataTempVo);
            if (dataTempVoList1 != null && dataTempVoList1.size() > 0){
                Map<String,Object> m = new HashMap<>();
                Map<String, List<DataTempVo>> mm =  dataTempVoList1.stream().collect(Collectors.groupingBy(DataTempVo::getLevle));
                Map<String, List<DataTempVo>> mmm =  mm.get("1").stream().collect(Collectors.groupingBy(DataTempVo::getMId));
                Map<String, List<DataTempVo>> mmmm =  mm.get("2").stream().collect(Collectors.groupingBy(DataTempVo::getPId));
                for (String key:mmm.keySet()){
                    for (String key2:mmmm.keySet() ){
                        if (key.equals(key2)){
                            m = new HashMap<>();
                            m.put("mmB",mmm.get(key));
                            m.put("mm",mmmm.get(key2));
                            mapList.add(m);
                        }
                    }
                }

            }
            map3.put("dataList",mapList);
            map3.put("year",year);
            map3.put("month",month);
            map3.put("creatDateStr",DateFormatUtil.dateToString(reportVo.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
            dataTempVo.setTrend("0");
            int rise = baseMapper.getTrend(dataTempVo);    //上涨
            map3.put("rise",rise);
            dataTempVo.setTrend("1");
            int descend = baseMapper.getTrend(dataTempVo); //持平
            map3.put("descend",descend);
            dataTempVo.setTrend("2");
            int unbiased = baseMapper.getTrend(dataTempVo);    //下降
            map3.put("unbiased",unbiased);
            return new Results(0,"查询成功",map3);
        }
        List<DataTempVo> dataTempVoList = baseMapper.findDataByReportId(dataTempVo);
        dataTempVo.setContrastRegionID("53");
        List<DataTempVo> dataTempVoList2 = baseMapper.findDataByReportId(dataTempVo);
        Map<String, List<DataTempVo>> mm =  dataTempVoList.stream().collect(Collectors.groupingBy(DataTempVo::getMId));
        if (maIds != null && maIds.length > 0 && maNames != null && maNames.length > 0){
            Map<String,Object> map2 = new HashMap<>();
            List<Map<String,Object>> mapList = new ArrayList<>();
            for (int i = 0; i<maIds.length; i++){
                map2 = new HashMap<>();
                if (mm.get(maIds[i]) != null){
                    map2.put("maName",baseMapper.getMaName(maIds[i]));
                    map2.put("mm",mm.get(maIds[i]));
                    map2.put("mmYn",dataTempVoList2);
                }else {
                    map2.put("maName",baseMapper.getMaName(maIds[i]));
                    map2.put("mm","暂无数据");
                    map2.put("mmYn",dataTempVoList2);
                }
                if ("1".equals(dataType)) {
                    try {
                        map = DateFormatUtil.getYearByDate(reportVo.getEndTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    int year = map.get("year");
                    int month = map.get("month");
                    map2.put("title",(year + "年" + month + "月," + maNames[i] + "月度数据报告"));
                }
                if ("2".equals(dataType)) {
                    try {
                        map = DateFormatUtil.getYearByDate(reportVo.getEndTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    int year = map.get("year");
                    map2.put("title",(year + "年第" + reportVo.getTimeInterval() + "季度" + maNames[i] + "季度数据报告"));
                }
                if ("3".equals(dataType)) {
                    try {
                        map = DateFormatUtil.getYearByDate(reportVo.getEndTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    int year = map.get("year");
                    map2.put("title",(year + "年" + maNames[i] + "年度数据报告"));
                }
                mapList.add(map2);
            }
            reportVo.setMapList(mapList);
        }
        return  new Results(0, "查询成功",reportVo);
    }

}
