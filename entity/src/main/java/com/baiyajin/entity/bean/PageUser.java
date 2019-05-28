package com.baiyajin.entity.bean;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("page_user")
public class PageUser {
  private String id;
  private String name;
  private String phone;
  private String password;
  private String statusID;
  private String userTypeID;
  @TableField(exist = false)
  private String token;

  private String headPortrait;
  private String location;
  private String unit;
  private String individualResume;

  private Timestamp createTime;
  private Timestamp updateTime;
  private String area;

}
