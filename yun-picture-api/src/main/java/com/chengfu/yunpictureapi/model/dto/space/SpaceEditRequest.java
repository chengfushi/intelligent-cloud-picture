package com.chengfu.yunpictureapi.model.dto.space;

import lombok.Data;

import java.io.Serializable;

/**
 * @BelongsProject: yun-picture-api
 * @BelongsPackage: com.chengfu.yunpictureapi.model.dto.space
 * @Author: Chengfu Shi
 * @CreateTime: 2025-06-30 17:58
 * @Description: 编辑空间请求
 * @Version: 1.0
 **/
@Data
public class SpaceEditRequest implements Serializable {

    /**
     * 空间 id
     */
    private Long id;

    /**
     * 空间名称
     */
    private String spaceName;


    private static final long serialVersionUID = 1L;
}


