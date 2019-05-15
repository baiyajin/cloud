package com.baiyajin.report.controller;

import com.baiyajin.entity.bean.PageReportRemark;
import com.baiyajin.entity.bean.ReportRemarkVo;
import com.baiyajin.report.service.PageReportInterface;
import com.baiyajin.report.service.PageReportRemarkInterface;
import com.baiyajin.util.u.IdGenerate;
import com.baiyajin.util.u.JWT;
import com.baiyajin.util.u.Results;
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

import java.util.Date;
import java.util.List;
import java.util.Map;

@Api("报告备注")
@Controller
@RequestMapping(value = "/PageReportRemarkController")

public class PageReportRemarkController {
    @Autowired
    private PageReportRemarkInterface pageReportRemarkInterface;
    @Autowired
    private PageReportInterface pageReportInterface;

    /**
     * 新增报告备注
     * @param map
     * @return
     */
    @ApiOperation(value = "新增报告备注",notes = "新增报告备注，请求类型json")
    @ApiImplicitParams({@ApiImplicitParam(name = "token,,reportId,mark " +
            "如:{\n" +
            "\t\"mark\":\"报告备注\",\n" +
            "\t\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIwOTdmMGRkOWUyMjc0Y2NmYjc2ZjRmYWMxNDQxNjMzOSIsImV4cCI6MTU1NzIwNzk4OSwibmJmIjoxNTU3MTIxNTg5fQ.terrsjOFp7KcCRQa5--eMxWwjpLxCZKJEXPmmayWy70\",\n" +
            "\t\"reportId\":\"8124ac8bf4ff4407a635fd34a91a74a9\"\n" + "}",dataType = "String")})
    @RequestMapping(value = "/add",method = {RequestMethod.POST},produces = "application/json;charset=UTF-8")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public Object addReportRemark(@RequestBody Map<String,Object> map){
        String token = map.get("token") != null ? map.get("token").toString():null;
        if (StringUtils.isBlank(token)){
            return new Results(1,"登录失效，青重新登录");
        }
        PageReportRemark pageReportRemark = new PageReportRemark();
        String id = IdGenerate.uuid();
        pageReportRemark.setId(id);
        Claims claims = JWT.parseJWT(token);
        if (claims == null){
            return new Results(1,"登录失效，青重新登录");
        }else {
            pageReportRemark.setUserId(claims.getId());
        }
        String mark = map.get("mark") != null ? map.get("mark").toString() : null;
        if (StringUtils.isBlank(mark)){
            return new Results(1,"备注内容不能为空");
        }else {
           if (mark.length() > 2000){
               return new Results(1,"备注内容字数不能超过2000");
           }
        }
        pageReportRemark.setMark(mark);
        String reportId = map.get("reportId") != null ? map.get("reportId").toString() : null;
        if (StringUtils.isBlank(reportId)){
            return new Results(1,"报告ID不能为空");
        }
        pageReportRemark.setReportId(reportId);
        pageReportRemark.setCreateTime(new Date());
        pageReportRemark.setUpdateTime(new Date());

        try {
            pageReportRemarkInterface.insert(pageReportRemark);
        } catch (Exception e) {
            e.printStackTrace();
            return new Results(1,"fail");
        }
        return new Results(0,"success");
    }

