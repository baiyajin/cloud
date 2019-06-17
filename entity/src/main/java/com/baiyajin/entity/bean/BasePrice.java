/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.baiyajin.entity.bean;

import com.baomidou.mybatisplus.annotations.TableField;
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

	@TableField(exist = false)
	private BigDecimal exponent;

	public BasePrice(){

	}

	public BasePrice(BasePrice entiy){
		this.c1=entiy.getC1();
		this.c2=entiy.getC2();
		this.c3=entiy.getC3();
		this.mname=entiy.getMname();
		this.munit=entiy.getMunit();
		this.id=entiy.getId();
		this.mid=entiy.getMid();
		this.city=entiy.getCity();
		this.area=entiy.getArea();
		this.price=entiy.getPrice();
		this.mdate=entiy.getMdate();
		this.exponent=entiy.getExponent();
	}

}
