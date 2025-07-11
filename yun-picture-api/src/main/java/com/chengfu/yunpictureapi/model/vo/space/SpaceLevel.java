package com.chengfu.yunpictureapi.model.vo.space;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @BelongsProject: yun-picture-api
 * @BelongsPackage: com.chengfu.yunpictureapi.model.vo.space
 * @Author: Chengfu Shi
 * @CreateTime: 2025-06-30 19:33
 * @Description: TODO
 * @Version: 1.0
 **/
@Data
@AllArgsConstructor
public class SpaceLevel {

    private int value;

    private String text;

    private long maxCount;

    private long maxSize;
}

