package com.baiyajin.report.service;


import com.baiyajin.entity.bean.HelperVo;
import com.baiyajin.entity.bean.Page;
import com.baiyajin.entity.bean.PageHelper;
import com.baomidou.mybatisplus.service.IService;

public interface PageHelperInterface extends IService<PageHelper> {
    Page<HelperVo> findList(Page<HelperVo> page, HelperVo helperVo);
}
