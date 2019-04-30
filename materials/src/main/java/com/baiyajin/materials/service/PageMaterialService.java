package com.baiyajin.materials.service;

import com.baiyajin.entity.bean.MaterialAndClass;
import com.baiyajin.entity.bean.MaterialVo;
import com.baiyajin.entity.bean.PageMaterial;
import com.baiyajin.materials.mapper.PageMaterialMapper;
import com.baiyajin.util.u.DateFormatUtil;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class PageMaterialService extends ServiceImpl<PageMaterialMapper,PageMaterial> implements PageMaterialInterface {

    @Override
    public List<MaterialAndClass> getMaterialsAndClass(Map<String,Object> map){
//        String id = map.get("id")==null?null:map.get("id").toString();
//        map.clear();
//        map.put("id",id);
        List<MaterialAndClass> materialAndClassList = baseMapper.getMaterialsAndClass(map);
        return materialAndClassList;
    }

    @Override
    public List<MaterialAndClass> getMaterialsClass(Map<String, Object> map) {
        return baseMapper.getMaterialsClass(map);
    }

    @Override
    public List<MaterialVo> findByTime(MaterialVo materialVo) {
        return baseMapper.findByTime(materialVo);
    }

    @Override
    public List<Map<String,Object>> getMaterialsInfo(Map<String,Object> map) throws ParseException {
        //默认查询一级分类
        if(map.get("level")==null){
            map.put("level",1);
        }

        if(map.get("number")!=null){
            String number =  map.get("number").toString();
            Date nowDate = new Date();
            map.put("endDate",nowDate);

            //计算开始时间
            //计算单位为月份
            if(map.get("type")==null || map.get("type").toString().equals("1")){
                Date stDate = DateFormatUtil.dateCompute(nowDate,2,Integer.parseInt(number));
                map.put("stratDate",stDate);
            }
            //计算单位为年
            if(map.get("type")!=null && map.get("type").toString().equals("3")){
                Date stDate = DateFormatUtil.dateCompute(nowDate,1,Integer.parseInt(number));
                map.put("stratDate",stDate);
            }
        }

        if(map.get("startDate")!=null){
            Date stDate1 =  DateFormatUtil.stringToDate(map.get("startDate").toString(),"yyyy-MM");
            stDate1 =  DateFormatUtil.setDate(stDate1,5,1);
            map.put("startDate",DateFormatUtil.dateToString(stDate1));
        }
        if(map.get("endDate")!=null){
            Date endDate =  DateFormatUtil.stringToDate(map.get("endDate").toString(),"yyyy-MM");
            String lastDay = DateFormatUtil.getDateLastDay(endDate);
            map.put("endDate",lastDay);
        }


        //默认云南地区
        if(map.get("area")==null || "".equals(map.get("area"))){
            map.put("area","53");
        }
        //type=0查询月份（默认），1查询季度，2查询年
        if(map.get("type")==null || map.get("type").toString().equals("0")  ){
            //直接查询返回结果(否则统计后返回)
            return baseMapper.getMaterialsInfo(map);
        }
       /* if(map.get("type").toString().equals("2")  ){
                map.put("type","quarter");
        }
        if(map.get("type").toString().equals("3")  ){
            map.put("type","year");
        }*/

         return this.getMaterialsInfoByYear(map);
    }

    @Override
    public List<Map<String, Object>> getMaterialsInfoByArea(Map<String, Object> map) throws ParseException {
        //默认云南
        if (map.get("area")==null || map.get("area").toString().equals("")){
            map.put("area","53");
        }
       
        if(map.get("startDate")!=null){
            Date stDate1 =  DateFormatUtil.stringToDate(map.get("startDate").toString(),"yyyy-MM");
            stDate1 =  DateFormatUtil.setDate(stDate1,5,1);
            map.put("startDate",DateFormatUtil.dateToString(stDate1));
        }
        if(map.get("endDate")!=null){
            Date endDate =  DateFormatUtil.stringToDate(map.get("endDate").toString(),"yyyy-MM");
            String lastDay = DateFormatUtil.getDateLastDay(endDate);
            map.put("endDate",lastDay);
        }
           // throw new CustomException("参数错误","-1");
//       String areas = map.get("areas").toString();
//
//        map.put("areas",areas);
        return  baseMapper.getMaterialsInfoByArea(map);
    }


    private List<Map<String, Object>> getMaterialsInfoByYear(Map<String, Object> map) {

        return baseMapper.getMaterialsInfoByYear(map);

    }


}
