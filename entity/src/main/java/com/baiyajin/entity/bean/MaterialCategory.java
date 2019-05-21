package com.baiyajin.entity.bean;

import lombok.Data;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class MaterialCategory implements Serializable {

    private Integer id;
    private String name;
    private Integer pid;
    private Integer sort;
    private Integer ifshow;
    private String path;
    private Integer level;
    private Integer shot_by_calss;
    private Integer is_del;


}
