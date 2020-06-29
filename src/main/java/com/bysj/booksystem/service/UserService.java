package com.bysj.booksystem.service;

import com.bysj.booksystem.model.*;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.omg.CORBA.INTERNAL;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface UserService {

    //登录
    public Map<String,Object> getUserByUserNameAndPassword(String phone, String password);

    //注册
    public String registerUser(User user);

    //记住密码
    public void setRememberMe(long time,String token,int user_id);

    //得到一条记录
    public RememberMe getRememberByPhone(String phone);

    //查询所有用户(用户管理界面)
    public List<User> getAllUserByRole(Integer page,Integer limit);

    //查询一个用户
    public User getUsrById(int id);

    //修改用户角色
    public int updateRole(int id,String role);

    //通过手机号查询用户(用户管理页面)
    public JsonFormatUtils<User> getUserByPhone(String phone,Integer page,Integer limit);

    //通过手机号查询用户(办理图书证页面)
    public User getUser(String phone);

    //通过手机号查询用户(图书证 挂失页面)
    public List<User> queryUser(String phone);

    //修改用户信息
    public void updateUserInfo(User user);

    //修改用户密码
    public String updatePassword(String oldPass, String newPass, String oncePass, HttpSession session);

    //得到查询用户的总数量
    public Integer getCount(String phone);

    //=====得到用户男女的数量================
    public List<Antv_sexData> getSexCount();


}
