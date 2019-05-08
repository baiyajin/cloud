package com.baiyajin.entity.bean;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@Data
@TableName("page_subscription")
public class PageSubscription {
  private String id;
  private String title;
  private String materialID;
  private String areaID;
  private String userID;
  private String statusID;
  private String number;
  private String isPush;  //是否推送，0代表已推送，1代表未推送
  private String remark;
  private Date bookDate;
  @TableField(exist = false)
  private String token;

  private Timestamp createTime;
  private Timestamp updateTime;



}
