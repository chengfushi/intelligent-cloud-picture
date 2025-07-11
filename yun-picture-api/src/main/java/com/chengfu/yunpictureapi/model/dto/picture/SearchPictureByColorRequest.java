package com.chengfu.yunpictureapi.model.dto.picture;

import lombok.Data;

import java.io.Serializable;

/**
 * @BelongsProject: yun-picture-api
 * @BelongsPackage: com.chengfu.yunpictureapi.model.dto.picture
 * @Author: Chengfu Shi
 * @CreateTime: 2025-07-02 18:26
 * @Description: TODO
 * @Version: 1.0
 **/
@Data
public class SearchPictureByColorRequest implements Serializable {

    /**
     * 图片主色调
     */
    private String picColor;

    /**
     * 空间 id
     */
    private Long spaceId;

    private static final long serialVersionUID = 1L;
}


