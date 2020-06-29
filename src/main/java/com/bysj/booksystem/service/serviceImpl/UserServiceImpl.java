package com.bysj.booksystem.service.serviceImpl;

import com.bysj.booksystem.dao.RememberMapper;
import com.bysj.booksystem.dao.UserMapper;
import com.bysj.booksystem.model.Antv_sexData;
import com.bysj.booksystem.model.JsonFormatUtils;
import com.bysj.booksystem.model.RememberMe;
import com.bysj.booksystem.model.User;
import com.bysj.booksystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private RememberMapper rememberMapper;
    //登录
    @Override
    public Map<String,Object> getUserByUserNameAndPassword(String phone, String password) {
        User user = userMapper.selectUser(phone);
        HashMap<String, Object> map = new HashMap<>();
        if(user!=null){
            if (user.getPassword().equals(password)){
                map.put("code",1);
                map.put("user",user);
                return map;
            }else {
                map.put("code",-1);
                return map;
            }
        }else {
            map.put("code",0);
            return map;
        }
    }

    //注册
    @Override
    @Transactional
    public String registerUser(User user) {
        String phone = user.getPhone();
        User u = userMapper.selectUser(phone);
        System.out.println(u);
        if(u!=null){
            return "1";
        }else {
            Date date = new Date();
            java.sql.Date date1 = new java.sql.Date(date.getTime());
            user.setRegister_time(date1);
            user.setRole("reader");
            int i = userMapper.insertUser(user);
            if(i==1){
                return "0";
            }
            return "-1";
        }
    }

    //记住密码
    @Override
    @Transactional
    public void setRememberMe(long time, String token, int user_id) {
        RememberMe rememberMe = new RememberMe();
        rememberMe.setUser_id(user_id);
        rememberMe.setToken(token);
        rememberMe.setCreate_time(time);
        rememberMe.setExpires(60*60*24*7);

        //插入前先更新
        rememberMapper.deleteRememberMe(user_id);
        rememberMapper.insetRemember(rememberMe);
    }

    @Override
    public RememberMe getRememberByPhone(String token) {
        RememberMe rememberMe = rememberMapper.selectRememberByToken(token);
        return rememberMe;
    }

    @Override
    public List<User> getAllUserByRole(Integer page,Integer limit) {
        Integer index = 0;
        if(page > 1){
            index = (page - 1) * limit;
        }
        List<User> users = userMapper.selectAllUserByRole(index,limit);
            return users;
    }

    @Override
    public User getUsrById(int id) {
        User user = userMapper.selectUserById(id);
        return user;
    }

    @Override
    public int updateRole(int id, String role) {

        int i = userMapper.updateRole(id, role);
        return i;
    }

    @Override
    public JsonFormatUtils<User> getUserByPhone(String phone,Integer page,Integer limit) {
        Integer index = 0;
        if(page > 1){
            index = (page - 1) * limit;
        }
        List<User> users = userMapper.selectUserByPhone(phone,index,limit);
        Integer count = userMapper.getCount(phone);
        JsonFormatUtils<User> userJsonFormatUtils = new JsonFormatUtils<>();
        userJsonFormatUtils.setCode(0);
        userJsonFormatUtils.setMsg("");
        userJsonFormatUtils.setCount(count);
        userJsonFormatUtils.setData(users);
        return userJsonFormatUtils;
    }

    @Override
    public User getUser(String phone) {
        User user = userMapper.selectPhone(phone);
        return user;
    }

    @Override
    public List<User> queryUser(String phone) {
        List<User> users = userMapper.queryUser(phone);
        return users;
    }

    @Override
    public void updateUserInfo(User user) {
        userMapper.updateUserInfo(user);

    }

    @Override
    public String updatePassword(String oldPass, String newPass, String oncePass,HttpSession session) {
        User loginUser =(User) session.getAttribute("loginUser");
        User user = userMapper.selectUserById(loginUser.getUser_id());
        if (oldPass.equals(user.getPassword())){
            if(newPass.equals(oncePass)){
                userMapper.updatePassword(newPass,user.getUser_id());
                return "修改成功！";
            }else {
                return "新密码与确认密码不同！";
            }

        }else {
            return "输入的旧密码不正确！";
        }
    }

    @Override
    public Integer getCount(String phone) {
        Integer count = userMapper.getCount(phone);
        return count;
    }

    @Override
    public List<Antv_sexData> getSexCount() {
        Integer maleCount = userMapper.getMaleCount();
        Integer femaleCount = userMapper.getFemaleCount();
        Antv_sexData antv_sexData = new Antv_sexData();
        antv_sexData.setGender("男");
        antv_sexData.setCount(maleCount);
        antv_sexData.setPercent((float)maleCount/(maleCount+femaleCount));

        Antv_sexData antv_sexData_1 = new Antv_sexData();
        antv_sexData_1.setGender("女");
        antv_sexData_1.setCount(femaleCount);
        antv_sexData_1.setPercent((float)femaleCount/(maleCount+femaleCount));

        List<Antv_sexData> antv_sexDataList = new ArrayList<>();
        antv_sexDataList.add(antv_sexData);
        antv_sexDataList.add(antv_sexData_1);

        return antv_sexDataList;
    }


}
