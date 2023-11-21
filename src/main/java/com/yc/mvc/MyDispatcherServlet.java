package com.yc.mvc;

import com.google.gson.Gson;
import com.yc.mvc.web.DemoController;
import com.yc.mvc.web.Handler;
import com.yc.mvc.web.IndexAction;
import com.yc.mvc.web.UserAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MyDispatcherServlet extends HttpServlet {

    //控制器集合
    Map<String, Handler> handlerMap = new HashMap<>();


    @Override
    public void init() throws ServletException {
        System.out.println("------+++-----");
        //假定: SpringIOC 容器扫描出三个控制器类  bean
        Object[] controllers = new Object[]{
                new DemoController(),
                new IndexAction(),
                new UserAction()
        };

        for (Object controller : controllers) {
            //判断控制器有没地址前缀
            final RequestMapping requestMapping = controller.getClass().getAnnotation(RequestMapping.class);
            String pathPrefix ="";
            if (requestMapping != null && requestMapping.value().length > 0){
                pathPrefix = requestMapping.value()[0];
            }

            final Method[] methods = controller.getClass().getDeclaredMethods();
            for (Method method : methods) {
                //仅判断一个注解 RequestMapping
                final RequestMapping requestMapping1 = method.getAnnotation(RequestMapping.class);
                if (requestMapping1 == null || requestMapping1.value().length == 0){
                    continue;
                }
                //构建地址表达式
                String pattern = pathPrefix + "/" +requestMapping1.value()[0];
                //在地址前补 /
                if (pattern.startsWith("/") == false){
                    pattern = "/" + pattern;
                }

                //构建 Handler
                Handler handler = new Handler(pattern,method,controller);
                handlerMap.put(pattern,handler);

            }
        }
        //验证
        handlerMap.values().forEach(System.out::println);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doDispatch(req,resp);
    }

    void doDispatch(HttpServletRequest request,
                      HttpServletResponse response) throws IOException {
        //根据地址查找控制器(Handler)
        String requestURI = request.getRequestURI();

        //上下文对象
        final ServletContext servletContext = request.getServletContext();
        //获取上下文路径
        final String contextPath = servletContext.getContextPath();
        //去除上下文路径
        requestURI = requestURI.replace(contextPath,"");

        final Handler handler = handlerMap.get(requestURI);

        //执行控制器方法，获取返回结果(json)
        if (handler == null){
            //TODO => 转到静态资源处理流程
            return;
        }
        final Object res = handler.handle(request);

        //将结果输出到浏览器
        if (handler.getController().getClass().getAnnotation(RestController.class) != null
            || handler.getController().getClass().getAnnotation(Controller.class) != null
            && handler.getMethod().getAnnotation(ResponseBody.class) != null){
            //JSON
            response.setContentType("application/json;charset=utf-8");
            Gson gson = new Gson();
            final String json = gson.toJson(res);
            response.getWriter().append(json);
        }else if (handler.getController().getClass().getAnnotation(Controller.class) != null
                || handler.getController().getClass().getAnnotation(RestController.class) != null
                && handler.getMethod().getReturnType().isAssignableFrom(ModelAndView.class)){
                //页面跳转 => 仅仅实现响应重定向
                //获取跳转的地址
            String path;
                if (res instanceof  String){
                    //Controller
                    path = (String) res;
                }else if (res instanceof  ModelAndView){
                    path = ((ModelAndView) res).getViewName();
                }else {
                    return;
                }
                if (path.startsWith("redirect:")){
                    path = path.substring("redirect:".length());
                    response.sendRedirect(path);
                }else {
                    //请求转发 => JSP 或 Thymeleaf 视图
                }

        }


    }


}
