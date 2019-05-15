package com.baiyajin.entity.bean;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
@TableName("page_report")
public class PageReport {
  private String id;
  private String type;
  private String dataType;  // 数据类型,1代表月度,2代表季度，3代表年度
  private String name;
  private String logo;
  private String content;
  private String mark;
  private String statusID;
  private String userID;

  private String timeInterval;
  private String materialClassID;
  private String contrastRegionID;

  private Date startTime;
  private Date endTime;



  @TableField(exist = false)
  private String token;


  private Timestamp createTime;
  private Timestamp updateTime;



}
