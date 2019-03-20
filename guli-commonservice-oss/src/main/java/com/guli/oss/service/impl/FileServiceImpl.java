package com.guli.oss.service.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CannedAccessControlList;
import com.guli.common.constants.ResultCodeEnum;
import com.guli.common.exception.GuliException;
import com.guli.oss.service.FileService;
import com.guli.oss.util.ConstantPropertiesUtil;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String upload(MultipartFile file) {
        String uploadUrl = null;
        try {
            //获取阿里云配置信息
            String endPoint = ConstantPropertiesUtil.END_POINT;
            String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
            String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
            String bucketName = ConstantPropertiesUtil.BUCKET_NAME;
            String fileHost = ConstantPropertiesUtil.FILE_HOST;

            // 创建OSSClient实例。
            OSSClient ossClient = new OSSClient(endPoint, accessKeyId, accessKeySecret);
            if (!ossClient.doesBucketExist(bucketName)) {
                //创建bucket
                ossClient.createBucket(bucketName);
                //设置权限:公共读
                ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            }
            //获取文件流
            InputStream inputStream = file.getInputStream();

            //构建日期路径：avatar/2019/02/28/文件名
            String filePath = new DateTime().toString("yyyy/MM/dd");

            //文件名：uuid.扩展名
            String originalFilename = file.getOriginalFilename();
            String fileName = UUID.randomUUID().toString();
            String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFileName = fileName + fileType;
            String fileUrl = fileHost + "/" + filePath + "/" + newFileName;


            //文件上传至阿里云
            ossClient.putObject(bucketName, fileUrl, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            //返回文件的完整url值也即获取url地址
            uploadUrl = "https://" + bucketName + "." + endPoint + "/" + fileUrl;

        } catch (IOException e) {
            e.printStackTrace();
            throw  new GuliException(ResultCodeEnum.FILE_UPLOAD_ERROR);
        }


        return uploadUrl;
    }
}
