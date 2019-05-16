package com.baiyajin.entity.bean;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

@Data
public class DataTempVo implements Serializable {
    private String areaName;
    private String areaId;
    private String maName;
    private BigDecimal price;
    private BigDecimal tbPrice;
    private BigDecimal hbPrice;
    private BigDecimal exponent;
    private BigDecimal tb;
    private BigDecimal hb;
    private String startTimeStr;
    private String endTimeStr;
    private String materialClassID;
    private String contrastRegionID;
    private String type;
    private String maDate;
    private  String mId;
    private String munit;   //单位
    private String timeInterval;

    private int timeIntervalYear;
    private int timeIntervalMonth;

}
