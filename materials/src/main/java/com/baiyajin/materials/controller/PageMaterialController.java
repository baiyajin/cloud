package com.baiyajin.materials.controller;


import com.baiyajin.entity.bean.MaterialAndClass;
import com.baiyajin.entity.bean.MaterialCount;
import com.baiyajin.entity.bean.MaterialVo;
import com.baiyajin.entity.bean.PageMaterial;
import com.baiyajin.materials.service.PageMaterialInterface;
import com.baiyajin.util.u.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.shiro.crypto.hash.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Api("材料")
@Controller
@RequestMapping("/PageMaterialController")
public class PageMaterialController {

    @Autowired
    private PageMaterialInterface pageMaterialInterface;

    @RequestMapping(value = "/", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public List<Map<String,Object>> login(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String,Object> map) {
        return null;
    }

    /**
     * 添加材料
     * @param pageMaterial
     * @return
     */
    @RequestMapping(value = "/addMaterial",method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public Object addMaterial(PageMaterial pageMaterial){
        pageMaterial.setId(IdGenerate.uuid());
        pageMaterial.setStatusID("qy");
        pageMaterial.setCreateTime(new Timestamp(System.currentTimeMillis()));
        pageMaterial.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        try {
            pageMaterialInterface.insert(pageMaterial);
        } catch (Exception e) {
            e.printStackTrace();
            return new Results(1,"fail");
        }
        return new Results(0,"success");
    }

    /**
     * 根据时间查询数据
     * @param materialVo
     * @return
     */
    @RequestMapping(value = "/findByTime",method = RequestMethod.POST)
    @ResponseBody
    public Object findByTime(MaterialVo materialVo){
        List<MaterialVo> materialVoList = pageMaterialInterface.findByTime(materialVo);
        if (materialVoList == null || materialVoList.size() == 0){
            return new Results(1,"暂时无数据");
        }
                return materialVoList;
    }

    @RequestMapping(value = "/getMaterials", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public Object getMaterials(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String,Object> map) {
        Map<String,Object> pMap = new HashMap<String,Object>();
        pMap.put("id",map.get("id"));
        List<PageMaterial> pageMaterialList = pageMaterialInterface.selectByMap(pMap);
        if (pageMaterialList == null || pageMaterialList.size() == 0){
            return new Results(1,"暂时无数据");
        }
        return pageMaterialList;

    }

   /* @RequestMapping(value = "/getMaterialsAndClass", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public List<MaterialAndClass> getMaterialsAndClass(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String,Object> map) {
        return pageMaterialInterface.getMaterialsAndClass(map);
    }*/


    @ApiOperation(value = "获取材料分类",notes = "获取材料分类信息（1,2级材料），请求类型json")
    @ApiImplicitParams({@ApiImplicitParam(name = "无需参数",value =  "如：\t {}",dataType = "String")})
    @RequestMapping(value = "/getMaterialsClass", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public List<MaterialAndClass> getMaterialsClass(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String,Object> map) {
        return pageMaterialInterface.getMaterialsClass(map);
    }

    //	"type":"1","pid":"0",, "area":"530112000000", "stratDate":"2019-01-01", "endDate":"2019-04-02"

    private int cou = 0;
    @ApiOperation(value = "获取材料价格信息",notes = "获取材料价格及统计数据，请求类型json")
    @ApiImplicitParams({@ApiImplicitParam(name = "pid,area,id,type,stratDate,endDate，number,level",value =  "分类id默认（查询一级分类）" +
            "区域地址id（默认查询云南地区），材料id指定查询该材料的信息,查询方式type=1查询月份（默认）type=2查询季度type=3查询年，" +
            "stratDate，stratDate查询的开始时间和结束时间，以当前时间为参照查询最近的数量,查询材料的等级 如：\t {\"pid\":\"0\",\"area\":\"530102000000\",\"id\":\"\"}",dataType = "String")})
    @RequestMapping(value = "/getMaterialsInfo", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public List<Map<String,Object>> getMaterialsInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String,Object> map) throws ParseException {
        return pageMaterialInterface.getMaterialsInfo(map);
    }

    /**
     * 获取最近一次一级分类材料数据
     * @param request
     * @param response
     * @param map
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/getMaterialsInfoByRecent", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public List<Map<String,Object>> getMaterialsInfoByRecent(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String,Object> map) throws ParseException {
        map.put("level","1");
        map.put("type","0");
        map.put("area","53");
        map.put("startDate","2019-03");
        map.put("endDate","2019-03");
      return pageMaterialInterface.getMaterialsInfo(map);

    }





    @ApiOperation(value = "",notes = "json")
    @ApiImplicitParams({@ApiImplicitParam(name = "",value =  "",dataType = "String")})
    @RequestMapping(value = "/getMaterialsInfoByArea", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public List<Map<String,Object>> getMaterialsInfoByArea(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String,Object> map) throws ParseException {

            return pageMaterialInterface.getMaterialsInfoByArea(map);

    }

    @ApiOperation(value = "",notes = "json")
    @ApiImplicitParams({@ApiImplicitParam(name = "",value =  "",dataType = "String")})
    @RequestMapping(value = "/getMaterialsInfoByAreaEncapsulation", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public ReturnModel getMaterialsInfoByAreaEncapsulation(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String,Object> map) throws ParseException {



        map.put("type","0");

        List<Map<String,Object>> list = pageMaterialInterface.getMaterialsInfoByArea(map);
        return new ReturnModel( 1,listToTreeArea(list));

    }

    public   Map<String,List<Map<String,Object>>> listToTreeArea( List<Map<String,Object>> childrenMaterialsList ){
        Map<String,List<Map<String,Object>>> map = new HashMap<>();
        System.out.println("size"+childrenMaterialsList.size());
        for(Map<String,Object> ml:childrenMaterialsList){
            if( map.get(ml.get("area").toString())!=null){
                List<Map<String,Object>> list =  map.get(ml.get("area").toString());
                list.add(ml);
                map.put(ml.get("area").toString(),list);
            }else{
                List<Map<String,Object>> list =  new ArrayList<>();
                list.add(ml);
                map.put(ml.get("area").toString(),list);
            }
        }
        return map;
    }




    /**
     * 获取材料统计数量
     * @param map
     * @return
     */
    @RequestMapping(value = "/getMaterialCount", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnModel getMaterialCount( @RequestBody Map<String, Object> map) {
        Map<String, Object> reMap = new HashMap<>();
        try {
            map.put("area", "530100000000");
            MaterialCount kmMaterialCount = pageMaterialInterface.getMaterialCount(map);
            map.remove("area");

            MaterialCount otherMaterialCount = pageMaterialInterface.getMaterialCount(map);
            otherMaterialCount.setCount(otherMaterialCount.getCount()-kmMaterialCount.getCount());
            map.put("mid", 0);
            MaterialCount materialCountAll = pageMaterialInterface.getMaterialCount(map);
            reMap.put("km", kmMaterialCount);
            reMap.put("other", otherMaterialCount);
            reMap.put("all", materialCountAll);
        }catch (Exception e){
            return new ReturnModel(0,null);
        }

        return new ReturnModel(1,reMap);

    }

    /**
     * 获取市级区域信息
     * @param map
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/getMaterialsInfoByAllCities", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnModel getMaterialsInfoByAllCities( @RequestBody Map<String, Object> map) throws ParseException {

    Integer monthNumber = Integer.parseInt(map.get("monthNumber").toString())-1;

        map.put("level","1,2,3");
        map.put("type","0");
        if(map.get("area")==null){
            String areas = "530100000000,530300000000,530400000000,530500000000,530600000000,530700000000," +
                    "530800000000,530900000000,532300000000,532500000000,532600000000,532800000000,532900000000,533100000000,533300000000,533400000000";
            map.put("area",areas);
        }

        Date date = DateFormatUtil.stringToDate("2019-03-31 23:59:59");
        Date stDate = DateFormatUtil.dateCompute(date,2,monthNumber * -1);
        stDate = DateFormatUtil.setDate(stDate,5,1);
        map.put("startDate",DateFormatUtil.dateToString(stDate,"yyyy-MM"));
        map.put("endDate",DateFormatUtil.dateToString(date,"yyyy-MM"));
System.out.println(map);
        List<Map<String,Object>> materialsList = pageMaterialInterface.getMaterialsInfo(map);

       // pageMaterialInterface
        return new ReturnModel(1,materialsList);
    }





    /**
     * 获取材料封装
     * @param map
     * @return
             * @throws ParseException
     */
    @RequestMapping(value = "/getMaterialsInfoEncapsulation", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnModel getMaterialsInfoEncapsulation( @RequestBody Map<String, Object> map) throws ParseException {

        map.put("level","1,2,3");
        map.put("area","53");
        map.put("type","0");
        String pid = map.get("pid")==null?null:map.get("pid").toString();
        List<Map<String,Object>> childrenMaterialsList = pageMaterialInterface.getMaterialsInfo(map);




//        map.remove("pid");
//        map.put("id",pid);
//        List<Map<String,Object>> materialsList = pageMaterialInterface.getMaterialsInfo(map);

        //Map<String, List<String>> resultList = materialsList.stream().collect(Collectors.groupingBy(String::get("level").toString()));
//        Map<String, List<Map<String,Object>>> remap = ListToTree(materialsList,childrenMaterialsList);
        return new ReturnModel(1,ListToTree(childrenMaterialsList));
    }



    public   Map<String,List<Map<String,Object>>> ListToTree( List<Map<String,Object>> childrenMaterialsList ){
        Map<String,List<Map<String,Object>>> map = new HashMap<>();
       for(Map<String,Object> ml:childrenMaterialsList){
          if( map.get(ml.get("mid").toString())!=null){
              List<Map<String,Object>> list =  map.get(ml.get("mid").toString());
              list.add(ml);
              map.put(ml.get("mid").toString(),list);
          }else{
              List<Map<String,Object>> list =  new ArrayList<>();
              list.add(ml);
              map.put(ml.get("mid").toString(),list);
          }
       }
        return map;
    }



//
//
//    public Map<String, List<Map<String,Object>>> ListToTree(List<Map<String,Object>> materialsList,List<Map<String,Object>> cmaterialsList) throws ParseException {
//
//        List<Map<String,Object>> list1 = new ArrayList<Map<String,Object>>();
//
//        Map<String, List<Map<String,Object>>> map = new HashMap<>();
//
//        if(materialsList.size()==0){
//            map.put("0",cmaterialsList);
//            return map;
//        }
//        for(Map<String,Object> m:materialsList){
//            m.put("children",new ArrayList<Map<String,Object>>());
//        }
//
////        for(Map<String,Object> m:materialsList ){
////            if(map.get(m.get("mid").toString())==null){
////                List<Map<String,Object>> list11 = new ArrayList<Map<String,Object>>();
////                list11.add(m);
////                map.put(m.get("mid").toString(),list11);
////            }else{
////                List<Map<String,Object>> list11 = (List<Map<String, Object>>) map.get(m.get("mid").toString());
////                list11.add(m);
////                map.put(m.get("mid").toString(),list11);
////            }
////        }
//
//
//
//       for(Map<String,Object> m:materialsList) {
//           for (Map<String, Object> cm : cmaterialsList) {
//               System.out.println("pid:" + cm.get("pid"));
//               System.out.println("mid:" + m.get("mid"));
//
//              Date a = DateFormatUtil.stringToDate(cm.get("asmdate").toString());
//               Calendar cale = Calendar.getInstance();
//               cale.setTime(a);
//               int month = cale.get(Calendar.MONTH) + 1;
//               Date a1 = DateFormatUtil.stringToDate(m.get("asmdate").toString());
//               Calendar cale1 = Calendar.getInstance();
//               cale1.setTime(a1);
//               int month1 = cale1.get(Calendar.MONTH) + 1;
//
//               if (cm.get("pid").toString().equals(m.get("mid").toString()) && month==month1) {
//                   List<Map<String, Object>> list = (List<Map<String, Object>>) m.get("children");
//                   list.add(cm);
//                   m.put("children", list);
//                   list1.add(m);
//               }
//           }
//       }
//
//
//        for(Map<String,Object> m:list1 ){
//            if(map.get(m.get("mid").toString())==null){
//                List<Map<String,Object>> list11 = new ArrayList<Map<String,Object>>();
//                list11.add(m);
//                map.put(m.get("mid").toString(),list11);
//            }else{
//                List<Map<String,Object>> list11 = (List<Map<String, Object>>) map.get(m.get("mid").toString());
//                list11.add(m);
//                map.put(m.get("mid").toString(),list11);
//            }
//        }
//
//
//
//
//        return  map;
//    }
//







 /*   @RequestMapping(value = "/getMaterialsInfoByYear", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public List<Map<String,Object>> getMaterialsInfoByYear(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String,Object> map) {

        return pageMaterialInterface.getMaterialsInfoByYear(map);

    }*/



}



