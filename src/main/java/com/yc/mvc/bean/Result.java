package com.yc.mvc.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result {
    int code;
    String msg;
    Object data;

}
