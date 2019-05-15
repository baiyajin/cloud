package com.baiyajin.materials.service;


import com.baiyajin.entity.bean.PageMaterialPrice;
import com.baiyajin.entity.bean.PageMaterialUpdata;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;
import java.util.Map;

public interface PageMaterialPriceInterface extends IService<PageMaterialPrice> {



    //public  updatePriceData
    public PageMaterialPrice selectPageMaterialPrice(Map<String,Object> map);

}
