package com.baiyajin.materials.service;


import com.baiyajin.entity.bean.PageArea;
import com.baiyajin.materials.mapper.PageAreaMapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PageAreaService extends ServiceImpl<PageAreaMapper,PageArea> implements PageAreaInterface {


    @Override
    public Integer save(Map<String, Object> map) {
        Object pid = map.get("pid");
        PageArea newPageArea = new PageArea(map);
        if(pid!=null){
            PageArea pageArea = baseMapper.selectById(pid.toString());
            String path = pageArea.getPid()==null?"":pageArea.getPid();
            newPageArea.setPath(path+","+pageArea.getId());
        }
        return baseMapper.insert(newPageArea);
    }
}
