package com.baiyajin.entity.bean;


import lombok.Data;

import java.util.List;

@Data
public class MaterialAndClass extends PageMaterialClass {

    private String level;
    private List<PageMaterial> childrenList;





}
