package com.baiyajin.materials.service;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("munit_unified_rule")
public class PageMunitUnifiedRule {

    private int id;
    private String munitName;
    private String matchedNames;
    private BigDecimal coefficient;




}
