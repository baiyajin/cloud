package com.baiyajin.user.service;


import com.baiyajin.entity.bean.SystemUserType;
import com.baiyajin.user.mapper.SystemUserTypeMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class SystemUserTypeService extends ServiceImpl<SystemUserTypeMapper, SystemUserType> implements SystemUserTypeInterface{
}
