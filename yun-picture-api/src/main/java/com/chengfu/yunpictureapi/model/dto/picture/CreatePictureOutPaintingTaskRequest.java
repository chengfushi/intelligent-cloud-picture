package com.chengfu.yunpictureapi.model.dto.picture;

import com.chengfu.yunpictureapi.api.aliyunai.model.CreateOutPaintingTaskRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * @BelongsProject: yun-picture-api
 * @BelongsPackage: com.chengfu.yunpictureapi.model.dto.picture
 * @Author: Chengfu Shi
 * @CreateTime: 2025-07-07 16:29
 * @Description: TODO
 * @Version: 1.0
 **/
@Data
public class CreatePictureOutPaintingTaskRequest implements Serializable {

    /**
     * 图片 id
     */
    private Long pictureId;

    /**
     * 扩图参数
     */
    private CreateOutPaintingTaskRequest.Parameters parameters;

    private static final long serialVersionUID = 1L;
}



