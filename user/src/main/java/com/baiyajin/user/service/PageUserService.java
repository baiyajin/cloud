package com.baiyajin.user.service;

import com.baiyajin.entity.bean.PageUser;
import com.baiyajin.user.mapper.PageUserMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class PageUserService extends ServiceImpl<PageUserMapper,PageUser> implements PageUserInterface {


}
