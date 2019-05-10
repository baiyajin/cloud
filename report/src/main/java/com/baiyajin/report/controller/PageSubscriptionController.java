package com.baiyajin.report.controller;


import com.baiyajin.entity.bean.Page;
import com.baiyajin.entity.bean.PageSubscription;
import com.baiyajin.entity.bean.SubscriptionVo;
import com.baiyajin.report.service.PageSubscriptionInterface;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Api("订阅")
@Controller
@RequestMapping("/PageSubscriptionController")
public class PageSubscriptionController {

    @Autowired
    private PageSubscriptionInterface pageSubscriptionInterface;

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
        String token = pageSubscription.getToken();
        Claims claims = JWT.parseJWT(token);
        if (claims == null){
            return new Results(1,"请重新登录");
        }else {
            pageSubscription.setUserID(claims.getId());
        }
        if (pageSubscription == null || StringUtils.isBlank(pageSubscription.getTitle())){
            return new Results(1,"请输入订阅标题");
        }
        if (pageSubscription == null || StringUtils.isBlank(pageSubscription.getMaterialID())){
            return new Results(1,"请选择材料类型");
        }
        if (pageSubscription == null || StringUtils.isBlank(pageSubscription.getAreaID())){
            return new Results(1,"请选择区域");
        }
        if (pageSubscription == null || StringUtils.isBlank(pageSubscription.getIsPush())){
            return new Results(1,"请选择是否推送");
        }

//        if (StringUtils.isNotBlank(bookDateStr)){
//            pageSubscription.setBookDate(DateUtils.parseDate(bookDateStr,"yyyy-MM-dd"));
//        }else {
//            return new Results(1,"请选择时间");
//        }

        if (StringUtils.isNotBlank(startTimeStr) && StringUtils.isNotBlank(endTimeStr)){
            pageSubscription.setStartTime(DateUtils.setDate(DateUtils.parseDate(startTimeStr,"yyyy-mm"),5,01));
            Date endDate =  DateUtils.parseDate(endTimeStr,"yyyy-MM");
            String lastDay = DateUtils.getDateLastDay(endDate);
            Date endTimeDate = DateUtils.parseDate(lastDay,"yyyy-MM-dd");
            pageSubscription.setEndTime(DateUtils.parseDate(lastDay,"yyyy-MM-dd"));
        }else {
            if (StringUtils.isBlank(startTimeStr)){
                return new Results(1,"请选择起始时间");
            }
            if (StringUtils.isBlank(endTimeStr)){
                return new Results(1,"请选择结束时间");
            }

        }
        pageSubscription.setId(IdGenerate.uuid());
        pageSubscription.setCreateTime(new Timestamp(System.currentTimeMillis()));
        pageSubscription.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        pageSubscription.setStatusID("qy");
        try {
            pageSubscriptionInterface.insert(pageSubscription);
        } catch (Exception e) {
            e.printStackTrace();
            return new Results(1,"fail");
        }
        return new Results(0,"success");
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
        if (StringUtils.isBlank(token)){
            return new Results(1,"登录失效，请重新登录");
        }
        Claims claims = JWT.parseJWT(token);
        if (claims == null){
            return new Results(1,"登录失效，请重新登录");
        }
        PageSubscription pageSubscription = new PageSubscription();
        pageSubscription.setId(id);
        pageSubscription.setStatusID("jy");
        try {
            pageSubscriptionInterface.updateById(pageSubscription);
        } catch (Exception e) {
            e.printStackTrace();
            return new Results(1,"fail");
        }
        return new Results(0,"success");
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
        Page<SubscriptionVo> p = new Page();
        String token = subscriptionVo.getToken();
        if (StringUtils.isBlank(token)){
            return new Results(1,"登录失效，重新登录");
        }
        Claims claims = JWT.parseJWT(token);
        if (claims == null){
            return new Results(1,"登录失效，青重新登录");
        }else {
            subscriptionVo.setUserID(claims.getId());
        }
        if (StringUtils.isNotBlank(pageNum)&& StringUtils.isNotBlank(pageSize)){
            if ("0".equals(pageNum)){
                pageNum = "1";
            }
            subscriptionVo.setPageCurrent(PageUtils.pageNoRecord(pageNum,pageSize));
            subscriptionVo.setPSize(Integer.valueOf(pageSize));
            p.setPageSize(Integer.valueOf(pageSize));
            p.setPageNo(Integer.valueOf(pageNum));
        }else {
            subscriptionVo.setPSize(10);
            subscriptionVo.setPageCurrent(PageUtils.pageNoRecord("1","10"));
            p.setPageSize(10);
            p.setPageNo(1);
        }
        String number = subscriptionVo.getNumber();
        if (StringUtils.isNotBlank(number)){
        }
        int count = pageSubscriptionInterface.getCount(subscriptionVo);
        Page<SubscriptionVo> page = pageSubscriptionInterface.findList(p,subscriptionVo);
        if (page == null || page.getList() == null ||page.getList().size() == 0){
            return new Results(1,"暂无数据");
        }
//        for (SubscriptionVo s : page.getList()){
//            String content = "《"+s.getTitle()+"》"+s.getArea()+s.getMaName()+","+((DateFormatUtils.format(s.getStartTime(),"yyyy-MM-dd"))+"至"+DateFormatUtils.format(s.getEndTime(),"yyyy-MM-dd"))+"价格指数：";
//            s.setContent(content);
//        }
        page.setCount(count);
        return page;
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
        if (StringUtils.isBlank(token)){
            return new Results(1,"请重新登录");
        }
        Claims claims = JWT.parseJWT(token);
        if (claims == null){
            return new Results(1,"请重新登录");
        }

        SubscriptionVo subscriptionVo  = pageSubscriptionInterface.getInfoById(id);
        if (subscriptionVo == null){
            return new Results(1,"没有该订阅消息");
        }
        if (StringUtils.isBlank(subscriptionVo.getMaId())){
            return new Results(1,"材料编号为空");
        }
        String[] maIds = subscriptionVo.getMaId().split(",");
        if (maIds != null && maIds.length > 0) {
            List<String> maIdList = new ArrayList<>();
            for (String m : maIds) {
                maIdList.add(m);
            }
            subscriptionVo.setMaIdList(maIdList);
        }
        if (StringUtils.isBlank(subscriptionVo.getMaName())){
            return new Results(1,"材料为空");
        }
        String[] maNames = subscriptionVo.getMaName().split(",");
        if (maNames != null && maNames.length > 0) {
            List<String> maNameList = new ArrayList<>();
            for (String m : maNames) {
                maNameList.add(m);
            }
            subscriptionVo.setMaNameList(maNameList);
        }
        if (StringUtils.isBlank(subscriptionVo.getAreaId())){
            return new Results(1,"区域ID为空");
        }
        String[] areaIds = subscriptionVo.getAreaId().split(",");
        if (areaIds != null && areaIds.length > 0) {
            List<String> areaIdList = new ArrayList<>();
            for (String a : areaIds) {
                areaIdList.add(a);
            }
            subscriptionVo.setAreaIdList(areaIdList);
        }
        if (StringUtils.isBlank(subscriptionVo.getArea())){
            return new Results(1,"区域为空");
        }
        String[] areaNames = subscriptionVo.getArea().split(",");
        if (areaNames != null && areaNames.length > 0) {
            List<String> areaNameList = new ArrayList<>();
            for (String a : areaNames) {
                areaNameList.add(a);
            }
            subscriptionVo.setAreaNameList(areaNameList);
        }
        return subscriptionVo;
    }



}



