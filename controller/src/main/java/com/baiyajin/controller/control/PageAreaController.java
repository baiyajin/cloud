package com.baiyajin.controller.control;


import com.baiyajin.entity.bean.PageArea;
import com.baiyajin.materials.service.PageAreaInterface;
import io.swagger.annotations.Api;
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
import java.util.List;
import java.util.Map;

@Api("地区")
@Controller
@RequestMapping("/PageAreaController")
public class PageAreaController {



    private static final String Rest_url_prefix = "http://controller";
    @Autowired
    RestTemplate restTemplate;



    @RequestMapping(value = "/getAreaList", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public List<PageArea> getAreaList(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String,Object> map) {
        return restTemplate.postForObject(Rest_url_prefix + "/PageAreaController/getAreaList", map,List.class);

    }









}



