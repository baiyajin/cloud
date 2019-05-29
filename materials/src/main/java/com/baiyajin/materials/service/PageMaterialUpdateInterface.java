package com.baiyajin.materials.service;


import com.baiyajin.entity.bean.PageMaterialPrice;
import com.baiyajin.entity.bean.PageMaterialUpdata;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;
import java.util.Map;

public interface PageMaterialUpdateInterface extends IService<PageMaterialUpdata> {

  public int receiveMaterialtPrice(Map<String, Object> map);

//  public int saveMaterialtPrice(List<PageMaterialUpdata> list);

//  public List<Map<String, Object>> getMaterialtAvgPrice(Map<String, Object> map);

  public PageMaterialPrice getMaterialtPrice(Map<String, Object> map);





}
