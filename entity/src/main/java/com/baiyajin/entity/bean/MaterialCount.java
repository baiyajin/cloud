package com.baiyajin.entity.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MaterialCount implements Serializable {

    private int id;
    private int mid;
    private String area;

    private int count;


}
