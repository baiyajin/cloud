package com.baiyajin.entity.bean;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MaterialAndClass extends PageMaterialClass implements Serializable{

    private String level;
    private List<PageMaterial> childrenList;





}
