package com.baiyajin.materials.controller;


import com.baiyajin.materials.service.PageMaterialUpdateInterface;
import com.baiyajin.util.u.ReturnModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 数据中心更新材料价格用
 */
@RestController
@RequestMapping("/materialtPriceUpdate")
public class PageMaterialtUpdateController {

    @Autowired
    private PageMaterialUpdateInterface pageMaterialUpdateInterface;

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
    public ReturnModel receiveMaterialtPrice(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String,Object> map){
        //c1,c2,c3,mname,mspec,munit,remark,city,area,price,mdate
        //通用材料：c1,c2,c3,mname,mspec,munit,remark,city,area,price,mtime
      try{
          pageMaterialUpdateInterface.receiveMaterialtPrice(map);
      }catch (Exception e){
          e.printStackTrace();
          return new ReturnModel(0);
      }
        return new ReturnModel(1);
    }


}
