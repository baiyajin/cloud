package com.baiyajin.report.controller;


import com.baiyajin.entity.bean.Page;
import com.baiyajin.entity.bean.PageReport;
import com.baiyajin.entity.bean.ReportVo;
import com.baiyajin.report.service.PageReportInterface;
import com.baiyajin.report.service.PageReportRemarkInterface;
import com.baiyajin.util.u.*;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

@Api("报告")
@Controller
@RequestMapping("/PageReportController")
public class PageReportController {

    @Autowired
    private PageReportInterface pageReportInterface;
    @Autowired
    private PageReportRemarkInterface pageReportRemarkInterface;


    @RequestMapping(value = "/", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public List<Map<String,Object>> login(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String,Object> map) {
        return null;
    }

    /**
     * 新增报告
     * @param pageReport
     * @return
     */
    @ApiOperation(value = "新增报告" ,notes = "新增报告默认状态ID为启用(qy)，type（非必填,1 平台发布,2 我的,默认为2）,状态默认为qy，若不默认可传入statusID：jy")
    @ApiImplicitParams({@ApiImplicitParam(name = "name(必填,由用户选择以后前端进行拼接传回)，" +
            "报告数据类型 dataType(必填)，1代表月度,2代表季度，3代表年度" +
            "mark(非必填)，token（必填）,timeInterval(订阅的时间点，必填),materialClassID(材料类型ID，选择传入,可多个，用逗号隔开)，contrastRegionID(对比地区，可多个，用逗号隔开)"
            ,value =  "name:123,logo:safdaf/sfsa.*,content:asfa,mark:sdaf，materialClassID：12346，dataType：1，contrastRegionID：132,asdf,123",dataType = "String",paramType = "form-data")})
    @RequestMapping(value = "/addReport",method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public Object addReport (PageReport pageReport) throws ParseException {
        String token = pageReport.getToken();
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> reMap = new HashMap<>();
        Claims claims = JWT.parseJWT(token);
        if (claims == null){
            return new Results(1,"请重新登录");
        }else {
            pageReport.setUserID(claims.getId());
            pageReport.setType("2");
            map.put("userId",claims.getId());
        }
        String dataType = pageReport != null ? pageReport.getDataType():null;
        if (StringUtils.isBlank(dataType)){
            return new Results(1,"请选择智能报告数据类型");
        }
        String timeInterval = pageReport != null ? pageReport.getTimeInterval():null;
        if (StringUtils.isBlank(timeInterval)){
            return new Results(1,"请选择智能报告时间点");
        }
        Map<String,Date> m = DateFormatUtil.getStAndEndTime(Integer.valueOf(dataType),timeInterval);
        Date startTime = m.get("startDate") != null ? m.get("startDate"):null;
        Date endTime = m.get("endDate") != null ? m.get("endDate"):null;
        if (startTime != null && endTime != null){
            pageReport.setStartTime(startTime);
            pageReport.setEndTime(endTime);
        }

        String id = IdGenerate.uuid();
        pageReport.setType("2");
        pageReport.setId(id);
        pageReport.setStatusID("qy");
        pageReport.setCreateTime(new Timestamp(System.currentTimeMillis()));
        pageReport.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        map.put("reportId",id);
        if (pageReport != null && StringUtils.isNotBlank(pageReport.getMark())){
            map.put("mark",pageReport.getMark());
            reMap =  pageReportInterface.selectRemarkByReportId(map);
            int i = 0;
            if(reMap==null){
                map.put("createTime",new Date());
                map.put("updateTime",new Date());
                map.put("id",IdGenerate.uuid());
                i = pageReportInterface.addRemark(map);
            }else{
                map.put("updateTime",new Date());
                map.put("id",reMap.get("id").toString());
                i = pageReportInterface.updateRemark(map);
            }
            if(i==0){
                return new Results(1,"fail");
            }
        }

        try {
            pageReportInterface.insert(pageReport);
        } catch (Exception e) {
            e.printStackTrace();
            return new Results(1,"fail");
        }
        return new Results(0,"success");
    }

    /**
     * 删除报告
     * @param id
     * @return
     */
    @ApiOperation(value = "删除报告" ,notes = "逻辑删除，statusId值为jy代表已删除，数据库依然存在，但是页面不显示")
    @ApiImplicitParams({@ApiImplicitParam(name = "id（必填)",value =  "id:123465",dataType = "String",paramType = "body")})
    @RequestMapping(value = "/deleteReport",method = RequestMethod.POST)
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public Object deleteReort(String id){
        PageReport p = pageReportInterface.selectById(id);
        if(p == null){
            return new Results(1,"该报告不存在");
        }
        PageReport pageReport = new PageReport();
        pageReport.setId(id);
        pageReport.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        pageReport.setStatusID("jy");
        try {
            pageReportInterface.updateById(pageReport);
            pageReportRemarkInterface.removeByUserAndReport(id,p.getUserID());
        } catch (Exception e) {
            e.printStackTrace();
            return new Results(1,"fail");
        }
        return new Results(0,"success");
    }


    /**
     * 修改报告
     * @param pageReport
     * @return
     */
    @ApiOperation(value = "修改报告" ,notes = "需要修改什么那些字段就传入那些字段，其中logo为图片上传，存放的是图片路径")
    @ApiImplicitParams({@ApiImplicitParam(name = "id（必填),type(非必填),content(非必填),logo(必填)",value =  "id:123465",dataType = "String",paramType = "body")})
    @RequestMapping(value = "/updateReport",method = RequestMethod.POST)
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public Object updateReport(PageReport pageReport,String token){
        String id = pageReport.getId();
        Claims claims = JWT.parseJWT(token);
        String userId = claims.getId();
        PageReport p = pageReportInterface.selectById(id);
        if(p == null){
            return new Results(1,"该报告不存在");
        }

        Map<String,Object> map = new HashMap<>();
        Map<String,Object> reMap = new HashMap<>();
        map.put("reportId",id);
        map.put("userId",userId);
        map.put("mark",pageReport.getMark());

        reMap =  pageReportInterface.selectRemarkByReportId(map);
        int i = 0;
        if(reMap==null){
            map.put("createTime",new Date());
            map.put("updateTime",new Date());
            i = pageReportInterface.addRemark(map);
        }else{
            map.put("updateTime",new Date());
            i = pageReportInterface.updateRemark(map);
        }
        if(i==0){
            return new Results(1,"fail");
        }
        return new Results(0,"success");
    }

    /**
     * 分页查询报告
     * @param reportVo
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "分页产需拿报告" ,notes = "分页查询，未传入pageNum和pageSize默认从第1页查，每页十条数据,num为非必填，填入以后只查询该编号的文章，num为数字," +
            "type(报告类型,1 平台发布,2 我的),不传入参数时默认为查询全部，查询我的报告时，token必填")
    @ApiImplicitParams({@ApiImplicitParam(name = "pageNum（非必填),pageSize(非必填)，token（非必填）,type(非必填)",value =  "pageNum:1,pageNum:5,token:15646saf",dataType = "String",paramType = "body")})
    @RequestMapping(value = "/findListByPage",method = RequestMethod.POST)
    @ResponseBody
    public Object findListByPage(ReportVo reportVo,String pageNum,String pageSize){
        Page<ReportVo> p = new Page();
        PageReport pageReport = new PageReport();
        String token = reportVo.getToken();
        Claims claims = JWT.parseJWT(token);

        if ("2".equals(reportVo.getType())){
            if (claims == null){
                return new Results(1,"请重新登录");
            }else {
                pageReport.setUserID(claims.getId());
                reportVo.setUserID(claims.getId());
            }
        }

        if(StringUtils.isBlank(reportVo.getType())){
            if (claims == null){
                return new Results(1,"请重新登录");
            }else {
                pageReport.setUserID(claims.getId());
                reportVo.setUserID(claims.getId());
            }
        }

        if (StringUtils.isNotBlank(pageNum)&&StringUtils.isNotBlank(pageSize)){
            if ("0".equals(pageNum)){
                pageNum = "1";
            }
            reportVo.setPageCurrent(PageUtils.pageNoRecord(pageNum,pageSize));
            reportVo.setPSize(Integer.valueOf(pageSize));
            p.setPageSize(Integer.valueOf(pageSize));
            p.setPageNo(Integer.valueOf(pageNum));
        }else {
            reportVo.setPSize(10);
            reportVo.setPageCurrent(PageUtils.pageNoRecord("1","10"));
            p.setPageSize(10);
            p.setPageNo(1);
        }

        String type = reportVo.getType();
        if (StringUtils.isNotBlank(type)){
            pageReport.setType(type);
        }
        pageReport.setStatusID("qy");
        int count = pageReportInterface.getCount(reportVo);
        Page<ReportVo> page = pageReportInterface.findList(p,reportVo);
        if (page == null || page.getList() == null ||page.getList().size() == 0){
            return new Results(1,"暂无数据");
        }
        page.setCount(count);
        return page;
    }

    /**
     * 获取报告详情
     * @param id
     * @return
     */
    @ApiOperation(value = "获取报告详情" ,notes = "ID查询，只要ID能获取到就能查到文章")
    @ApiImplicitParams({@ApiImplicitParam(name = "id（必填)",value =  "id:123465",dataType = "String",paramType = "body")})
    @RequestMapping(value = "/getReportByInfo",method = RequestMethod.POST)
    @ResponseBody
    public Object getReportById(String id,String token) throws ParseException {
        Claims claims = JWT.parseJWT(token);
        if (claims == null) {
            return new Results(1, "登录失效");
        }
        ReportVo reportVo = pageReportInterface.getReportInfoById(id);
        if (reportVo == null) {
            return new Results(1, "该报告不存在");
        }
        String[] maIds = reportVo.getMaterialClassID().split(",");
        if (maIds != null && maIds.length > 0) {
            List<String> maIdList = new ArrayList<>();
            for (String m : maIds) {
                maIdList.add(m);
            }
            reportVo.setMaIdList(maIdList);
        }
        String[] maNames = reportVo.getMaterialName().split(",");
        if (maNames != null && maNames.length > 0) {
            List<String> maNameList = new ArrayList<>();
            for (String m : maNames) {
                maNameList.add(m);
            }
            reportVo.setMaNameList(maNameList);
        }
        String[] areaIds = reportVo.getContrastRegionID().split(",");
        if (areaIds != null && areaIds.length > 0) {
            List<String> areaIdList = new ArrayList<>();
            for (String a : areaIds) {
                areaIdList.add(a);
            }
            reportVo.setAreaIdList(areaIdList);
        }
        String[] areaNames = reportVo.getAreaName().split(",");
        if (areaNames != null && areaNames.length > 0) {
            List<String> areaNameList = new ArrayList<>();
            for (String a : areaNames) {
                areaNameList.add(a);
            }
            reportVo.setAreaNameList(areaNameList);
        }
        String dataType = reportVo.getDataType();
        if ("1".equals(dataType)) {
            Map<String, Integer> map = DateFormatUtil.getYearByDate(reportVo.getEndTime());
            int year = map.get("year");
            int month = map.get("month");
            List<String> titleList = new ArrayList<>();
            for (String s : maNames) {
                titleList.add(year + "年" + month + "月," + s + "月度数据报告");
            }
            reportVo.setTitleList(titleList);
        }
        if ("2".equals(dataType)) {
            Map<String, Integer> map = DateFormatUtil.getYearByDate(reportVo.getEndTime());
            int year = map.get("year");
            List<String> titleList = new ArrayList<>();
            for (String s : maNames) {
                titleList.add(year + "年第" + reportVo.getTimeInterval() + "季度" + s + "季度数据报告");
            }
            reportVo.setTitleList(titleList);
        }
        if ("3".equals(dataType)) {
            if ("3".equals(dataType)) {
                Map<String, Integer> map = DateFormatUtil.getYearByDate(reportVo.getEndTime());
                int year = map.get("year");
                List<String> titleList = new ArrayList<>();
                for (String s : maNames) {
                    titleList.add(year + "年" + s + "年度数据报告");
                }
                reportVo.setTitleList(titleList);
            }
        }
        if (reportVo.getStartTime() != null && reportVo.getEndTime() != null){
            reportVo.setStartTimeStr(DateFormatUtil.dateToStr(reportVo.getStartTime()));
            reportVo.setEndTimeStr(DateFormatUtil.dateToStr(reportVo.getEndTime()));
        }
            return reportVo;
        }

    }




