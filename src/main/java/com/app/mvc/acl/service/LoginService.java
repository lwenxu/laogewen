package com.app.mvc.acl.service;

import com.app.mvc.acl.condition.UserCondition;
import com.app.mvc.acl.po.User;
import com.app.mvc.beans.Page;
import com.app.mvc.captcha.Captcha;
import com.app.mvc.exception.ServiceException;
import com.app.mvc.util.LoginUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2018/4/9.
 */
@Service
public class LoginService {

    @Autowired
    private  UserService userService;


    public  User  lgoin(HttpServletRequest request, HttpServletResponse response, User loginUser){

        UserCondition condition=new UserCondition();
        condition.setUserName(loginUser.getUserName());
        Page<User> page=userService.searchUser(condition);
        if(page.getData()==null || page.getData().size()==0){
            throw ServiceException.create("USER.USERNAME.IS.NULL");
        }
        User sysUser=page.getData().get(0);
        if (!sysUser.getEncryptedUserPassword().equalsIgnoreCase(DigestUtils.md5Hex(loginUser.getPassword()))){
            throw ServiceException.create("USER.PWD.IS.FAIL");
        }
        LoginUtil.saveUserToCookie(request,response,loginUser);
        return  sysUser;
    }



    public void generate(HttpServletRequest request,HttpServletResponse response){
        response.setContentType("image/jpeg");
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache");
        response.setDateHeader("Expires",0);

        Captcha captcha=new Captcha(120,40,4,30);
        String  sessionId=request.getSession().getId();
        String  code=captcha.getCode();
        try{
             captcha.write(response.getOutputStream());
        }catch (Exception e){
            throw  ServiceException.create("PUBLIC.SYSTEM.CODE.FAIL");
        }
    }
}



































