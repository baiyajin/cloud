package com.baiyajin.controller.control;

import com.baiyajin.entity.bean.SystemUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/SystemUserController")
public class SystemUserController {

    @Autowired
    RestTemplate restTemplate;

    private static final String Rest_url_prefix = "http://user";

    /**
     * 增加用户
     * @param systemUser
     * @return
     */
    @RequestMapping(value = "/addSysUser",method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public Object addSysUser(SystemUser systemUser){
        return restTemplate.getForObject(Rest_url_prefix+"/SystemUserController/addSysUser",Object.class);
    }

    /**
     * 删除用户，逻辑删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteSysUser",method = RequestMethod.PUT)
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public Object deleteSysUser(String id){
        return restTemplate.getForObject(Rest_url_prefix+"/SystemUserController/deleteSysUser",Object.class);
    }

    /**
     * 修改用户
     * @param systemUser
     * @return
     */
    @RequestMapping(value = "/updateSysUser",method = RequestMethod.PUT)
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public Object updateSysUser(SystemUser systemUser){
        return restTemplate.getForObject(Rest_url_prefix+"/SystemUserController/updateSysUser",Object.class);
    }


    /**
     * 后台用户登录
     * @param
     * @return
     */
    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public Map<String,Object> login(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String,Object> map) {
        return restTemplate.getForObject(Rest_url_prefix+"/SystemUserController/login",Map.class);
    }


    /**
     * 后台用户注册账号
     * @param
     * @return
     */
    @RequestMapping(value = "/registerAccount", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public Map<String,Object> registerAccount(HttpServletRequest request, HttpServletResponse response, @RequestBody SystemUser user) {
        return restTemplate.getForObject(Rest_url_prefix+"/SystemUserController/registerAccount",Map.class);
    }

    /**
     * 查询后台用户列表
     * @return
     */
    @RequestMapping(value = "/findSysUserList",method = RequestMethod.GET)
    @ResponseBody
    public List<SystemUser> findSysUserList(){
        return restTemplate.getForObject(Rest_url_prefix+"/SystemUserController/findSysUserList",List.class);
    }

    /**
     * 根据用户ID查询用户
     * @param id
     * @return
     */
    @RequestMapping(value = "/findSysUserById")
    @ResponseBody
    public SystemUser findSysUserById(String id){
        return restTemplate.getForObject(Rest_url_prefix+"/SystemUserController/findSysUserById",SystemUser.class);
    }

}



