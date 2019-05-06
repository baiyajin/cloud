package com.baiyajin.entity.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ReportRemarkVo implements Serializable {
    private String id; //主键ID
    private String userId;  //用户ID
    private String reportId;    //报告ID
    private String mark;    //备注内容
    private Date updateTime;    //更新时间
}
