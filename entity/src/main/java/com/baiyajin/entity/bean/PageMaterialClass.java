package com.baiyajin.entity.bean;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("page_material_class")
public class PageMaterialClass {
  private String id;
  private String name;
  private String statusID;


  private Timestamp createTime;
  private Timestamp updateTime;





}
