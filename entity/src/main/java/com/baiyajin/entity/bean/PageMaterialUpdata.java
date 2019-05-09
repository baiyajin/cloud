package com.baiyajin.entity.bean;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@TableName("material_price_info_update")
public class PageMaterialUpdata {

    private int id;
    private int c1;
    private int c2;
    private int c3;
    private String mname;
    private String mspec;
    private String munit;
    private String remark;
    private int mid;
    private String city;
    private String area;
    private BigDecimal price;
    private Timestamp mdate;
    private String temp;
    private int mattype;

    @Override
    public String toString() {
        return "PageMaterialUpdata{" +
                "id=" + id +
                ", c1=" + c1 +
                ", c2=" + c2 +
                ", c3=" + c3 +
                ", mname='" + mname + '\'' +
                ", mspec='" + mspec + '\'' +
                ", munit='" + munit + '\'' +
                ", remark='" + remark + '\'' +
                ", mid=" + mid +
                ", city='" + city + '\'' +
                ", area='" + area + '\'' +
                ", price=" + price +
                ", mdate=" + mdate +
                ", temp=" + temp +
                '}';
    }
}
