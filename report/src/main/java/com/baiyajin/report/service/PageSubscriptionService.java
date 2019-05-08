package com.baiyajin.report.service;


import com.baiyajin.entity.bean.Page;
import com.baiyajin.entity.bean.PageSubscription;
import com.baiyajin.entity.bean.SubscriptionVo;
import com.baiyajin.report.mapper.PageSubscriptionMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PageSubscriptionService extends ServiceImpl<PageSubscriptionMapper, PageSubscription> implements PageSubscriptionInterface {


    @Override
    public Page<SubscriptionVo> findList(Page<SubscriptionVo> page, SubscriptionVo subscriptionVo) {
        List<SubscriptionVo> subscriptionVoList = baseMapper.findList(subscriptionVo);
        // 设置分页参数
        subscriptionVo.setPage(page);
        // 执行分页查询
        page.setList(subscriptionVoList);
        return page;
    }

    @Override
    public int getCount(SubscriptionVo subscriptionVo) {
        return baseMapper.getCount(subscriptionVo);
    }
}
