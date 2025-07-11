package com.chengfu.yunpictureapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chengfu.yunpictureapi.model.dto.spaceuser.SpaceUserAddRequest;
import com.chengfu.yunpictureapi.model.dto.spaceuser.SpaceUserQueryRequest;
import com.chengfu.yunpictureapi.model.entity.SpaceUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chengfu.yunpictureapi.model.vo.spaceuser.SpaceUserVO;

import java.util.List;

/**
* @author Lenovo
* @description 针对表【space_user(空间用户关联)】的数据库操作Service
* @createDate 2025-07-06 13:53:23
*/
public interface SpaceUserService extends IService<SpaceUser> {

    long addSpaceUser(SpaceUserAddRequest spaceUserAddRequest);

    void validSpaceUser(SpaceUser spaceUser, boolean add);

    QueryWrapper<SpaceUser> getQueryWrapper(SpaceUserQueryRequest spaceUserQueryRequest);

    List<SpaceUserVO> getSpaceUserVOList(List<SpaceUser> spaceUserList);
}
