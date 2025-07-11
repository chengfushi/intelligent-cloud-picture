package com.chengfu.yunpictureapi.model.dto.spaceuser;

import lombok.Data;

import java.io.Serializable;

/**
 * @BelongsProject: yun-picture-api
 * @BelongsPackage: com.chengfu.yunpictureapi.model.dto.spaceuser
 * @Author: Chengfu Shi
 * @CreateTime: 2025-07-06 14:32
 * @Description: 空间成员添加请求，给空间管理员用
 * @Version: 1.0
 **/
@Data
public class SpaceUserAddRequest implements Serializable {

    /**
     * 空间 ID
     */
    private Long spaceId;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 空间角色：viewer/editor/admin
     */
    private String spaceRole;

    private static final long serialVersionUID = 1L;
}


