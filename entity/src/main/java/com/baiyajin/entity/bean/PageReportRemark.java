package com.baiyajin.entity.bean;

import com.baomidou.mybatisplus.annotations.TableName;
import com.sun.scenario.effect.impl.prism.PrImage;
import lombok.Data;

import java.util.Date;

@Data
@TableName("page_repor_remark")
public class PageReportRemark {
    private String id; //主键ID
    private String userId;  //用户ID
    private String reportId;    //报告ID
    private String mark;    //备注内容
    private String statusID;
    private Date updateTime;    //更新时间
    private Date createTime;    //创建时间
}
