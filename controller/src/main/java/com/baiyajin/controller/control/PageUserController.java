package com.baiyajin.controller.control;

import com.baiyajin.entity.bean.PageUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/PageUserController")
public class PageUserController {

    @Autowired
    RestTemplate restTemplate;

    private static final String Rest_url_prefix = "http://user";




    /**
     * 前台用户登录
     * @param
     * @return user
     */
    @RequestMapping(value = "/login", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    @Cacheable(cacheNames={"pag_login"},key = "#map.get('phone')")
    public Map<String,Object> login(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String,Object> map) {
        return restTemplate.getForObject(Rest_url_prefix+"/PageUserController/login",Map.class);
    }


    /**
     * 前台用户注册账号
     * @param
     * @return user
     */
    @RequestMapping(value = "/registerAccount", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public Map<String,Object> registerAccount(HttpServletRequest request, HttpServletResponse response, @RequestBody PageUser user) {
        return restTemplate.getForObject(Rest_url_prefix + "/PageUserController/registerAccount", Map.class);
    }
}



