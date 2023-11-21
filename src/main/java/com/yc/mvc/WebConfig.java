package com.yc.mvc;


import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class WebConfig implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext cxt =
                new AnnotationConfigWebApplicationContext();
        cxt.register(MvcConfig.class);
        //注册转发器Servlet
//        DispatcherServlet servlet = new DispatcherServlet(cxt);
        MyDispatcherServlet servlet = new MyDispatcherServlet();
        ServletRegistration.Dynamic registration = servletContext.addServlet("app",servlet);
        //设置服务器在启动时加载 DispatcherServlet
        registration.setLoadOnStartup(1);
        //转发全部请求，必须配置响应地址为: /
        registration.addMapping("/");

        MultipartConfigElement multipartConfigElement
                = new MultipartConfigElement("E:\\133\\sltImg");
        registration.setMultipartConfig(multipartConfigElement);
    }
}
