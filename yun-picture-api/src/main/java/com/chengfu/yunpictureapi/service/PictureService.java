package com.chengfu.yunpictureapi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chengfu.yunpictureapi.api.aliyunai.model.CreateOutPaintingTaskResponse;
import com.chengfu.yunpictureapi.model.dto.picture.*;
import com.chengfu.yunpictureapi.model.entity.Picture;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chengfu.yunpictureapi.model.entity.User;
import com.chengfu.yunpictureapi.model.vo.picture.PictureVO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author Lenovo
* @description 针对表【picture(图片)】的数据库操作Service
* @createDate 2025-06-23 11:35:45
*/
public interface PictureService extends IService<Picture> {


    /**
     * 上传图片
     *
     * @param inputSource          文件输入源
     * @param pictureUploadRequest 图片上传请求
     * @param loginUser 用户登录
     * @return 图片封装类
     */
    PictureVO uploadPicture(Object inputSource,
                            PictureUploadRequest pictureUploadRequest,
                            User loginUser);

    /*
     * 将查询条件封装成QueryWrapper
     * @param pictureQueryRequest 图片查询请求
     * @return QueryWrapper 图片查询条件
     * */
    QueryWrapper<Picture> getQueryWrapper(PictureQueryRequest pictureQueryRequest);

    /*
     * 获取单个图片封装类
     * @param picture 图片实体类
     * @return PictureVO 图片封装类
     * */
    PictureVO getPictureVo(Picture picture, HttpServletRequest httpServletRequest);

    /**
     * @param picturePage
     * @param request
     * @return
     */
    Page<PictureVO> getPictureVOPage(Page<Picture> picturePage, HttpServletRequest request);

    void validPicture(Picture picture);

    /*
     * */
    boolean doPictureReview(PictureReviewRequest pictureReviewRequest, User loginUser);

    void fillReviewParams(Picture picture, User loginUser);

    /**
     * 批量抓取和创建图片
     *
     * @param pictureUploadByBatchRequest
     * @param loginUser
     * @return 成功创建的图片数
     */
    Integer uploadPictureByBatch(
            PictureUploadByBatchRequest pictureUploadByBatchRequest,
            User loginUser
    );

    // void checkPictureAuth(User loginUser, Picture picture);

    boolean deletePicture(long pictureId, User loginUser);

    @Async
    void clearPictureFile(Picture oldPicture);

    void editPicture(PictureEditRequest pictureEditRequest, User loginUser);

    List<PictureVO> searchPictureByColor(Long spaceId, String picColor, User loginUser);

    @Transactional(rollbackFor = Exception.class)
    void editPictureByBatch(PictureEditByBatchRequest pictureEditByBatchRequest, User loginUser);

    /**
     * AI 扩展图片服务
     *
     * @param createPictureOutPaintingTaskRequest
     * @param loginUser 登录的用户
     * @return
     */
    CreateOutPaintingTaskResponse createPictureOutPaintingTask(CreatePictureOutPaintingTaskRequest createPictureOutPaintingTaskRequest, User loginUser);

}
