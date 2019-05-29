package com.baiyajin.user.controller;

import com.baiyajin.entity.bean.PageUser;
import com.baiyajin.user.service.PageUserInterface;
import com.baiyajin.util.u.HashSalt;
import com.baiyajin.util.u.JWT;
import com.baiyajin.util.u.PhoneUtils;
import com.baiyajin.util.u.Results;
import io.jsonwebtoken.Claims;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/PageUserController")
public class PageUserController {

    @Autowired
    private PageUserInterface pageUserInterface;

    @RequestMapping(value = "/test", method = {RequestMethod.GET})
    @Transactional(rollbackFor = Exception.class)
    public Object test(HttpServletRequest request, HttpServletResponse response) {

        String url = "";
        url = request.getScheme() +"://" + request.getServerName()
                + ":" +request.getServerPort()
                + request.getServletPath();
        if (request.getQueryString() != null){
            url += "?" + request.getQueryString();
        }
        System.out.println("---------------------------");
        System.out.println(url);
        System.out.println("---------------------------");

        return url;
    }


    /**
     * 前台用户登录
     * @param
     * @return user
     */
    @RequestMapping(value = "/login", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
//    @Cacheable(cacheNames={"pag_login"},key = "#map.get('phone')+#map.get('password')")
    public Map<String,Object> login(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String,Object> map) {
        Map<String,Object> m = new HashMap<>();
        try {

            if(null == map.get("password") && "" == map.get("password")){
                m.put("message","密码不能为空");
                return m;
            }
            if(null != map.get("phone") && "" != map.get("phone")
//                    && PhoneUtils.isPhone(map.get("phone").toString())
            ){
//                String salt = HashSalt.encode(Long.parseLong(map.get("password").toString()));
//                String hashSalt = HashSalt.getMD5(salt);
//                String ecPassWord = new SimpleHash("SHA-1", map.get("password").toString(), hashSalt).toString();
                String ecPassWord = HashSalt.getMD5(map.get("password").toString());
                /*通过手机去数据库拿密码作比对*/
                map.remove("password");
                List<PageUser> systemUsers = pageUserInterface.selectByMap(map);
                if(systemUsers.size() > 0 && systemUsers.get(0).getPassword().equals(ecPassWord)){
                    m.put("message","登录成功");
                    systemUsers.get(0).setToken(JWT.createJWT(systemUsers.get(0).getId()));
                    systemUsers.get(0).setHeadPortrait("");
                    m.put("user",systemUsers.get(0));
                    return m;
                }else if(systemUsers.size() == 0){
                    m.put("message","此手机号未注册");
                    return m;
                }else if(!systemUsers.get(0).getPassword().equals(ecPassWord)){
                    m.put("message","密码错误");
                    return m;
                }
            }

            m.put("message","手机号格式不对");
            return m;

        } catch (Exception e) {
            e.printStackTrace();
            m.put("message","异常"+e.getMessage());
            return m;
        }
    }


    /**
     * 前台用户注册账号
     * @param
     * @return user
     */
    @RequestMapping(value = "/registerAccount", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public Map<String,Object> registerAccount(HttpServletRequest request, HttpServletResponse response, @RequestBody PageUser user) {
        Map<String,Object> m = new HashMap<>();
        try {


            Map<String, Object> objectObjectHashMap = new HashMap<>();
            objectObjectHashMap.put("phone",user.getPhone());

            List<PageUser> systemUsers = pageUserInterface.selectByMap(objectObjectHashMap);


            if (null == systemUsers || systemUsers.size() == 0) {
                user.setId(UUID.randomUUID().toString().replace("-", ""));
                user.setStatusID("qy");

                String salt = HashSalt.encode(Long.parseLong(user.getPhone()));
                String hashSalt = HashSalt.getMD5(salt);
                String ecPassWord = new SimpleHash("SHA-1", user.getPassword(), hashSalt).toString();
                user.setPassword(ecPassWord);

                user.setCreateTime(new Timestamp(System.currentTimeMillis()));
                pageUserInterface.insert(user);
                m.put("message","注册成功");
                return m;
            }
            m.put("message","此手机号已经被注册");
            return m;
        } catch (Exception e) {
            e.printStackTrace();
            m.put("message","程序异常"+e.getMessage());
            return m;
        }
    }



    /**
     * 提交修改，更新资料
     * @param user
     * @return Object
     */
    @RequestMapping(value = "/updateUserInfo", method = {RequestMethod.POST}, produces = "application/json;charset=UTF-8")
    @Transactional(rollbackFor = Exception.class)
    @ResponseBody
    public Object updateUserInfo(@RequestBody PageUser user) {
        if(user.getToken() == null)return new Results(1,"token不允许为空");
        if (JWT.parseJWT(user.getToken()) == null)return new Results(1,"token已过期,请重新登录");
        if(user.getPhone() != null)return new Results(1,"此接口不允许修改手机号");
        if(user.getPassword() != null)return new Results(1,"此接口不允许修改密码");

        user.setPhone(null);
        user.setStatusID(null);
        user.setCreateTime(null);
        user.setPassword(null);
        user.setUpdateTime(null);
        user.setUserTypeID(null);

        user.setId(JWT.parseJWT(user.getToken()).getId());

        pageUserInterface.updateById(user);
        return new Results(0,"更新成功",user);
    }


}



