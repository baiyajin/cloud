package com.baiyajin.controller.control;

import com.baiyajin.entity.bean.MaterialAndClass;
import com.baiyajin.entity.bean.MaterialVo;
import com.baiyajin.entity.bean.PageMaterial;
import com.baiyajin.util.u.DateFormatUtil;
import com.baiyajin.util.u.ReturnModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api("材料")
@Controller
@RequestMapping("/PageMaterialController")
public class Material {

    @Autowired
    RestTemplate restTemplate;

    private static final String Rest_url_prefix = "http://controller";

    /**
     * 添加材料
     * @param pageMaterial
     * @return
     */
    @RequestMapping(value = "/addMaterial",method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public Object addMaterial(PageMaterial pageMaterial){
        return  restTemplate.postForObject(Rest_url_prefix+"/PageMaterialController/addMaterial",pageMaterial, Object.class);
    }

    /**
     * 根据时间查询数据
     * @param materialVo
     * @return
     */
    @RequestMapping(value = "/findByTime",method = RequestMethod.POST)
    @ResponseBody
    public Object findByTime(MaterialVo materialVo){
        return  restTemplate.postForObject(Rest_url_prefix+"/PageMaterialController/findByTime",materialVo, Object.class);
    }

    @RequestMapping(value = "/getMaterials", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public Object getMaterials(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String,Object> map) {
        return  restTemplate.postForObject(Rest_url_prefix+"/PageMaterialController/getMaterials",map, Object.class);
    }



    @ApiOperation(value = "获取材料分类",notes = "获取材料分类信息（1,2级材料），请求类型json")
    @ApiImplicitParams({@ApiImplicitParam(name = "无需参数",value =  "如：\t {}",dataType = "String")})
    @RequestMapping(value = "/getMaterialsClass", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public Object getMaterialsClass(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String,Object> map) {
        return restTemplate.postForObject(Rest_url_prefix+"/PageMaterialController/getMaterialsClass",map, Object.class);
    }

    //	"type":"1","pid":"0",, "area":"530112000000", "stratDate":"2019-01-01", "endDate":"2019-04-02"

    @ApiOperation(value = "获取材料价格信息",notes = "获取材料价格及统计数据，请求类型json")
    @ApiImplicitParams({@ApiImplicitParam(name = "pid,area,id,type,stratDate,endDate，number,level",value =  "分类id默认（查询一级分类）" +
            "区域地址id（默认查询云南地区），材料id指定查询该材料的信息,查询方式type=1查询月份（默认）type=2查询季度type=3查询年，" +
            "stratDate，stratDate查询的开始时间和结束时间，以当前时间为参照查询最近的数量,查询材料的等级 如：\t {\"pid\":\"0\",\"area\":\"530102000000\",\"id\":\"\"}",dataType = "String")})
    @RequestMapping(value = "/getMaterialsInfo", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public Object getMaterialsInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String,Object> map) throws ParseException {
        return restTemplate.postForObject(Rest_url_prefix+"/PageMaterialController/getMaterialsInfo",map, Object.class);
    }


    @RequestMapping(value = "/getMaterialsInfoByRecent", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public List<Map<String,Object>> getMaterialsInfoByRecent(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String,Object> map) throws ParseException {
        return restTemplate.postForObject(Rest_url_prefix+"/PageMaterialController/getMaterialsInfoByRecent",map, List.class);
    }


    @ApiOperation(value = "",notes = "json")
    @ApiImplicitParams({@ApiImplicitParam(name = "",value =  "",dataType = "String")})
    @RequestMapping(value = "/getMaterialsInfoByArea", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public Object getMaterialsInfoByArea(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String,Object> map) throws ParseException {
        return restTemplate.postForObject(Rest_url_prefix+"/PageMaterialController/getMaterialsInfoByArea",map, Object.class);
    }



    @ApiImplicitParams({@ApiImplicitParam(name = "",value =  "",dataType = "String")})
    @RequestMapping(value = "/getMaterialsInfoByProvinceAreaEncapsulation", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public ReturnModel getMaterialsInfoByAreaEncapsulation(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String,Object> map) throws ParseException {
        return restTemplate.postForObject(Rest_url_prefix+"/PageMaterialController/getMaterialsInfoByProvinceAreaEncapsulation",map, ReturnModel.class);

    }



    @RequestMapping(value = "/getMaterialCount", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnModel getMaterialCount( @RequestBody Map<String, Object> map) {
        return restTemplate.postForObject(Rest_url_prefix+"/PageMaterialController/getMaterialCount",map, ReturnModel.class);
    }



    @RequestMapping(value = "/getMaterialsInfoByAllCities", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnModel getMaterialsInfoByAllCities( @RequestBody Map<String, Object> map) throws ParseException {
        return restTemplate.postForObject(Rest_url_prefix+"/PageMaterialController/getMaterialsInfoByAllCities",map, ReturnModel.class);
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
        return restTemplate.postForObject(Rest_url_prefix+"/PageMaterialController/getMaterialsInfoEncapsulation", map,ReturnModel.class);
    }





//    @Cacheable(cacheNames={"getMaterialsClass"},key = "getMaterialsClass")
    public List<MaterialAndClass> getMaterialsClass2(HttpServletRequest request, HttpServletResponse response) {
        return restTemplate.getForObject(Rest_url_prefix+"/PageMaterialController/getMaterialsClass2", List.class);
    }


}



