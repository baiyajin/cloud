package com.baiyajin.report.controller;

import com.baiyajin.entity.bean.PageReport;
import com.baiyajin.report.service.PageReportInterface;
import com.baiyajin.util.u.DateFormatUtil;
import com.baiyajin.util.u.IdGenerate;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

@Component
@CacheEvict(value="getReportInfoById")
public class ReportTimer {
    @Autowired
    private PageReportInterface pageReportInterface;
//    @Scheduled(cron = "0/600 * * * * ? ")//每600秒执行一次
//    @Scheduled(cron = “00 00 00 01 * ?”)//每月1号的0:00:00执行
    public void creatReport(){
        PageReport pageReport = new PageReport();
        pageReport.setId(IdGenerate.uuid());
        pageReport.setType("1");
        pageReport.setDataType("1");
        pageReport.setCreateTime(new Timestamp(System.currentTimeMillis()));
        pageReport.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        pageReport.setStatusID("qy");
        pageReport.setContrastRegionID("53");
        pageReport.setMaterialClassID("8,9,10,11,12,13,16,17,18,19,20,21,22,37,23,24,43,28,29,30,32,33,46,45,42,47");
        pageReport.setName("云南省建设工程主要材料市场价格变动情况");
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        if (month == 1){
            year = year -1;
            month = 12;
        }else {
            month = month -1;
        }
        pageReport.setTimeInterval(year+"-"+month);
        String startTimeStr = year+"-"+month+"-01 00:00:00";
        Date startTime = null;
        Date endTime = null;
        if (StringUtils.isNotBlank(startTimeStr)){
            try {
                startTime = DateFormatUtil.stringToDate(startTimeStr,"yyyy-MM-dd HH:mm:ss");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (startTime != null){
                pageReport.setStartTime(startTime);
            }
        }

        String endTimeStr = DateFormatUtil.getDateLastDay(String.valueOf(year),String.valueOf(month));
            if (StringUtils.isNotBlank(endTimeStr)){
                try {
                    endTime = DateFormatUtil.stringToDate(endTimeStr,"yyyy-MM-dd HH:mm:ss");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            if (endTime != null){
                pageReport.setEndTime(endTime);
            }
        }

        try {
            pageReportInterface.insert(pageReport);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("自动生成报告失败");
        }
        System.out.println("自动生成报告成功");

    }
}
