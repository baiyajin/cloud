package com.baiyajin.controller.control;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Api("报告备注")
@Controller
@CrossOrigin
@RequestMapping(value = "/PageReportRemarkController")
public class PageReportRemarkController {
    @Autowired
    RestTemplate restTemplate;

    private static final String Rest_url_prefix = "http://report";

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
    @RequestMapping(value = "/add",method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public Object addReportRemark(@RequestBody Map<String,Object> map){
        return  restTemplate.postForObject(Rest_url_prefix+"/PageReportRemarkController/add",map, Object.class);
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
        return  restTemplate.postForObject(Rest_url_prefix+"/PageReportRemarkController/deleteRemark",map, Object.class);
    }

    /**
     * 修改备注
     * @param map
     * @return
     */
    @ApiOperation(value = "修改备注",notes = "修改备注，请求类型json")
    @ApiImplicitParams({@ApiImplicitParam(name = "token,,reportId " +
            "如:{\n" +
            "\t\"id\":\"0b3ac13d0a484b89bfc338c685a25fa0\",\n" +
            "\t\"mark\":\"报告备注修改\"\n" +
            "}",dataType = "String")})
    @RequestMapping(value = "/updateRemark",method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object updateRemark(@RequestBody Map<String,Object> map){
        return  restTemplate.postForObject(Rest_url_prefix+"/PageReportRemarkController/updateRemark",map, Object.class);
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
        return  restTemplate.postForObject(Rest_url_prefix+"/PageReportRemarkController/findList",map, Object.class);
    }
}
