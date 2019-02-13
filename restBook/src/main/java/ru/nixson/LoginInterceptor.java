package ru.nixson;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception)
            throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Enumeration headerNames = request.getHeaderNames();
        String token = "";
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            if(key.toUpperCase()=="AUTHORIZATION"){
                token = request.getHeader(key);
            }
        }
        if(token.length() == 0 || !AddressBook.checkToken(token)){
            throw new Exception("Invalid User Id or Password. Please try again.");
        }

        return true;
    }


}