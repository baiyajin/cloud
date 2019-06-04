package com.baiyajin.materials.mapper;


import com.baiyajin.entity.bean.BasePrice;
import com.baiyajin.entity.bean.MaterialCategory;
import com.baiyajin.entity.bean.PageMaterialPrice;
import com.baiyajin.entity.bean.PageMaterialUpdata;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

public interface PageMaterialUpdateMapper extends BaseMapper<PageMaterialUpdata> {


    public List<Map<String, Object>> getMaterialtAvgPrice(Map<String, Object> map);

    public List<MaterialCategory> getMaterialCategory();

    public List<PageMaterialPrice> getMaterialtPrice(Map<String, Object> map);
    public PageMaterialPrice getMaterialtPriceByFilter(Map<String, Object> map);

    public List<BasePrice>getPinfbPrice(Map<String, Object> map);

    public List<PageMaterialPrice> getPageMaterialPriceByQuarter(Map<String, Object> map);
    public List<PageMaterialPrice> getPageMaterialPriceByYear(Map<String, Object> map);

    public int saveUpdatelog(List<Map<String, Object>> list);


}
