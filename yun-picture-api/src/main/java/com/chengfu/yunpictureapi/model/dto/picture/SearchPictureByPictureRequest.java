package com.chengfu.yunpictureapi.model.dto.picture;

import lombok.Data;

import java.io.Serializable;

/**
 * @BelongsProject: yun-picture-api
 * @BelongsPackage: com.chengfu.yunpictureapi.model.dto.picture
 * @Author: Chengfu Shi
 * @CreateTime: 2025-07-02 17:18
 * @Description: 以图搜图请求类
 * @Version: 1.0
 **/
@Data
public class SearchPictureByPictureRequest implements Serializable {

    /**
     * 图片 id
     */
    private Long pictureId;

    private static final long serialVersionUID = 1L;
}


