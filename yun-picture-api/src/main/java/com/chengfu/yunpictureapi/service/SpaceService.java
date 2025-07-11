package com.chengfu.yunpictureapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chengfu.yunpictureapi.model.dto.space.SpaceAddRequest;
import com.chengfu.yunpictureapi.model.dto.space.SpaceQueryRequest;
import com.chengfu.yunpictureapi.model.entity.Space;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chengfu.yunpictureapi.model.entity.User;
import com.chengfu.yunpictureapi.model.vo.space.SpaceVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author Lenovo
* @description 针对表【space(空间)】的数据库操作Service
* @createDate 2025-06-30 17:34:36
*/
public interface SpaceService extends IService<Space> {

    /**
     * @param add 判断是否是创建还是编辑
     * @description: 校验空间参数是否合法
     * @author: Chengfu Shi
     * @date: 2025/6/30 18:11
     * @param: space 校验空间参数
     * @return: void
     **/
    void vaildSpace(Space space, boolean add);

    /*
     * @description: 根据空间级别填充数据
     * @author: Chengfu Shi
     * @date: 2025/6/30 18:25
     * @param: space 空间
     * @return: void
     **/
    void fillSpaceBySpaceLevel(Space space);

    QueryWrapper<Space> getQueryWrapper(SpaceQueryRequest spaceQueryRequest);

    SpaceVO getSpaceVO(Space space, HttpServletRequest request);

    long addSpace(SpaceAddRequest spaceAddRequest, User loginUser);

    // void checkSpaceAuth(User loginUser, Space oldSpace);

    Page<SpaceVO> getSpaceVOPage(Page<Space> spacePage, HttpServletRequest request);
}
