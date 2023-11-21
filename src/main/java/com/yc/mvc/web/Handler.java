package com.yc.mvc.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Handler {

    String pattern; //地址表达式

    Method method; //方法对象

    Object controller; //控制器对象

    public Object handle(HttpServletRequest request){
        //从请求参数中获取方法参数值
        final Parameter[] parameters = method.getParameters();

        //获取参数名 => RequestParam
        List<Object> parameterValues = new ArrayList<>();
        for (Parameter parameter : parameters) {
            //获取参数名 => RequestParam
            final RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
            String name = requestParam.value();

            //默认值，必填属性  省略。。。。。。
            //获取字符串类型的请求参数的值
            String svalue = request.getParameter(name);
            //转换参数的类型
            Object value = castType(svalue,parameter.getType());
            parameterValues.add(value);
        }
        final Object[] parameterArray = parameterValues.toArray();
        //允许强制访问
        method.setAccessible(true);

        try {
            return method.invoke(controller,parameterArray);
        } catch (Exception e) {
            throw new RuntimeException("控制器方法执行失败!",e);
        }
    }

    private Object castType(String svalue, Class<?> type) {
        if (type.isAssignableFrom(String.class)){
            return svalue;
        }else if (type.isAssignableFrom(Integer.class) || type.isAssignableFrom(int.class)){
            return Integer.valueOf(svalue);
        }else if (type.isAssignableFrom(Double.class) || type.isAssignableFrom(double.class)){
            return Double.valueOf(svalue);
        }else {
            //TODO 扩展其它类型的转换
        }
        return svalue;
    }
//    method.invoke(被调用对象，参数数组)
}
