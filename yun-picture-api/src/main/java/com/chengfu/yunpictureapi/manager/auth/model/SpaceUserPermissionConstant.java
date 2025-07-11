package com.chengfu.yunpictureapi.manager.auth.model;

/**
 * @BelongsProject: yun-picture-api
 * @BelongsPackage: com.chengfu.yunpictureapi.auth
 * @Author: Chengfu Shi
 * @CreateTime: 2025-07-07 13:15
 * @Description: TODO
 * @Version: 1.0
 **/
public interface SpaceUserPermissionConstant {
    /**
     * 空间用户管理权限
     */
    String SPACE_USER_MANAGE = "spaceUser:manage";

    /**
     * 图片查看权限
     */
    String PICTURE_VIEW = "picture:view";

    /**
     * 图片上传权限
     */
    String PICTURE_UPLOAD = "picture:upload";

    /**
     * 图片编辑权限
     */
    String PICTURE_EDIT = "picture:edit";

    /**
     * 图片删除权限
     */
    String PICTURE_DELETE = "picture:delete";
}

