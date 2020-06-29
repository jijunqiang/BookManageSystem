package com.bysj.booksystem.controller;

import com.bysj.booksystem.model.Antv_sexData;
import com.bysj.booksystem.model.JsonFormatUtils;
import com.bysj.booksystem.model.User;
import com.bysj.booksystem.service.UserService;
import com.bysj.booksystem.utils.MD5;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.print.DocFlavor;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



@Controller
@RequestMapping("userController")
public class UserController {
    @Autowired
    private UserService userService;


    //登录
    @GetMapping("login")
    @ResponseBody
    public String login(String phone, String password, String flag,
                        HttpServletRequest request, HttpServletResponse response, HttpSession session){
        Map<String, Object> result = userService.getUserByUserNameAndPassword(phone, password);
        String code = result.get("code").toString();
        if(code.equals("0")){
            System.out.println("没有该用户，请先注册");
            return code;
        }else if (code.equals("-1")){
            System.out.println("账号或密码错误");
            return code;
        }else {
            User user = (User)result.get("user");
            //用户记住密码
            if(flag.equals("true")){
                //创建时间(秒)
                long time = System.currentTimeMillis()/1000;
                //登陆令牌
                String token = MD5.getMD5ofStr(phone);

                userService.setRememberMe(time,token,user.getUser_id());

                Cookie cookie = new Cookie(phone,token);
                cookie.setMaxAge(60*60*24*7);
                cookie.setPath("/userController/login");
                response.addCookie(cookie);
            }
            session.setAttribute("loginUser",user);
            if(user.getRole().equals("reader")){
                return "2";
            }else {
                return code;
            }
        }
    }

    //注册
    @PostMapping("register")
    @ResponseBody
    public String register(@RequestBody User user){
        String msg = userService.registerUser(user);
        System.out.println(msg);
        return msg;
    }

    //得到登录者信息（用户管理界面，用于判断登录者角色）
    @RequestMapping("userInfo")
    public String allUser(Model model,HttpSession session){
        User loginUser =(User) session.getAttribute("loginUser");
        User usrById = userService.getUsrById(loginUser.getUser_id());
        model.addAttribute("u",usrById);
        return "userPage/userList";
    }

    //根据登录者角色获取用户信息（用户管理界面）
    @RequestMapping("getUserList")
    @ResponseBody
    public JsonFormatUtils<User> getAllUser(HttpSession session,HttpServletRequest request){
        Integer page = Integer.parseInt(request.getParameter("page"));
        Integer limit = Integer.parseInt(request.getParameter("limit"));

        User loginUser =(User) session.getAttribute("loginUser");
        String role1 = loginUser.getRole();
        List<User> allUser = userService.getAllUserByRole(page,limit);

        Integer count = userService.getCount(null);
        if(count == null){
            count =0;
        }
        JsonFormatUtils<User> userJsonFormatUtils = new JsonFormatUtils<>();
        userJsonFormatUtils.setCode(0);
        userJsonFormatUtils.setMsg("");
        userJsonFormatUtils.setCount(count);
        userJsonFormatUtils.setData(allUser);
        return userJsonFormatUtils;
    }

    //修改用户角色
    @RequestMapping("updateRole")
    @ResponseBody
    public String updateRole(Integer user_id,String role){
        int i = userService.updateRole(user_id, role);
        if(i>0){
            return "1";
        }else {
            return "-1";
        }

    }

    //根据手机号查询用户(用户管理页面)
    @RequestMapping("getUserByPhone")
    @ResponseBody
    public JsonFormatUtils<User> getUserByPhone(String phone,HttpServletRequest request){
        Integer page = Integer.parseInt(request.getParameter("page"));
        Integer limit = Integer.parseInt(request.getParameter("limit"));
        JsonFormatUtils<User> userByPhone = userService.getUserByPhone(phone,page,limit);
        return userByPhone;

    }

    //得到登录者信息（主界面，用于展示登录者信息）
    @RequestMapping("getLoginUser")
    @ResponseBody
    public User getLoginUser(HttpSession session){
        User loginUser = (User)session.getAttribute("loginUser");
        User usrById = userService.getUsrById(loginUser.getUser_id());
        return usrById;
    }

    //用手机号查询用户（办理图书证页面）
    @RequestMapping("getUser")
    @ResponseBody
    public User getUser(String phone){
        User user = userService.getUser(phone);
        return user;
    }


    //根据角色得到user
//    private String getUserByRole(Model model, HttpSession session) {
//        User loginUser = (User)session.getAttribute("loginUser");
//        String role1 = loginUser.getRole();
//        List<User> allUser = userService.getAllUserByRole(role1);
//        model.addAttribute("userList",allUser);
//        model.addAttribute("u",loginUser);
//        return "userPage/userList";
//    }

    //根据user_id得到user
    @RequestMapping("getUserById")
    @ResponseBody
    public User getUserById(int user_id){
        User u = userService.getUsrById(user_id);
        return u;
    }

    //通过手机号查询用户(图书证 挂失页面)
    @RequestMapping("queryUser")
    @ResponseBody
    public String queryUser(String phone){
        List<User> users = userService.queryUser(phone);
        String str ="";
        for (int i =0 ;i<users.size();i++){
            if(i<(users.size()-1)){
                str += users.get(i).getUser_id()+",";
            }else {
                str += users.get(i).getUser_id();
            }

        }
        return str;
    }

    //修改用户信息
    @RequestMapping("updateUserInfo")
    @ResponseBody
    public void updateUserInfo(@RequestBody User user){
         userService.updateUserInfo(user);
    }

    //修改密码
    @RequestMapping("updatePassword")
    @ResponseBody
    public String updatePassword(String oldPass,String newPass,String oncePass,HttpSession session){
        String s = userService.updatePassword(oldPass, newPass, oncePass, session);
        session.invalidate();
        return s;
    }

//=========退出登录===============
    @RequestMapping("loginOut")
    public void loginOut(HttpSession session) {
        session.invalidate();
    }

    //=====得到用户男女的数量================
    @RequestMapping("getSexCount")
    @ResponseBody
    public List<Antv_sexData> getSexCount(){
        List<Antv_sexData> sexCount = userService.getSexCount();
        return sexCount;
    }

}
