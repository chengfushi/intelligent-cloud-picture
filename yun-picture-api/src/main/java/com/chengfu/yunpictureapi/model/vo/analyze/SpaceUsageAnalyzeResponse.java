package com.chengfu.yunpictureapi.model.vo.analyze;

import lombok.Data;

import java.io.Serializable;

/**
 * @BelongsProject: yun-picture-api
 * @BelongsPackage: com.chengfu.yunpictureapi.model.vo.analyze
 * @Author: Chengfu Shi
 * @CreateTime: 2025-07-05 14:48
 * @Description: 空间资源使用响应类
 * @Version: 1.0
 **/
@Data
public class SpaceUsageAnalyzeResponse implements Serializable {

    /**
     * 已使用大小
     */
    private Long usedSize;

    /**
     * 总大小
     */
    private Long maxSize;

    /**
     * 空间使用比例
     */
    private Double sizeUsageRatio;

    /**
     * 当前图片数量
     */
    private Long usedCount;

    /**
     * 最大图片数量
     */
    private Long maxCount;

    /**
     * 图片数量占比
     */
    private Double countUsageRatio;

    private static final long serialVersionUID = 1L;
}


