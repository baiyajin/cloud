package com.baiyajin.controller.control;

import com.baiyajin.entity.bean.PageUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Api("前端用户")
@RestController
@RequestMapping("/PageUserController")
public class PageUserController {

    @Autowired
    RestTemplate restTemplate;

    private static final String Rest_url_prefix = "http://user";




    @RequestMapping(value = "/login", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    @Cacheable(cacheNames={"pag_login"},key = "#map.get('phone')")
    public Map<String,Object> login(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String,Object> map) {
        return restTemplate.postForObject(Rest_url_prefix+"/PageUserController/login",map,Map.class);
    }


    @RequestMapping(value = "/registerAccount", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public Map<String,Object> registerAccount(@RequestBody PageUser user) {
        return restTemplate.postForObject(Rest_url_prefix+"/PageUserController/registerAccount",user,Map.class);
    }


    @ApiOperation(value = "提交修改更新用户资料",notes =
            "请求类型为:\t\n body\t\n"+
                    "请求参数说明:\t\n " +
                    "token(必填)\n" +
                    "name\n" +
                    "password\n" +
                    "headPortrait\n" +
                    "location\n" +
                    "unit\n" +
                    "individualResume\t\n" +
                    "请求参数列表为:\t\n{\"token\":\"122121\",\"name\":\"用户名\",\"headPortrait\":\"头像\",\"location\":\"所在地\",\"unit\":\"单位\",\"individualResume\":\"个人简介\"}")
    @RequestMapping(value = "/updateUserInfo", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object updateUserInfo(@RequestBody PageUser user) {
        return restTemplate.postForObject(Rest_url_prefix+"/PageUserController/updateUserInfo",user,Object.class);
    }


}



