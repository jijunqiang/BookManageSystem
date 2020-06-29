package com.bysj.booksystem.dao;

import com.bysj.booksystem.model.RememberMe;
import com.bysj.booksystem.model.User;
import org.apache.ibatis.annotations.Param;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.springframework.jdbc.support.CustomSQLErrorCodesTranslation;

import java.util.List;

public interface UserMapper {

    //登录
    public User selectUser(@Param("phone") String phone);

    //添加一个用户
    public int insertUser(@Param("user") User user);

    //查询所有用户(用户管理界面)
    public List<User> selectAllUserByRole(@Param("index") Integer index,@Param("limit") Integer limit);

    //查询一个用户
    public User selectUserById(int id);

    //修改用户角色
    public int updateRole(@Param("id") int id,@Param("role") String role);

    //根据手机号查询用户(用户管理页面)
    public List<User> selectUserByPhone(@Param("phone") String phone,@Param("index") Integer index,@Param("limit") Integer limit);

    //根据手机号查询用户(图书证办理页面)
    public User selectPhone(@Param("phone") String phone);

    //通过手机号查询用户(图书证挂失页面)
    public List<User> queryUser(@Param("phone") String phone);

    //查询所有用户(用户统计界面)
    public List<User> selectUsersByCondition(@Param("userIds") List<Integer> userIds,@Param("gender") String gender,@Param("condition") String condition,@Param("index") Integer index,@Param("limit") Integer limit);

    //查询用户通过user_idList
    public List<User> selectUserByIdList(@Param("list") List<Integer> list);

    //修改用户信息
    public void updateUserInfo(@Param("user") User user);

    //修改用户密码
    public void updatePassword(@Param("newPass") String newPass,@Param("user_id") Integer user_id);

    //得到查询用户的总数量
    public Integer getCount(@Param("phone") String phone);

    //得到男生数量
    public Integer getMaleCount();

    //得到女生数量
    public Integer getFemaleCount();
}
