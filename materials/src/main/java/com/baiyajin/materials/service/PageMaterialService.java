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

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

@Service
public class PageMaterialService extends ServiceImpl<PageMaterialMapper,PageMaterial> implements PageMaterialInterface {
   private  Map<String,String> muMap = new HashMap<>();
   private List<String> mulist = new ArrayList<>();
    {

        String muc ="12,13,23,24,43,33,42,47";
        String[] mucArr =muc.split(",");
        for(String mc:mucArr){
            mulist.add(mc);
        }

        String ids = "8,9,10,11,12,13,16,17,19,20,21,22,23,24,28,29,30,32,33,37,40,42,43,46,47,45";
        String names =  "钢筋,钢板,钢管,型钢,钢绞线,钢丝绳,水泥,砌体材料," +
                "建筑用石,地基用材,混凝土,建筑砂浆,电力电缆,电气装备用电线电缆,非金属管,复合管,金属管,防水卷材,防水涂料,轻骨料,光纤光缆,混凝土管,其他电气材料,防水砂浆,混凝土预制桩,特种玻璃";

        String mus = "吨,吨,吨,吨,千克,千克,吨,吨,立方米,立方米,立方米,立方米,米,米,米,米,吨,平方米,千克,立方米,米,米,米,吨,米,平方米";
        String[] idArr = ids.split(",");
        String[] nameArr = names.split(",");
        String[] muArr = mus.split(",");
        for(int i=0;i<idArr.length;i++){
            muMap.put(idArr[i],muArr[i]);
        }

    }

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
//   @Cacheable(value ="getMaterialsInfo1")
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
                mapp.put("startDate",stDate);
            }
            //计算单位为年
            if(mapp.get("type")!=null && mapp.get("type").toString().equals("3")){
                Date stDate = DateFormatUtil.dateCompute(nowDate,1,Integer.parseInt(number));
                mapp.put("stratDate",stDate);
            }
        }

        if(mapp.get("startDate")!=null && !"".equals(mapp.get("startDate").toString())){
            Date stDate1 =  DateFormatUtil.stringToDate(mapp.get("startDate").toString(),"yyyy-MM");
            stDate1 =  DateFormatUtil.setDate(stDate1,5,1);
            mapp.put("startDate",DateFormatUtil.dateToString(stDate1));
        }
        if(mapp.get("endDate")!=null && !"".equals(mapp.get("endDate").toString())){
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
            return baseMapper.getMaterialsInfo(mapp);
        }
       /* if(map.get("type").toString().equals("2")  ){
                map.put("type","quarter");
        }
        if(map.get("type").toString().equals("3")  ){
            map.put("type","year");
        }*/

//       muMap.put();



        List<Map<String,Object>> relist = this.getMaterialsInfoByYear(mapp);
        for(Map<String,Object> m:relist){
            m.put("munit",muMap.get(m.get("mid").toString()));

           if( mulist.contains( m.get("mid").toString())){
               BigDecimal price = new BigDecimal( m.get("price").toString());
               BigDecimal newPrice = price.divide(new BigDecimal("1000"),BigDecimal.ROUND_DOWN);
               m.put("price",newPrice);
           }
        }

        return relist;
    }







    @Override
//    @Cacheable(value ="getMaterialsInfo4")
    public List<Map<String, Object>> getMaterialsInfoByArea(Map<String, Object> map) throws ParseException {
        //默认云南
//        if (map.get("area")==null || map.get("area").toString().equals("")){
//            map.put("area","53");
//        }

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


        List<Map<String,Object>> relist =  baseMapper.getMaterialsInfoByArea(map);
        for(Map<String,Object> m:relist){
            m.put("munit",muMap.get(m.get("mid").toString()));
            if( mulist.contains(  m.get("mid").toString())){
                BigDecimal price = new BigDecimal( m.get("price").toString());
                BigDecimal newPrice = price.divide(new BigDecimal("1000"),BigDecimal.ROUND_DOWN);
                m.put("price",newPrice);
            }
        }
        return relist;
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