    /**
     * 删除备注
     * @param map
     * @return
     */
    @ApiOperation(value = "删除备注",notes = "删除备注，请求类型json")
    @ApiImplicitParams({@ApiImplicitParam(name = "token,,reportId " +
            "如:{\n" +
            "\t\"id\":\"f545a47d7b7f4b1b8d6f8adf73a3180d\",\n" +
            "\t\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIwOTdmMGRkOWUyMjc0Y2NmYjc2ZjRmYWMxNDQxNjMzOSIsImV4cCI6MTU1NzIwNzk4OSwibmJmIjoxNTU3MTIxNTg5fQ.terrsjOFp7KcCRQa5--eMxWwjpLxCZKJEXPmmayWy70\"\n" +
            "}",dataType = "String")})
    @RequestMapping(value = "/deleteRemark",method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object deleteRemark(@RequestBody Map<String,Object> map){
        String token = String.valueOf(map.get("token"));
        if (StringUtils.isBlank(token)){
            return new Results(1,"登录失效，青重新登录");
        }
        PageReportRemark pageReportRemark = new PageReportRemark();
        String id = String.valueOf(map.get("id"));
        if (StringUtils.isBlank(id)){
            return new Results(1,"请选择需要删除的备注");
        }else if (pageReportRemarkInterface.selectById(id) == null) {
            return new Results(1,"该备注不存在");
        }
        pageReportRemark.setId(id);
        pageReportRemark.setStatusID("jy");
        pageReportRemark.setUpdateTime(new Date());
        try {
            pageReportRemarkInterface.updateById(pageReportRemark);
        } catch (Exception e) {
            e.printStackTrace();
            return new Results(1,"fail");
        }
        return new Results(0,"success");
    }

    /**
     * 修改备注
     * @param map
     * @return
     */
    @ApiOperation(value = "修改备注",notes = "修改备注，请求类型json")
    @ApiImplicitParams({@ApiImplicitParam(name = "token,,id " +
            "如:{\n" +
            "\t\"id\":\"0b3ac13d0a484b89bfc338c685a25fa0\",\n" +
            "\t\"mark\":\"报告备注修改\"\n" +
            "}",dataType = "String")})
    @RequestMapping(value = "/updateRemark",method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object updateRemark(@RequestBody Map<String,Object> map){
        String token = map.get("token") != null ? map.get("token").toString():null;
        if (StringUtils.isBlank(token)){
            return new Results(1,"登录失效，青重新登录");
        }
        String mark = map.get("mark")==null?null:map.get("mark").toString();
        if (StringUtils.isBlank(mark)){
            return new Results(1,"备注内容不能为空");
        }else {
            if (mark.length() > 2000){
                return new Results(1,"备注内容字数不能超过2000");
            }
        }
        String id = map.get("id")==null?null:map.get("id").toString();
        if (StringUtils.isBlank(id)){
            return new Results(1,"请选择需要修改的备注");
        }
        PageReportRemark pageReportRemark = pageReportRemarkInterface.selectById(id);
        if (pageReportRemark == null) {
            return new Results(1,"该备注不存在");
        }
        pageReportRemark.setMark(mark);
        pageReportRemark.setUpdateTime(new Date());
        try {
            pageReportRemarkInterface.updateById(pageReportRemark);
        } catch (Exception e) {
            e.printStackTrace();
            return new Results(1,"fail");
        }
        return new Results(0,"success");
    }

    /**
     * 查询备注列表
     * @param map
     * @return
     */
    @ApiOperation(value = "查询备注列表",notes = "查询备注列表，请求类型json")
    @ApiImplicitParams({@ApiImplicitParam(name = "token,,reportId " +
            "如:{\n" +
            "\t\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIwOTdmMGRkOWUyMjc0Y2NmYjc2ZjRmYWMxNDQxNjMzOSIsImV4cCI6MTU1NzIwNzk4OSwibmJmIjoxNTU3MTIxNTg5fQ.terrsjOFp7KcCRQa5--eMxWwjpLxCZKJEXPmmayWy70\",\n" +
            "\t\"reportId\":\"8124ac8bf4ff4407a635fd34a91a74a9\"\n" +
            "}",dataType = "String")})
    @RequestMapping(value = "/findList",method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object findList(@RequestBody Map<String,Object> map){
        String token = map.get("token") != null ? map.get("token").toString():null;
        if (StringUtils.isBlank(token)){
            return new Results(1,"登录失效，重新登录");
        }
        Claims claims = JWT.parseJWT(token);
        ReportRemarkVo reportRemarkVo = new ReportRemarkVo();
        if (claims == null){
            return new Results(1,"登录失效，青重新登录");
        }else {
            reportRemarkVo.setUserId(claims.getId());
        }
        String reportId = map.get("reportId")==null?null:map.get("reportId").toString();

        if (StringUtils.isBlank(reportId)){
            return new Results(1,"请传入报告ID");
        }else {
            if (null == pageReportInterface.selectById(reportId)){
                return new Results(1,"该报告不存在");
            }
        }

        reportRemarkVo.setReportId(reportId);
        List<ReportRemarkVo> reportRemarkVoList = pageReportRemarkInterface.findList(reportRemarkVo);
        if (reportRemarkVoList == null || reportRemarkVoList.size()<=0){
            return  new Results(1,"暂无数据");
        }
        return new Results(0,"查询成功",reportRemarkVoList);
    }
}
