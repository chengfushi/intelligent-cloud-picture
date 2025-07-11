package com.chengfu.yunpictureapi.model.dto.spaceuser;

import lombok.Data;

import java.io.Serializable;

/**
 * @BelongsProject: yun-picture-api
 * @BelongsPackage: com.chengfu.yunpictureapi.model.dto.spaceuser
 * @Author: Chengfu Shi
 * @CreateTime: 2025-07-06 14:33
 * @Description: 空间成员编辑请求，给管理员用，可以修改成员角色
 * @Version: 1.0
 **/
@Data
public class SpaceUserEditRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 空间角色：viewer/editor/admin
     */
    private String spaceRole;

    private static final long serialVersionUID = 1L;
}


