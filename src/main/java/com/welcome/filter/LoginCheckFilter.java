package com.welcome.filter;

import com.alibaba.fastjson.JSON;
import com.welcome.controller.Result;
import com.welcome.util.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    // 路径匹配器,支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 转换为httpServlet
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 放行微信小程序的所有请求
//        String referer = request.getHeader("Referer");
//        if(referer!=null && referer.contains("servicewechat")){
//            filterChain.doFilter(request,response);
//            return;
//        }
        String requestURI = request.getRequestURI();
        log.info("拦截到请求" + requestURI);
        // 忽略静态页面
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/user/login",
                "/user/logout",
                "/backend/**",
                "/front/**"
        };
        // 遍历静态目录,放行
        for (String url : urls) {
            if(PATH_MATCHER.match(url,requestURI)){
                filterChain.doFilter(request,response);
                return;
            }
        }
        // 判断后台登录状态
        if(request.getSession().getAttribute("employee")!=null){
            Long empId = (Long) request.getSession().getAttribute("employee");
            // 把当前用户的id放进线程里
            BaseContext.setCurrentId(empId);
            filterChain.doFilter(request,response);
            return;
        }
        // 判断前台登录状态
        if(request.getSession().getAttribute("userId")!=null){
            Long userId = (Long) request.getSession().getAttribute("userId");
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(request,response);
            return;
        }
        // 没登录就返回未登录的信息给前端
        log.info("前后台未登录");
        response.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
    }
}
