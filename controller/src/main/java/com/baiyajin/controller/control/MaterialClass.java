package com.baiyajin.controller.control;

import com.baiyajin.entity.bean.PageMaterialClass;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api("材料类")
@RestController
@RequestMapping("/PageMaterialClassController")
@CrossOrigin
public class MaterialClass {

    @Autowired
    RestTemplate restTemplate;

    private static final String Rest_url_prefix = "http://materials";

    @RequestMapping(value = "/getMaterialClass2", method = {RequestMethod.GET})
    @Transactional(rollbackFor = Exception.class)
//    @Cacheable(cacheNames={"getMaterialClass"},key = "#map.get('id')")
    public List<PageMaterialClass> getMaterialClass2(HttpServletRequest request, HttpServletResponse response) {
        return restTemplate.getForObject(Rest_url_prefix+"/PageMaterialClassController/getMaterialClass2",List.class);
    }










}



