package com.baiyajin.user.service;

import com.baiyajin.entity.bean.SystemLog;
import com.baiyajin.user.mapper.SystemLogMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class SystemLogService extends ServiceImpl<SystemLogMapper, SystemLog> implements SystemLogInterface{
}
