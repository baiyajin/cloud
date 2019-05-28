package com.baiyajin.report.service;


import com.baiyajin.entity.bean.DataTempVo;
import com.baiyajin.entity.bean.Page;
import com.baiyajin.entity.bean.PageSubscription;
import com.baiyajin.entity.bean.SubscriptionVo;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

public interface PageSubscriptionInterface extends IService<PageSubscription> {

    Page<SubscriptionVo> findList(Page<SubscriptionVo> page, SubscriptionVo subscriptionVo);

    int getCount(SubscriptionVo subscriptionVo);

    SubscriptionVo getInfoById(String id);

    List<DataTempVo> findDataByReportId(DataTempVo dataTempVo);
}
