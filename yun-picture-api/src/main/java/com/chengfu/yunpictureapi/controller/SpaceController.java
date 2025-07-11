package com.chengfu.yunpictureapi.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chengfu.yunpictureapi.annotation.AuthCheck;
import com.chengfu.yunpictureapi.common.BaseResponse;
import com.chengfu.yunpictureapi.common.DeleteRequest;
import com.chengfu.yunpictureapi.common.ResultUtils;
import com.chengfu.yunpictureapi.constant.UserConstant;
import com.chengfu.yunpictureapi.exception.BusinessException;
import com.chengfu.yunpictureapi.exception.ErrorCode;
import com.chengfu.yunpictureapi.exception.ThrowUtils;
import com.chengfu.yunpictureapi.manager.auth.SpaceUserAuthManager;
import com.chengfu.yunpictureapi.manager.auth.annotation.SaSpaceCheckPermission;
import com.chengfu.yunpictureapi.manager.auth.model.SpaceUserPermissionConstant;
import com.chengfu.yunpictureapi.model.dto.space.SpaceAddRequest;
import com.chengfu.yunpictureapi.model.dto.space.SpaceEditRequest;
import com.chengfu.yunpictureapi.model.dto.space.SpaceQueryRequest;
import com.chengfu.yunpictureapi.model.dto.space.SpaceUpdateRequest;
import com.chengfu.yunpictureapi.model.entity.Space;
import com.chengfu.yunpictureapi.model.entity.User;
import com.chengfu.yunpictureapi.model.enums.SpaceLevelEnum;
import com.chengfu.yunpictureapi.model.vo.space.SpaceLevel;
import com.chengfu.yunpictureapi.model.vo.space.SpaceVO;
import com.chengfu.yunpictureapi.service.SpaceService;
import com.chengfu.yunpictureapi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @BelongsProject: yun-picture-api
 * @BelongsPackage: com.chengfu.yunpictureapi.controller
 * @Author: Chengfu Shi
 * @CreateTime: 2025-06-30 18:51
 * @Description: 空间控制类
 * @Version: 1.0
 **/
@RestController
@Slf4j
@RequestMapping("/space")
public class SpaceController {
    @Resource
    private SpaceService spaceService;

    @Resource
    private UserService userService;

    @Resource
    private SpaceUserAuthManager spaceUserAuthManager;

    @PostMapping("/add")
    public BaseResponse<Long> addSpace(@RequestBody SpaceAddRequest spaceAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(spaceAddRequest == null, ErrorCode.PARAMS_ERROR, "参数不能为空");
        User user = userService.getLoginUser(request);

        Long newId = spaceService.addSpace(spaceAddRequest, user);
        return ResultUtils.success(newId);
    }

    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateSpace(@RequestBody SpaceUpdateRequest spaceUpdateRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(spaceUpdateRequest == null || spaceUpdateRequest.getId() < 0,
                ErrorCode.PARAMS_ERROR, "请求参数错误");

        // 将实体类和DTO类进行转换
        Space space = new Space();
        BeanUtils.copyProperties(spaceUpdateRequest, space);

        // 自动填充数据
        spaceService.fillSpaceBySpaceLevel(space);
        // 参数校验
        spaceService.vaildSpace(space, false);

        // 判断空间是否存在
        ThrowUtils.throwIf(spaceService.getById(space.getId()) == null, ErrorCode.NOT_FOUND_ERROR, "空间不存在");

        boolean result = spaceService.updateById(space);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(result);
    }

    @PostMapping("/delete")
    @SaSpaceCheckPermission(value = SpaceUserPermissionConstant.SPACE_USER_MANAGE)
    public BaseResponse<Boolean> deleteSpace(@RequestBody DeleteRequest deleteRequest
            , HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Long id = deleteRequest.getId();
        // 判断是否存在
        Space oldSpace = spaceService.getById(id);
        ThrowUtils.throwIf(oldSpace == null, ErrorCode.NOT_FOUND_ERROR);

        // 操作数据库
        boolean result = spaceService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取空间（仅管理员可用）
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Space> getSpaceById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Space space = spaceService.getById(id);
        ThrowUtils.throwIf(space == null, ErrorCode.NOT_FOUND_ERROR);
        // 获取封装类
        return ResultUtils.success(space);
    }

