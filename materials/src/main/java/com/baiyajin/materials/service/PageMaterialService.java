package com.baiyajin.materials.service;



import com.baiyajin.entity.bean.MaterialAndClass;
import com.baiyajin.entity.bean.MaterialCount;
import com.baiyajin.entity.bean.MaterialVo;
import com.baiyajin.entity.bean.PageMaterial;
import com.baiyajin.materials.mapper.PageMaterialMapper;
import com.baiyajin.util.u.DateFormatUtil;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PageMaterialService extends ServiceImpl<PageMaterialMapper,PageMaterial> implements PageMaterialInterface {

    @Autowired
    private PageMaterialClassInterface pageMaterialClassInterface;

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
   @Cacheable(value ="getMaterialsInfo1")
    public List<Map<String,Object>> getMaterialsInfo(Map<String,Object> map) throws ParseException {
        Map<String,Object> mapp = new HashMap<>();
        mapp.putAll(map);
        //默认查询一级分类
        if(mapp.get("level")==null){
            mapp.put("level",1);
        }
        System.out.println("查询......");
        if(mapp.get("number")!=null){
            String number =  mapp.get("number").toString();
            Date nowDate = new Date();
            mapp.put("endDate",nowDate);

            //计算开始时间
            //计算单位为月份
            if(mapp.get("type")==null || mapp.get("type").toString().equals("1")){
                Date stDate = DateFormatUtil.dateCompute(nowDate,2,Integer.parseInt(number));
                mapp.put("stratDate",stDate);
            }
            //计算单位为年
            if(mapp.get("type")!=null && mapp.get("type").toString().equals("3")){
                Date stDate = DateFormatUtil.dateCompute(nowDate,1,Integer.parseInt(number));
                mapp.put("stratDate",stDate);
            }
        }

        if(mapp.get("startDate")!=null && "".equals(mapp.get("startDate").toString())){
            Date stDate1 =  DateFormatUtil.stringToDate(mapp.get("startDate").toString(),"yyyy-MM");
            stDate1 =  DateFormatUtil.setDate(stDate1,5,1);
            mapp.put("startDate",DateFormatUtil.dateToString(stDate1));
        }
        if(mapp.get("endDate")!=null && "".equals(mapp.get("startDate").toString())){
            Date endDate =  DateFormatUtil.stringToDate(mapp.get("endDate").toString(),"yyyy-MM");
            String lastDay = DateFormatUtil.getDateLastDay(endDate);
            mapp.put("endDate",lastDay);
        }


        //默认云南地区
        if(mapp.get("area")==null || "".equals(mapp.get("area"))){
            mapp.put("area","53");
        }
        //type=0查询月份（默认），1查询季度，2查询年
        if(mapp.get("type")==null || mapp.get("type").toString().equals("0")  ){
            //直接查询返回结果(否则统计后返回)
            return baseMapper.getMaterialsInfo(map);
        }
       /* if(map.get("type").toString().equals("2")  ){
                map.put("type","quarter");
        }
        if(map.get("type").toString().equals("3")  ){
            map.put("type","year");
        }*/

         return this.getMaterialsInfoByYear(mapp);
    }





    @Override
    @Cacheable(value ="getMaterialsInfo4")
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


    @Override
    public MaterialCount getMaterialCount(Map<String, Object> map) {
        List<MaterialCount> materialCountList = baseMapper.getMaterialCountByMap(map);
        return materialCountList.size()>0?materialCountList.get(0):null;
    }




    private List<Map<String, Object>> getMaterialsInfoByYear(Map<String, Object> map) {

        return baseMapper.getMaterialsInfoByYear(map);

    }


}
