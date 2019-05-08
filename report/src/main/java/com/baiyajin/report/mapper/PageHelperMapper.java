package com.baiyajin.report.mapper;


import com.baiyajin.entity.bean.HelperVo;
import com.baiyajin.entity.bean.PageHelper;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

public interface PageHelperMapper extends BaseMapper<PageHelper> {
    List<HelperVo> findList(HelperVo helperVo);
}
