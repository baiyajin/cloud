package com.baiyajin.user.service;

import com.baiyajin.entity.bean.SystemUser;
import com.baiyajin.user.mapper.SystemUserMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SystemUserService extends ServiceImpl<SystemUserMapper,SystemUser> implements SystemUserInterface {

    @Override
    public List<Map<String, Object>> loadSystemUserList(Map<String, Object> map) {
        return baseMapper.loadSystemUserList(map);
    }
}
