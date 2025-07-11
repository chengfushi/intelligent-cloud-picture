package com.chengfu.yunpictureapi.model.dto.analyze;

import lombok.Data;

import java.io.Serializable;

/**
 * @BelongsProject: yun-picture-api
 * @BelongsPackage: com.chengfu.yunpictureapi.model.dto.analyze
 * @Author: Chengfu Shi
 * @CreateTime: 2025-07-05 14:24
 * @Description: 图库分析通用请求类
 * @Version: 1.0
 **/
@Data
public class SpaceAnalyzeRequest implements Serializable {

    /**
     * 空间 ID
     */
    private Long spaceId;

    /**
     * 是否查询公共图库
     */
    private boolean queryPublic;

    /**
     * 全空间分析
     */
    private boolean queryAll;

    private static final long serialVersionUID = 1L;
}


