package com.bysj.booksystem.interceptor;

import ch.qos.logback.classic.spi.TurboFilterList;
import com.bysj.booksystem.dao.RememberMapper;
import com.bysj.booksystem.model.RememberMe;
import com.bysj.booksystem.model.User;
import com.bysj.booksystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class RememberMeInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        String phone = request.getParameter("phone");
        String flag = request.getParameter("flag");
        HttpSession session = request.getSession();

        if("true".equals(flag)) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null){
                for (Cookie cookie : cookies) {
                    String name = cookie.getName();
                    if (name.equals(phone)) {
                        //调用mapper，查看数据库是否有记录，如果有，查看是否过期
                        String value = cookie.getValue();
                        RememberMe remember = userService.getRememberByPhone(value);
                        if (remember != null) {
                            long created = remember.getCreate_time();
                            long expires = remember.getExpires();
                            long nowTime = System.currentTimeMillis() / 1000;
                            if ((created + expires) > nowTime) {//判断验证信息是否过期
                                User usrById = userService.getUsrById(remember.getUser_id());
                                session.setAttribute("loginUser",usrById);
                                String requestURL = request.getRequestURL().toString();
                                if(usrById.getRole().equals("reader")){
                                    returnInfo(response,"reader");
                                }else {
                                    returnInfo(response,"main");
                                }
                                System.out.println("---1---:");
                                return false;
                            } else {
                                System.out.println("---2");
                                return true;
                            }
                        }
                        System.out.println("---3");
                        return true;
                    }
                }
                System.out.println("---4");
                return true;
            }
            System.out.println("---5");
            return true;
        }

        return true;
    }

    /**
     * @Author:         jjq
     * @CreateDate:     2020/4/4 13:11
     * @Version:        1.0
     * @Description:    给前端返回登录者信息
     */
    public void returnInfo(HttpServletResponse response,String str){
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(str);

        } catch (IOException e) {
        } finally {
            if (writer != null)
                writer.close();
        }
    }
}
