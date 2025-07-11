package com.chengfu.yunpictureapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chengfu.yunpictureapi.model.dto.user.UserAddRequest;
import com.chengfu.yunpictureapi.model.dto.user.UserQueryRequest;
import com.chengfu.yunpictureapi.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chengfu.yunpictureapi.model.vo.user.LoginUserVO;
import com.chengfu.yunpictureapi.model.vo.user.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author Lenovo
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2025-06-21 14:37:56
*/
public interface UserService extends IService<User> {

    /*
    * 注册逻辑
    * @param userAccount 账号
    * @param userPassword 密码
    * @param checkPassword 校验密码
    * @return Long 用户ID
    * */
    Long userRegister(String userAccount, String userPassword, String checkPassword);

    /*
    * 对密码加密
    *
    * @param userPassword 密码
    * @return String 加密后的密码
    * */
    String getEncryptPassword(String userPassword);


    /*
    * 登录逻辑
    * @param userAccount 账号
    * @param userPassword 密码
    * @param  httpServlet 登录态
    * @return LoginUserVO 登录用户信息
    * */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    LoginUserVO getLoginUserVO(User user);

    User getLoginUser(HttpServletRequest request);

    boolean logout(HttpServletRequest request);

    UserVO getUserVO(User user);

    List<UserVO> getUserVOList(List<User> userList);

    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    boolean isAdmin(User user);
}
