package com.baiyajin.entity.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class ReportVo implements Serializable {

    private String id;  //id
    private String type;    //报告类型
    private String dataType;  // 数据类型,1代表月度,2代表季度，3代表年度
    private String title;   //报告标题
    private String materialName;
    private String areaName;
    private String contrastRegionID;
    private String materialClassID;
    private List<String> maNameList;
    private List<String> maIdList;
    private List<String> areaNameList;
    private List<String> areaIdList;
    private List<String> titleList;
    private Date startTime;
    private String startTimeStr;
    private Date endTime;
    private String endTimeStr;
    private String timeInterval;
    private Date createTime;  //时间
    private String userID;
    private String token;
    private Integer pageCurrent;    //分页查询开始记录数
    private Integer pSize;
    private Page<ReportVo>  page;//分页信息

}