    /**
     * 根据 id 获取空间（封装类）
     */
    @GetMapping("/get/vo")
    public BaseResponse<SpaceVO> getSpaceVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Space space = spaceService.getById(id);
        ThrowUtils.throwIf(space == null, ErrorCode.NOT_FOUND_ERROR);
        SpaceVO spaceVO = spaceService.getSpaceVO(space, request);
        User loginUser = userService.getLoginUser(request);

        List<String> permissionList = spaceUserAuthManager.getPermissionList(space, loginUser);
        spaceVO.setPermissionList(permissionList);

        // 获取封装类
        return ResultUtils.success(spaceVO);
    }

    /**
     * 分页获取空间列表（仅管理员可用）
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Space>> listSpaceByPage(@RequestBody SpaceQueryRequest spaceQueryRequest) {
        long current = spaceQueryRequest.getCurrent();
        long size = spaceQueryRequest.getPageSize();
        // 查询数据库
        Page<Space> spacePage = spaceService.page(new Page<>(current, size),
                spaceService.getQueryWrapper(spaceQueryRequest));
        return ResultUtils.success(spacePage);
    }

    /**
     * 分页获取空间列表（封装类）
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<SpaceVO>> listSpaceVOByPage(@RequestBody SpaceQueryRequest spaceQueryRequest,
                                                         HttpServletRequest request) {
        long current = spaceQueryRequest.getCurrent();
        long size = spaceQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<Space> spacePage = spaceService.page(new Page<>(current, size),
                spaceService.getQueryWrapper(spaceQueryRequest));
        // 获取封装类
        return ResultUtils.success(spaceService.getSpaceVOPage(spacePage, request));
    }

    /**
     * 编辑空间（给用户使用）
     */
    // @PostMapping("/edit")
    // public BaseResponse<Boolean> editSpace(@RequestBody SpaceEditRequest spaceEditRequest, HttpServletRequest request) {
    //     if (spaceEditRequest == null || spaceEditRequest.getId() <= 0) {
    //         throw new BusinessException(ErrorCode.PARAMS_ERROR);
    //     }
    //
    //     //判断是否存在
    //     long id = spaceEditRequest.getId();
    //     Space oldSpace = spaceService.getById(id);
    //     ThrowUtils.throwIf(oldSpace == null, ErrorCode.NOT_FOUND_ERROR);
    //
    //     User loginUser = userService.getLoginUser(request);
    //
    //     // 仅本人或管理员可编辑
    //     spaceService.checkSpaceAuth(loginUser, oldSpace);
    //
    //     //直接将数据更新
    //     Space space = new Space();
    //     BeanUtils.copyProperties(spaceEditRequest, space);
    //
    //     boolean result = spaceService.updateById(space);
    //     ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
    //     return ResultUtils.success(true);
    // }

    /**
     * 编辑空间（给用户使用）
     */
    @PostMapping("/edit")
    @SaSpaceCheckPermission(value = SpaceUserPermissionConstant.SPACE_USER_MANAGE)
    public BaseResponse<Boolean> editSpace(@RequestBody SpaceEditRequest spaceEditRequest, HttpServletRequest request) {
        if (spaceEditRequest == null || spaceEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 在此处将实体类和 DTO 进行转换
        Space space = new Space();
        BeanUtils.copyProperties(spaceEditRequest, space);
        // 自动填充数据
        spaceService.fillSpaceBySpaceLevel(space);
        // 设置编辑时间
        space.setEditTime(new Date());
        // 数据校验
        spaceService.vaildSpace(space, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = spaceEditRequest.getId();
        Space oldSpace = spaceService.getById(id);
        ThrowUtils.throwIf(oldSpace == null, ErrorCode.NOT_FOUND_ERROR);
        // // 仅本人或管理员可编辑
        // spaceService.checkSpaceAuth(loginUser, oldSpace);
        // 操作数据库
        boolean result = spaceService.updateById(space);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 获取空间级别列表，便于前端展示
     *
     * @return
     */
    @GetMapping("/list/level")
    public BaseResponse<List<SpaceLevel>> listSpaceLevel() {
        List<SpaceLevel> spaceLevelList = Arrays.stream(SpaceLevelEnum.values())
                .map(spaceLevelEnum -> new SpaceLevel(
                        spaceLevelEnum.getValue(),
                        spaceLevelEnum.getText(),
                        spaceLevelEnum.getMaxCount(),
                        spaceLevelEnum.getMaxSize()
                ))
                .collect(Collectors.toList());
        return ResultUtils.success(spaceLevelList);
    }


}

