package com.baiyajin.user.service;

import com.baiyajin.entity.bean.SystemJurisdiction;
import com.baiyajin.util.u.ReturnModel;
import com.baomidou.mybatisplus.service.IService;

import java.util.Map;

public interface SystemJurisdictionInterface extends IService<SystemJurisdiction> {

     public int addJurisdiction(SystemJurisdiction systemJurisdiction);

     public ReturnModel updateJurisdiction(Map<String, Object> map);


}
