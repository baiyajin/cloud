package com.baiyajin.controller.control;


import com.baiyajin.entity.bean.Page;
import com.baiyajin.entity.bean.PageSubscription;
import com.baiyajin.entity.bean.SubscriptionVo;
import com.baiyajin.util.u.*;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Api("订阅")
@Controller
@CrossOrigin
@RequestMapping("/PageSubscriptionController")
public class PageSubscriptionController {

    @Autowired
    RestTemplate restTemplate;

    private static final String Rest_url_prefix = "http://report";

    @RequestMapping(value = "/", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public List<Map<String,Object>> login(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String,Object> map) {
        return null;
    }

    /**
     * 增加订阅消息
     * @param pageSubscription
     * @return
     */
    @ApiOperation(value = "新增订阅" ,notes = "新增订阅默认状态ID为启用(qy)，若不默认可传入statusID：jy，isPush代表是否推送(0代表已推送，1代表未推送)，bookPrice代表订阅是材料价格")
    @ApiImplicitParams({@ApiImplicitParam(name = "title（必填),materialID(必填),areaID（必填）token（必填），remark（非必填），isPush(必填),bookPrice(必填)，bookDate(订阅要关注的数据的时间)"
            ,value =  "title:jijkokie,materialID:1dsfs3,areaID:sa132546fdaf/sfsa.*,token:asasdffa,remark:s546daf,bookDate：20190-04-15",dataType = "String",paramType = "body")})
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public Object add(PageSubscription pageSubscription, @RequestParam("startTimeStr") String startTimeStr,@RequestParam("endTimeStr")String endTimeStr,@RequestParam(value = "bookDateStr",required = false)String bookDateStr){
        return  restTemplate.postForObject(Rest_url_prefix+"/PageSubscriptionController/add",pageSubscription, Object.class,startTimeStr,endTimeStr,bookDateStr);
    }


    /**
     * 删除订阅
     * @param id
     * @return
     */
    @ApiOperation(value = "删除订阅" ,notes = "逻辑删除，statusId值为jy代表已删除，数据库依然存在，但是页面不显示")
    @ApiImplicitParams({@ApiImplicitParam(name = "id（必填)",value =  "id:123465",dataType = "String",paramType = "body")})
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public Object delete(String id,String token){
        return  restTemplate.postForObject(Rest_url_prefix+"/PageSubscriptionController/delete",id, Object.class,token);
    }

    /**
     * 分页查询订阅消息
     * @param subscriptionVo
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "分页查询订阅" ,notes = "分页查询，未传入pageNum和pageSize默认从第1页查，每页十条数据")
    @ApiImplicitParams({@ApiImplicitParam(name = "pageNum（非必填),pageSize(非必填)，token（必填）,areaId(非必填，区域ID，多个用逗号隔开)," +
            "maId(非必填，材料ID，多个用逗号隔开),month(非必填，输入月份，如:2019.04)",value =  "token:sdfsadfsa,pageNum:1,pageNum:5",dataType = "String",paramType = "body")})
    @RequestMapping(value = "/findPage",method = RequestMethod.POST)
    @ResponseBody
    public Object findPage(SubscriptionVo subscriptionVo, String pageNum, String pageSize){
        return  restTemplate.postForObject(Rest_url_prefix+"/PageSubscriptionController/findPage",subscriptionVo, Object.class,pageNum,pageNum);
    }

    /**
     * 查询订阅详情
     * @param id
     * @return
     */
    @ApiOperation(value = "查询订阅详情" ,notes = "ID查询，只要ID能获取到就能查到文章，无论是否被删除")
    @ApiImplicitParams({@ApiImplicitParam(name = "id（必填)",value =  "id:123465",dataType = "String",paramType = "body")})
    @RequestMapping(value = "/getInfoById",method = RequestMethod.POST)
    @ResponseBody
    public Object getInfoById(String id,String token){
        return  restTemplate.postForObject(Rest_url_prefix+"/PageSubscriptionController/getInfoById",id, Object.class,token);
    }



}



