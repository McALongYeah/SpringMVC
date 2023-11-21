package com.yc.mvc.web;

import com.yc.mvc.bean.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("a/b/c")
public class DemoController {
    @RequestMapping("hello.do")
    @ResponseBody //=>json
    public String hello(@RequestParam("name") String name){
        return "hello" + name;
    }

    @RequestMapping("toTaobao")
    public String toTaobao(){
        return "redirect:http://www.taobao.com";
    }

    @RequestMapping("toIndex.do")
    public String toIndex(){
        return "/WEB-INF/index.jsp";
    }

    @RequestMapping("sign")
    @ResponseBody
    public Object sign(@Valid User user, Errors errors){
        if (errors.hasErrors()){
            return errors.getAllErrors();
        }else {
            System.out.println("注册成功!");
            return "注册成功!";
        }
    }

    @PostMapping("upload")
    @ResponseBody
    public Object upload(@RequestParam("f1") MultipartFile file,
                         @RequestParam("f2") MultipartFile img,
                         @RequestParam("fs") MultipartFile[] files) throws IOException {
        File dir = new File("E:\\133\\sltImg\\img");
        if (dir.exists() == false){
            dir.mkdirs();
        }

        //保存原文件名
        String filename = file.getOriginalFilename();
        file.transferTo(new File("E:\\133\\sltImg\\img",filename));
        final String uuidname = UUID.randomUUID().toString();
        
        //随机命名文件名
        filename = img.getOriginalFilename();
        filename = filename.replaceAll("[^\\.]\\.([^\\.])",uuidname + ".$1");
        img.transferTo(new File("E:\\133\\sltImg\\img",filename));

        for (MultipartFile f : files) {
            filename = f.getOriginalFilename();
            f.transferTo(new File("E:\\133\\sltImg\\img",filename));;
        }
        return "文件上传成功!";
    }
}
