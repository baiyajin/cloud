package com.baiyajin.controller.control;


import com.baiyajin.entity.bean.PageMessage;
import com.baiyajin.util.u.CustomException;
import com.baiyajin.util.u.JWT;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api("消息")
@Controller
@RequestMapping("/PageMessageController")
@CrossOrigin
public class PageMessageController {

    @Autowired
    RestTemplate restTemplate;

    private static final String Rest_url_prefix = "http://report";

    /**
     * 8.	获取消息列表
     * @param request
     * @param response
     * @param map
     * @return
     */
    @ApiOperation(value = "获取消息列表",notes = "通过用户id获取该用户的消息数据，请求类型json")
    @ApiImplicitParams({@ApiImplicitParam(name = "userId（必填）",value =  "如：\t{\"userId\":\"11\"}",dataType = "String")})
    @RequestMapping(value = "/getMessage", method = {RequestMethod.POST},produces = "application/json;charset=UTF-8")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public Map<String,Object> getMessage(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String,Object> map) {
        return  restTemplate.postForObject(Rest_url_prefix+"/PageMessageController/getMessage",map, Map.class);
    }


    /**
     * 8.	删除消息
     * @param request
     * @param response
     * @param map
     * @return
     */
    @ApiOperation(value = "删除消息",notes = "通过消息id删除该条消息（需要删除多条id用逗号隔开），请求类型json")
    @ApiImplicitParams({@ApiImplicitParam(name = "id（必填）",value =  "如：\t{\"userId\":\"11\"}或者{\"userId\":\"11,12\"}",dataType = "String")})
    @RequestMapping(value = "/delMessage", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public Map<String,Object> delMessage(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String,Object> map) {
        return  restTemplate.postForObject(Rest_url_prefix+"/PageMessageController/delMessage",map, Map.class);
    }

//    /**
//     * 获取消息条数
//     * @param request
//     * @param response
//     * @param map
//     * @return
//     */
//    @RequestMapping(value = "/getMessageCount", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
//    @Transactional(rollbackFor = Exception.class)
//    @ResponseBody
//    public int getMessageCount(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String,Object> map) {
//        return  pageMessageInterface.selectCount(map);
//    }



}



