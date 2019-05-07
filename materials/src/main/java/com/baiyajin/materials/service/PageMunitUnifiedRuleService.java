package com.baiyajin.materials.service;



import com.baiyajin.entity.bean.MaterialAndClass;
import com.baiyajin.entity.bean.MaterialVo;
import com.baiyajin.entity.bean.PageMaterial;
import com.baiyajin.entity.bean.PageMunitUnifiedRule;
import com.baiyajin.materials.mapper.PageMaterialMapper;
import com.baiyajin.materials.mapper.PageMunitUnifiedRuleMapper;
import com.baiyajin.util.u.DateFormatUtil;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PageMunitUnifiedRuleService extends ServiceImpl<PageMunitUnifiedRuleMapper,PageMunitUnifiedRule> implements PageMunitUnifiedRuleInterface {


}
