package com.baiyajin.controller.control;

import com.baiyajin.entity.bean.SystemUserType;
import com.baiyajin.util.u.IdGenerate;
import com.baiyajin.util.u.Results;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.List;

@Controller
@RequestMapping(value = "/systemUserTypeController")
public class SystemUserTypeController {
    @Autowired
    RestTemplate restTemplate;

    private static final String Rest_url_prefix = "http://user";

    /**
     * 新增用户类型
     * @param systemUserType
     * @return
     */
    @RequestMapping(value = "/addSystemUserType",method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public Object addSystemUserType(SystemUserType systemUserType){
        return restTemplate.getForObject(Rest_url_prefix+"/systemUserTypeController/addSystemUserType",Object.class);

    }

    /**
     * 删除用户类型
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteSystemType",method = RequestMethod.PUT)
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public Object deleteSystemType(String id){
        return restTemplate.getForObject(Rest_url_prefix+"/systemUserTypeController/deleteSystemType",Object.class);


    }

    /**
     * 修改用户类型
     * @param systemUserType
     * @return
     */
    @RequestMapping(value = "/updateSystemType",method = RequestMethod.PUT)
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public Object updateSystemType(SystemUserType systemUserType) {
        return restTemplate.getForObject(Rest_url_prefix+"/systemUserTypeController/updateSystemType",Object.class);

    }

        /**
     * 查询用户类型列表
     * @return
     */
    @RequestMapping(value = "/findUserTypeList",method = RequestMethod.GET)
    @ResponseBody
    public List<SystemUserType> findUserTypeList(){
        return restTemplate.getForObject(Rest_url_prefix+"/systemUserTypeController/findUserTypeList",List.class);

    }

    @RequestMapping(value = "/findUserTypeById")
    @ResponseBody
    public SystemUserType findUserTypeById(String id){
        return restTemplate.getForObject(Rest_url_prefix+"/systemUserTypeController/findUserTypeById",SystemUserType.class);

    };

}
