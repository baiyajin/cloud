package com.baiyajin.report.service;


import com.baiyajin.entity.bean.Page;
import com.baiyajin.entity.bean.PageSubscription;
import com.baiyajin.entity.bean.SubscriptionVo;
import com.baomidou.mybatisplus.service.IService;

public interface PageSubscriptionInterface extends IService<PageSubscription> {

    Page<SubscriptionVo> findList(Page<SubscriptionVo> page, SubscriptionVo subscriptionVo);
}
