package com.baiyajin.materials.mapper;


import com.baiyajin.entity.bean.PageMaterialPrice;
import com.baiyajin.entity.bean.PageMaterialUpdata;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

public interface PageMaterialUpdateMapper extends BaseMapper<PageMaterialUpdata> {


    public List<Map<String, Object>> getMaterialtAvgPrice(Map<String, Object> map);

    public List<PageMaterialUpdata> getMaterialtPriceList(PageMaterialUpdata pageMaterialUpdata);

    public List<PageMaterialPrice> getMaterialtPrice(Map<String, Object> map);
    public PageMaterialPrice getMaterialtPriceByFilter(Map<String, Object> map);




}
