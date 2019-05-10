package com.baiyajin.materials.controller;

import com.baiyajin.entity.bean.PageMaterialClass;
import com.baiyajin.materials.service.PageMaterialClassInterface;
import com.baiyajin.materials.service.PageMaterialClassService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/PageMaterialClassController")
public class PageMaterialClassController {

    @Autowired
    private PageMaterialClassInterface pageMaterialClassInterface;

    @RequestMapping(value = "/getMaterialClass2", method = {RequestMethod.GET})
    @Transactional(rollbackFor = Exception.class)
//    @Cacheable(cacheNames={"getMaterialClass"},key = "#map.get('id')")
    public List<PageMaterialClass> getMaterialClass2(HttpServletRequest request, HttpServletResponse response) {

        String url = "";
        url = request.getScheme() +"://" + request.getServerName()
                + ":" +request.getServerPort()
                + request.getServletPath();
        if (request.getQueryString() != null){
            url += "?" + request.getQueryString();
        }
        System.out.println("---------------------------");
        System.out.println(url);
        System.out.println("---------------------------");

        return pageMaterialClassInterface.selectByMap(new HashMap<String,Object>());
    }



    @RequestMapping(value = "/getMaterialClass", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
//    @Cacheable(cacheNames={"getMaterialClass"},key = "#map.get('id')")
    public List<PageMaterialClass> getMaterialClass(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String,Object> map) {
        Map<String,Object> pMap = new HashMap<String,Object>();
        pMap.put("id",map.get("id"));
        List<PageMaterialClass> PageMaterialClassList = pageMaterialClassInterface.selectByMap(pMap);
        return PageMaterialClassList;
    }










}



