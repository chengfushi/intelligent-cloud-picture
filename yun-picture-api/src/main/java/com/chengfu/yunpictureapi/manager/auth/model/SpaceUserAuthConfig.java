package com.chengfu.yunpictureapi.manager.auth.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @BelongsProject: yun-picture-api
 * @BelongsPackage: com.chengfu.yunpictureapi.auth.model
 * @Author: Chengfu Shi
 * @CreateTime: 2025-07-07 13:13
 * @Description: 接收配置文件
 * @Version: 1.0
 **/
@Data
public class SpaceUserAuthConfig implements Serializable {

    /**
     * 权限列表
     */
    private List<SpaceUserPermission> permissions;

    /**
     * 角色列表
     */
    private List<SpaceUserRole> roles;

    private static final long serialVersionUID = 1L;
}


