package com.baiyajin.materials.service;



import com.baiyajin.entity.bean.MaterialAndClass;
import com.baiyajin.entity.bean.MaterialVo;
import com.baiyajin.entity.bean.PageMaterial;
import com.baiyajin.entity.bean.PageMaterialPrice;
import com.baiyajin.materials.mapper.PageMaterialMapper;
import com.baiyajin.materials.mapper.PageMaterialPriceMapper;
import com.baiyajin.util.u.DateFormatUtil;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PageMaterialPriceService extends ServiceImpl<PageMaterialPriceMapper,PageMaterialPrice> implements PageMaterialPriceInterface {


    @Override
    public PageMaterialPrice selectPageMaterialPrice(Map<String, Object> map) {
        List<PageMaterialPrice> list = baseMapper.selectPageMaterialPrice(map);
        return  list.size()>0?list.get(0):null;
    }
}
