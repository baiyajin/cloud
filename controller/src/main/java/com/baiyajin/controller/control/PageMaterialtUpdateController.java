package com.baiyajin.controller.control;


import com.baiyajin.util.u.ReturnModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 数据中心更新材料价格用
 */
@RestController
@RequestMapping("/materialtPriceUpdate")
public class PageMaterialtUpdateController {

    private static final String Rest_url_prefix = "http://controller";
    @Autowired
    RestTemplate restTemplate;


    /**
     * 接收材料价格
     * @param request
     * @param response
     * @param map
     * @return
     */
    @RequestMapping(value = "/receiveMaterialtPrice", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    @CrossOrigin
    public ReturnModel receiveMaterialtPrice(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String,Object> map) {
        return restTemplate.postForObject(Rest_url_prefix + "/materialtPriceUpdate/receiveMaterialtPrice", map,ReturnModel.class);
    }



}





