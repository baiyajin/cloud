package com.baiyajin.user.mapper;

import com.baiyajin.entity.bean.SystemUser;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

public interface SystemUserMapper extends BaseMapper<SystemUser> {
    List<Map<String , Object>> loadSystemUserList(Map<String, Object> map);


}
