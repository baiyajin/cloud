package com.baiyajin.entity.bean;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class SubscriptionVo implements Serializable {
    private String id;
    private String title;
    private String number;
    private Date createTime;
    private Date updateTime;
    private Date bookDate;
//    private Date time;
    private String isPush;  //是否推送，0代表已推送，1代表未推送
    private String areaId; //区域ID
    private String area;
    private String maName;
    private String maId;//材料ID
    private Date startTime;
    private Date endTime;
    private Integer pageCurrent;    //分页查询开始记录数
    private Integer pSize;
    private Page<SubscriptionVo> page;
    private String token;
    private String userID;

    private String month;
    private List<String> maNameList;
    private List<String> maIdList;
    private List<String> areaNameList;
    private List<String> areaIdList;

}
