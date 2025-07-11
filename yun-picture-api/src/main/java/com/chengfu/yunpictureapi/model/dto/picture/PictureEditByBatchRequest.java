package com.chengfu.yunpictureapi.model.dto.picture;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @BelongsProject: yun-picture-api
 * @BelongsPackage: com.chengfu.yunpictureapi.model.dto.picture
 * @Author: Chengfu Shi
 * @CreateTime: 2025-07-02 20:46
 * @Description: 批量编辑图片
 * @Version: 1.0
 **/
@Data
public class PictureEditByBatchRequest implements Serializable {

    /**
     * 图片 id 列表
     */
    private List<Long> pictureIdList;

    /**
     * 空间 id
     */
    private Long spaceId;

    /**
     * 分类
     */
    private String category;

    /**
     * 标签
     */
    private List<String> tags;

    /**
     * 命名规则
     */
    private String nameRule;


    private static final long serialVersionUID = 1L;
}


