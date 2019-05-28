package com.baiyajin.controller.control;


import com.baiyajin.entity.bean.Page;
import com.baiyajin.entity.bean.PageReport;
import com.baiyajin.entity.bean.ReportVo;
import com.baiyajin.util.u.*;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

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
    RestTemplate restTemplate;

    private static final String Rest_url_prefix = "http://report";


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
    @ApiOperation(value = "新增报告" ,notes = "新增报告默认状态ID为启用(qy)，type（1 平台发布,2 我的）,状态默认为qy，若不默认可传入statusID：jy")
    @ApiImplicitParams({@ApiImplicitParam(name = "name(必填,由用户选择以后前端进行拼接传回)，" +
            "报告数据类型 dataType(必填)，1代表月度,2代表季度，3代表年度" +
            "mark(非必填)，token（必填）,timeInterval(订阅的时间点，必填),materialClassID(材料类型ID，选择传入,可多个，用逗号隔开)，contrastRegionID(对比地区，可多个，用逗号隔开)"
            ,value =  "name:123,logo:safdaf/sfsa.*,content:asfa,mark:sdaf，materialClassID：12346，dataType：1，contrastRegionID：132,asdf,123",dataType = "String",paramType = "form-data")})
    @RequestMapping(value = "/addReport",method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public Object addReport (PageReport pageReport) throws ParseException {
        return  restTemplate.postForObject(Rest_url_prefix+"/PageReportController/addReport",pageReport, Object.class);
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
        return  restTemplate.postForObject(Rest_url_prefix+"/PageReportController/deleteReport",id, Object.class);
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
        return  restTemplate.postForObject(Rest_url_prefix+"/PageReportController/findListByPage",reportVo, Object.class,pageNum,pageSize);
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
        return  restTemplate.postForObject(Rest_url_prefix+"/PageReportController/getReportByInfo",id, Object.class,token);
        }

    }




