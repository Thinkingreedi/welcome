package com.welcome.controller;

import com.welcome.util.DownloadURLFile;
import com.welcome.util.UploadURLFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件的上传和下载
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    @Value("${welcome.temp-path}")
    private String basePath;
    @Value("${welcome.cloud-image.path}")
    private String cloudPath;
    @Value("${welcome.cloud-image.accessKey}")
    private String accessKey;
    @Value("${welcome.cloud-image.secretKey}")
    private String secretKey;
    @Value("${welcome.cloud-image.key}")
    private String key;
    @Value("${welcome.temp-file}")
    private String tempFile;
    @Value("${welcome.cloud-image.bucket}")
    private String bucket;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){
        //file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除
        //原始文件名
        String originalFilename = file.getOriginalFilename();//abc.jpg
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用UUID重新生成文件名，防止文件名称重复造成文件覆盖
        String fileName = UUID.randomUUID().toString() + suffix;//dfsdfdfd.jpg
        //创建一个目录对象
        File dir = new File(basePath);
        //判断当前目录是否存在
        if(!dir.exists()){
            //目录不存在，需要创建
            dir.mkdirs();
        }

        try {
            //将临时文件转存到指定位置
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 存到本地供后台快速访问,再存一份到云端供小程序访问
        UploadURLFile.upload(accessKey,secretKey,bucket,basePath + fileName,key + fileName);
        return Result.success(fileName);
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        DownloadURLFile downloadURLFile = new DownloadURLFile();
        try {
            // 判断本地是否有这个文件,不存在先下载
            File file = new File(basePath+name);
            if(!file.exists()){
                // 用下载工具将图片下载到本地临时文件夹
                downloadURLFile.down(cloudPath + key + name ,basePath, tempFile);
            }
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}