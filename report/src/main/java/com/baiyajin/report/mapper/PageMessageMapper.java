package com.baiyajin.report.mapper;

import com.baiyajin.entity.bean.PageMessage;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

public interface PageMessageMapper extends BaseMapper<PageMessage> {

    public  Integer  getCount(Map<String, Object> map);


    public  List<PageMessage>   getMessage(Map<String, Object> map);

    public Integer deleteMessage(Map<String, Object> map);
}
