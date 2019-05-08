package com.baiyajin.report.mapper;


import com.baiyajin.entity.bean.PageSubscription;
import com.baiyajin.entity.bean.SubscriptionVo;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

public interface PageSubscriptionMapper extends BaseMapper<PageSubscription> {

    List<SubscriptionVo> findList(SubscriptionVo subscriptionVo);

    int getCount(SubscriptionVo subscriptionVo);
}
