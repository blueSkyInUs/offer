package com.money.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaseResult <T> {
    private int code;
    private String msg;
    private T info;
    public BaseResult(){
        this.code=200;
        this.msg="success";
    }

}
