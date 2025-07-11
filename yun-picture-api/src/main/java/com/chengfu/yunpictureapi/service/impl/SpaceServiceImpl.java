package com.chengfu.yunpictureapi.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chengfu.yunpictureapi.exception.BusinessException;
import com.chengfu.yunpictureapi.exception.ErrorCode;
import com.chengfu.yunpictureapi.exception.ThrowUtils;
import com.chengfu.yunpictureapi.manager.sharding.DynamicShardingManager;
import com.chengfu.yunpictureapi.model.dto.space.SpaceAddRequest;
import com.chengfu.yunpictureapi.model.dto.space.SpaceQueryRequest;
import com.chengfu.yunpictureapi.model.entity.Space;
import com.chengfu.yunpictureapi.model.entity.SpaceUser;
import com.chengfu.yunpictureapi.model.entity.User;
import com.chengfu.yunpictureapi.model.enums.SpaceLevelEnum;
import com.chengfu.yunpictureapi.model.enums.SpaceRoleEnum;
import com.chengfu.yunpictureapi.model.enums.SpaceTypeEnum;
import com.chengfu.yunpictureapi.model.vo.space.SpaceVO;
import com.chengfu.yunpictureapi.model.vo.user.UserVO;
import com.chengfu.yunpictureapi.service.SpaceService;
import com.chengfu.yunpictureapi.mapper.SpaceMapper;
import com.chengfu.yunpictureapi.service.SpaceUserService;
import com.chengfu.yunpictureapi.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author Lenovo
* @description 针对表【space(空间)】的数据库操作Service实现
* @createDate 2025-06-30 17:34:36
*/
@Service
public class SpaceServiceImpl extends ServiceImpl<SpaceMapper, Space>
    implements SpaceService{

    @Resource
    private UserService userService;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    @Lazy
    private SpaceUserService spaceUserService;

    // @Resource
    // private DynamicShardingManager dynamicShardingManager;

    @Override
    public void vaildSpace(Space space, boolean add) {
        // 1. 校验参数
        ThrowUtils.throwIf(space == null, ErrorCode.PARAMS_ERROR, "请求参数异常");

        // 取值
        String spaceName = space.getSpaceName();
        Integer spaceLevel = space.getSpaceLevel();
        Integer spaceType = space.getSpaceType();

        SpaceTypeEnum spaceTypeEnum = SpaceTypeEnum.getEnumByValue(spaceType);

        SpaceLevelEnum spaceLevelEnum = SpaceLevelEnum.getEnumByValue(spaceLevel);

        // 2. 如果是创建,那么名称和级别不能为空
        if (add) {
            ThrowUtils.throwIf(spaceName == null, ErrorCode.PARAMS_ERROR, "空间名不能为空");
            ThrowUtils.throwIf(spaceLevelEnum == null, ErrorCode.PARAMS_ERROR, "空间级别不能为空");
            ThrowUtils.throwIf(spaceTypeEnum == null, ErrorCode.PARAMS_ERROR, "空间类型不能为空");
        }
        // 3. 如果是编辑,则判断空间级别是否存在,再验证长度

        ThrowUtils.throwIf(spaceLevel != null &&  spaceLevelEnum == null, ErrorCode.PARAMS_ERROR, "空间级别不存在");

        // 验证空间长度
        ThrowUtils.throwIf(StrUtil.isNotBlank(spaceName) && spaceName.length() > 30, ErrorCode.PARAMS_ERROR, "请求参数过长");
        // 修改数据时，如果要改空间级别
        if (spaceType != null && spaceTypeEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "空间类型不存在");
        }
    }

    @Override
    public void fillSpaceBySpaceLevel(Space space) {
        Integer spaceLevel = space.getSpaceLevel();
        SpaceLevelEnum spaceLevelEnum = SpaceLevelEnum.getEnumByValue(spaceLevel);

        if (spaceLevelEnum != null) {
            long maxSize = spaceLevelEnum.getMaxSize();

            if (space.getMaxSize() == null) {
                space.setMaxSize(maxSize);
            }

            long maxCount = spaceLevelEnum.getMaxCount();
            if (space.getMaxCount() == null) {
                space.setMaxCount(maxCount);
            }
        }

    }

    @Override
    public QueryWrapper<Space> getQueryWrapper(SpaceQueryRequest spaceQueryRequest) {
        QueryWrapper<Space> queryWrapper = new QueryWrapper<>();
        // 如果查询条件为空，直接返回
        if (spaceQueryRequest == null) {
            return queryWrapper;
        }

        // 从字段中取值
        Long id = spaceQueryRequest.getId();
        Long userId = spaceQueryRequest.getUserId();
        String spaceName = spaceQueryRequest.getSpaceName();
        Integer spaceLevel = spaceQueryRequest.getSpaceLevel();
        String sortField = spaceQueryRequest.getSortField();
        String sortOrder = spaceQueryRequest.getSortOrder();

        Integer spaceType = spaceQueryRequest.getSpaceType();
        queryWrapper.eq(ObjUtil.isNotEmpty(spaceType), "spaceType", spaceType);
        queryWrapper.eq(ObjUtil.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjUtil.isNotEmpty(userId), "userId", userId);
        queryWrapper.like(ObjUtil.isNotEmpty(spaceName), "spaceName", spaceName);
        queryWrapper.eq(ObjUtil.isNotEmpty(spaceLevel), "spaceLevel", spaceLevel);

        // 排序
        queryWrapper.orderBy(StrUtil.isNotEmpty(sortField), "ascend".equals(sortOrder), sortField);
        return queryWrapper;
    }

    @Override
    public SpaceVO getSpaceVO(Space space, HttpServletRequest request) {
        // 对象转封装类
        SpaceVO spaceVO = SpaceVO.objToVo(space);
        // 关联查询用户信息
        Long userId = space.getUserId();
        if (userId != null && userId > 0) {
            User user = userService.getById(userId);
            UserVO userVO = userService.getUserVO(user);
            spaceVO.setUser(userVO);
        }
        return spaceVO;
    }

    public Page<SpaceVO> getSpaceVOPage(Page<Space> spacePage, HttpServletRequest request) {
        List<Space> spaceList = spacePage.getRecords();

        Page<SpaceVO> spaceVOPage = new Page<>(spacePage.getCurrent(), spacePage.getSize());

        if (CollUtil.isEmpty(spaceList)) {
            return spaceVOPage;
        }

        // 对象列表=>封装类列表
        List<SpaceVO> spaceVOList = spaceList.stream().map(SpaceVO::objToVo).collect(Collectors.toList());

        // 关联查询用户信息
        Set<Long> userIdSet = spaceVOList.stream().map(SpaceVO::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));

        // 填充信息
        spaceVOList.forEach(spaceVO -> {
            Long userId = spaceVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            spaceVO.setUser(userService.getUserVO(user));
        });

        spaceVOPage.setRecords(spaceVOList);
        return spaceVOPage;
    }

    @Override
    public long addSpace(SpaceAddRequest spaceAddRequest, User loginUser) {
        // 实体类和DTO转换
        Space space = new Space();
        BeanUtils.copyProperties(spaceAddRequest, space);

        // 默认值
        if (StrUtil.isBlank(spaceAddRequest.getSpaceName())) {
            space.setSpaceName("默认空间");
        }

        if (spaceAddRequest.getSpaceLevel() == null) {
            space.setSpaceLevel(SpaceLevelEnum.COMMON.getValue());
        }
        if (spaceAddRequest.getSpaceType() == null){
            space.setSpaceType(SpaceTypeEnum.PRIVATE.getValue());
        }

        Long userId = loginUser.getId();

        // 填充数据
        this.fillSpaceBySpaceLevel(space);
        space.setUserId(userId);

        // 数据校验
        this.vaildSpace(space, true);

        if (SpaceLevelEnum.COMMON.getValue() != spaceAddRequest.getSpaceLevel() && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限创建指定级别的空间");
        }

        // 锁 + 事务确保一个用户只能创建一个空间
        String lock = String.valueOf(userId).intern();

        synchronized (lock) {
            Long newSpaceId = transactionTemplate.execute(status -> {
                if (!userService.isAdmin(loginUser)) {
                    boolean exists = this.lambdaQuery()
                            .eq(Space::getUserId, userId)
                            .eq(Space::getSpaceType, spaceAddRequest.getSpaceType())
                            .exists();
                    ThrowUtils.throwIf(exists, ErrorCode.OPERATION_ERROR, "每个用户每类空间仅能创建一个");
                }
                // 写入数据库
                boolean save = this.save(space);
                ThrowUtils.throwIf(!save, ErrorCode.OPERATION_ERROR, "创建空间失败，未知错误");

                // 如果是团队空间，关联新增团队成员记录
                if (SpaceTypeEnum.TEAM.getValue() == spaceAddRequest.getSpaceType()) {
                    SpaceUser spaceUser = new SpaceUser();
                    spaceUser.setSpaceId(space.getId());
                    spaceUser.setUserId(userId);
                    spaceUser.setSpaceRole(SpaceRoleEnum.ADMIN.getValue());
                    save = spaceUserService.save(spaceUser);
                    ThrowUtils.throwIf(!save, ErrorCode.OPERATION_ERROR, "创建团队成员记录失败");

                }

                // dynamicShardingManager.createSpacePictureTable(space);
                // 返回空间id
                return space.getId();
            });
            return newSpaceId;
        }
    }

    // @Override
    // public void checkSpaceAuth(User loginUser, Space space) {
    //     // 仅本人或管理员可编辑
    //     if (!space.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
    //         throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
    //     }
    // }


}




