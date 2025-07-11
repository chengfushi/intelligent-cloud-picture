package com.chengfu.yunpictureapi.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/*
 * 用户注册请求类
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 5200000L;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;

    /*
     * 校验密码
     * */
    private String checkPassword;

}
