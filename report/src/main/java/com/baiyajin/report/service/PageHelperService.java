package com.baiyajin.report.service;


import com.baiyajin.entity.bean.HelperVo;
import com.baiyajin.entity.bean.Page;
import com.baiyajin.entity.bean.PageHelper;
import com.baiyajin.report.mapper.PageHelperMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PageHelperService extends ServiceImpl<PageHelperMapper, PageHelper> implements PageHelperInterface{

    @Override
    public Page<HelperVo> findList(Page<HelperVo> page, HelperVo helperVo) {
        List<HelperVo> helperVoList = baseMapper.findList(helperVo);
        // 设置分页参数
        helperVo.setPage(page);
        // 执行分页查询
        page.setList(helperVoList);
        return page;
    }
}
