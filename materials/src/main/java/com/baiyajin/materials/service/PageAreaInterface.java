package com.baiyajin.materials.service;


import com.baiyajin.entity.bean.PageArea;
import com.baomidou.mybatisplus.service.IService;

import java.util.Map;

public interface PageAreaInterface extends IService<PageArea> {
    Integer save(Map<String, Object> map);
}
