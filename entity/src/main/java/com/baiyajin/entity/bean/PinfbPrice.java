/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.baiyajin.entity.bean;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("hl_pinfb_price")
public class PinfbPrice implements Serializable {
	private int id;
	private Integer mid;
	private Integer city;
	private Integer area;
	private BigDecimal price;
	private Date mtime;
}
