package com.yc.mvc.web;

import com.yc.mvc.bean.User;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserAction {
    @RequestMapping(value = "register",
    produces = MediaType.TEXT_HTML_VALUE + ";charset=utf-8")
    public User register(User user){
        return user;
    }

    @RequestMapping("add")
    public int add(@RequestParam("x") int a,
                   @RequestParam(value = "y",required = false,defaultValue = "1")int b){
        return a+b;
    }

    @RequestMapping("sub/{a}/{b}")
    public int sub(@PathVariable(required = true) int a,
                   @PathVariable int b){
        return a - b;
    }

    @RequestMapping("values")
    public Map values(@RequestHeader String Host,
                        @RequestHeader("User-Agent") String userAgent,
                         @RequestHeader String Cookie,//a=1,b=2,c=3
                         @CookieValue String myName,
                         @RequestBody String body){
        Map map = new HashMap();
        map.put("Host",Host);
                map.put("Host",userAgent);
                map.put("Host",Cookie);
                map.put("Host",myName);
                map.put("Host",body);
                return map;

    }
}
