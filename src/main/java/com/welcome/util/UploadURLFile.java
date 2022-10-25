package com.welcome.util;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

public class UploadURLFile {
    /**
     * 上传文件到云端
     * @param accessKey key1
     * @param secretKey key2
     * @param bucket 文件空间名
     * @param filePath 源文件路径(绝对)
     * @param key 目标路径(相对云端)
     */
    public static void upload(String accessKey,String secretKey,String bucket,String filePath,String key){
        /*
         * Configuration 表示带指定Zone对象的配置类
         * 其中Zone 2 表示华南地区
         * */
        Configuration cfg = new Configuration(Zone.zone2());
        UploadManager uploadManager = new UploadManager(cfg);
//        String accessKey = "79LhOgymSKmxhJd-QbvGz5rQwzDYgt4jyRrJQ3vs";
//        String secretKey ="6s_9viFla083tAkocjTXe4yETrXnCvxSY6XEgSJ6";
//        String bucket= "bugger";
//        String key = "welcome/imgs/test3.jpg";
        Auth auth = Auth.create(accessKey,secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(filePath,key,upToken);
            new Gson().fromJson(response.bodyString(),DefaultPutRet.class);
        } catch (QiniuException e) {
            e.printStackTrace();
        }
    }
}
