package com.baiyajin.materials.mapper;


import com.baiyajin.entity.bean.PageMaterialPrice;
import com.baiyajin.entity.bean.PageMaterialUpdata;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

public interface PageMaterialPriceMapper extends BaseMapper<PageMaterialPrice> {


    public List<PageMaterialPrice> selectPageMaterialPrice(Map<String,Object> map);

}
