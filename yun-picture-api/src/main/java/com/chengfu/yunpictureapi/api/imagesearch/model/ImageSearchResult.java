package com.chengfu.yunpictureapi.api.imagesearch.model;

import lombok.Data;

/**
 * @BelongsProject: yun-picture-api
 * @BelongsPackage: com.chengfu.yunpictureapi.api.imagesearch.model
 * @Author: Chengfu Shi
 * @CreateTime: 2025-07-02 17:10
 * @Description: 以图搜图返回值
 * @Version: 1.0
 **/
@Data
public class ImageSearchResult {

    /**
     * 缩略图地址
     */
    private String thumbUrl;

    /**
     * 来源地址
     */
    private String fromUrl;
}


