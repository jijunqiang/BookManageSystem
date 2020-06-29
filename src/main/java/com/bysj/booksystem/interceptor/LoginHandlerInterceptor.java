package com.bysj.booksystem.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class LoginHandlerInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        HttpSession session = request.getSession();
        Object loginUser = session.getAttribute("loginUser");
        if(loginUser == null){
            //未登录
//            request.setAttribute("msg","请先登录");
//            request.getRequestDispatcher("/").forward(request,response);
            response.sendRedirect("/");
            return false;
        }else {
            //已登录
            return true;
        }

    }
}
