package com.baiyajin.entity.bean;

import lombok.Data;

@Data
public class MaterialCategory {
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
