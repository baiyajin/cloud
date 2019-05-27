package com.baiyajin.controller.control;


import com.baiyajin.entity.bean.HelperVo;
import com.baiyajin.entity.bean.Page;
import com.baiyajin.entity.bean.PageHelper;
import com.baiyajin.util.u.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Api("帮助")
@Controller
@RequestMapping(value = "/pageHelperController")
public class PageHelperController {
    @Autowired
    RestTemplate restTemplate;

    private static final String Rest_url_prefix = "http://report";


    /**
     * 新增帮助文章
     * @param pageHelper
     * @return
     */
    @ApiOperation(value = "帮助中心文章增加" ,notes = "publishState默认为1,0代表已发布，1代表未发布，传入0以后自动生成发布时间")
    @ApiImplicitParams({@ApiImplicitParam(name = "title（必填）,content(非必填),publishState(非必填)",value =  "title:123465,content:789463,publishState:0",dataType = "String",paramType = "body")})
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    @CrossOrigin
    public Object addHelper(PageHelper pageHelper){
        return  restTemplate.postForObject(Rest_url_prefix+"/pageHelperController/add",pageHelper, Object.class);
    };

    /**
     * 删除文章
     * @param id
     * @return
     */
    @ApiOperation(value = "帮助中心文章删除" ,notes = "逻辑删除，statusId值为jy代表已删除，数据库依然存在，但是页面不显示")
    @ApiImplicitParams({@ApiImplicitParam(name = "id（必填)",value =  "id:123465",dataType = "String",paramType = "body")})
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    @CrossOrigin
    public Object delete(String id){
        return  restTemplate.postForObject(Rest_url_prefix+"/pageHelperController/delete",id, Object.class);
    };

    /**
     * 修改文章
     * @param pageHelper
     * @return
     */
    @ApiOperation(value = "帮助中心文章修改" ,notes = "需要修改什么那些字段就传入那些字段")
    @ApiImplicitParams({@ApiImplicitParam(name = "id（必填),title(非必填),content(非必填),publishState(非必填)",value =  "id:123465",dataType = "String",paramType = "body")})
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    @CrossOrigin
    public Object update(PageHelper pageHelper){
        return  restTemplate.postForObject(Rest_url_prefix+"/pageHelperController/update",pageHelper, Object.class);
    };

    /**
     * 发布文章
     * @param id
     * @return
     */
    @ApiOperation(value = "帮助中心文章发布" ,notes = "publishState，0代表已发布，1代表未发布，默认新增文章时为未发布")
    @ApiImplicitParams({@ApiImplicitParam(name = "id（必填)",value =  "id:123465",dataType = "String",paramType = "body")})
    @RequestMapping(value = "/publish",method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    @CrossOrigin
    public Object publish(String id){
        return  restTemplate.postForObject(Rest_url_prefix+"/pageHelperController/publish",id, Object.class);
    };


    /**
     * 分页查询帮助文章
     * @param
     * @return
     */
    @ApiOperation(value = "查询帮助中心文章列表" ,notes = "分页查询，未传入pageNum和pageSize默认从第1页查，每页十条数据,num为非必填，填入以后只查询该编号的文章，num为数字")
    @ApiImplicitParams({@ApiImplicitParam(name = "pageNum（非必填),pageSize(非必填)",value =  "pageNum:1,pageNum:5",dataType = "String",paramType = "body")})
    @RequestMapping(value = "/findHelperByPage",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Object findHelperByPage(HelperVo helperVo,String pageNum,String pageSize){
        Map<String, Object> map = ReflectUtil.objcetToMap(helperVo);
        map.put("pageNum",pageNum);
        map.put("pageSize",pageSize);
        return  restTemplate.postForObject(Rest_url_prefix+"/pageHelperController/findHelperByPage",map,Object.class);
    };

    /**
     * 查询文章详情
     * @param id
     * @return
     */
    @ApiOperation(value = "查询文章详情" ,notes = "ID查询，只要ID能获取到就能查到文章，无论是否被删除")
    @ApiImplicitParams({@ApiImplicitParam(name = "id（必填)",value =  "id:123465",dataType = "String",paramType = "body")})
    @RequestMapping(value = "/getArtInfo",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Object getArtInfo(String id){
        return  restTemplate.postForObject(Rest_url_prefix+"/pageHelperController/getArtInfo",id,Object.class);
    }

}
