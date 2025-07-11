package com.chengfu.yunpictureapi.model.dto.spaceuser;

import lombok.Data;

import java.io.Serializable;

/**
 * @BelongsProject: yun-picture-api
 * @BelongsPackage: com.chengfu.yunpictureapi.model.dto.spaceuser
 * @Author: Chengfu Shi
 * @CreateTime: 2025-07-06 14:34
 * @Description: 查询空间成员请求，这里不分页
 * @Version: 1.0
 **/
@Data
public class SpaceUserQueryRequest implements Serializable {

    /**
     * ID
     */
    private Long id;

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


