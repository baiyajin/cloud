/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.baiyajin.entity.bean;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class BasePrice implements Serializable {
	private Integer c1;
	private Integer c2;
	private Integer c3;
	private String mname;
	private String munit;
	private Integer id;
	private Integer mid;
	private String city;
	private String area;
	private BigDecimal price;
	private Date mdate;

}
