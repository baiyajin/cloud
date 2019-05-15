package com.baiyajin.materials.service;

import com.baiyajin.entity.bean.MaterialAndClass;
import com.baiyajin.entity.bean.MaterialCount;
import com.baiyajin.entity.bean.MaterialVo;
import com.baiyajin.entity.bean.PageMaterial;
import com.baomidou.mybatisplus.service.IService;

import java.text.ParseException;
import java.util.List;

import java.util.Map;

public interface PageMaterialInterface extends IService<PageMaterial> {
    /**
     *
     * @param materialVo
     * @return
     */
    List<MaterialVo> findByTime(MaterialVo materialVo);

    List<MaterialAndClass> getMaterialsAndClass(Map<String,Object> map);

    public List<MaterialAndClass> getMaterialsClass(Map<String,Object> map);

    public List<Map<String,Object>> getMaterialsInfo(Map<String,Object> map) throws ParseException;

    public List<Map<String,Object>> getMaterialsInfoByArea(Map<String,Object> map) throws ParseException;

    public MaterialCount getMaterialCount(Map<String, Object> map);





}
