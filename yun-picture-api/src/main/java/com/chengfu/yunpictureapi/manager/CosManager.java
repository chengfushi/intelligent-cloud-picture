package com.chengfu.yunpictureapi.manager;

import cn.hutool.core.io.FileUtil;
import com.chengfu.yunpictureapi.config.CosClientConfig;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.ciModel.persistence.PicOperations;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
@Data
public class CosManager {
    @Resource
    private CosClientConfig cosClientConfig;

    @Resource
    private COSClient cosClient;



    // 将本地文件上传到 COS
    public PutObjectResult putObject(String key, File file)
            throws CosClientException, CosServiceException {
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosClientConfig.getBucket(),key,file);
        return cosClient.putObject(putObjectRequest);
    }

    /**
     * 下载对象
     *
     * @param key 唯一键
     */
    public COSObject getObject(String key) {
        GetObjectRequest getObjectRequest = new GetObjectRequest(cosClientConfig.getBucket(), key);
        return cosClient.getObject(getObjectRequest);
    }

    /**
     * 上传对象（附带图片信息）
     *
     * @param key  唯一键
     * @param file 文件
     */
    public PutObjectResult putPictureObject(String key, File file) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosClientConfig.getBucket(), key,
                file);
        // 对图片进行处理（获取基本信息也被视作为一种处理）
        PicOperations picOperations = new PicOperations();
        // 1 表示返回原图信息
        picOperations.setIsPicInfo(1);

        List< PicOperations.Rule> compressRules = new ArrayList<>();

        // 图片压缩，转化为webp 格式
        String webpKey = FileUtil.getName(key) + ".webp";
        PicOperations.Rule compressRule = new PicOperations.Rule();
        compressRule.setRule("imageMogr2/format/webp");
        compressRule.setBucket(cosClientConfig.getBucket());
        compressRule.setFileId(webpKey);
        compressRules.add(compressRule);

        //图片缩略图处理,仅对大于20kb
        if (file.length() > 20 * 1024) {
            PicOperations.Rule thumbnailRule = new PicOperations.Rule();
            String thumbnailKet = FileUtil.getName(key) + "_thumbnail" + FileUtil.getSuffix(key);
            thumbnailRule.setRule(String.format("imageMogr2/thumbnail/%sx%s", 256, 256));
            thumbnailRule.setBucket(cosClientConfig.getBucket());
            thumbnailRule.setFileId(thumbnailKet);
            compressRules.add(thumbnailRule);
        }

        // 构造处理参数
        picOperations.setRules(compressRules);
        putObjectRequest.setPicOperations(picOperations);
        return cosClient.putObject(putObjectRequest);
    }

    /**
     * @description: 删除文件
     * @author: Chengfu Shi
     * @date: 2025/6/29 17:41
     * @param: key 需要删除的文件
     * @return: void
     **/
    public void deleteObject(String key) throws CosClientException{
        cosClient.deleteObject(cosClientConfig.getBucket(),key);
    }

}
