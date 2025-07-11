package com.chengfu.yunpictureapi.manager.auth.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @BelongsProject: yun-picture-api
 * @BelongsPackage: com.chengfu.yunpictureapi.auth.model
 * @Author: Chengfu Shi
 * @CreateTime: 2025-07-07 13:14
 * @Description: TODO
 * @Version: 1.0
 **/
@Data
public class SpaceUserPermission implements Serializable {

    /**
     * 权限键
     */
    private String key;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限描述
     */
    private String description;

    private static final long serialVersionUID = 1L;

}


