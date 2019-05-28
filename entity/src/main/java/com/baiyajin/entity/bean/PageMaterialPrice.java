package com.baiyajin.entity.bean;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@TableName("page_material_pice")
public class PageMaterialPrice {

    private int id;
    private int mid;
    private String mat_name;
    private String city;
    private String mat_pid;
    private String level;
    private String area;
    private String area_name;
    private BigDecimal price;
    private BigDecimal exponent;
    private BigDecimal tongbi;
    private BigDecimal huanbi;
    private Timestamp mdate;
    private String type;
    private String temp;
    private int tyear;
    private int is_del;



}
