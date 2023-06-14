package com.reggie.controller;

import com.reggie.common.R;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Lyx
 * @date 2023-06-10-4:36
 * @description 文件上传与下载
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    // 读取配置文件中配置的存储路径，赋给basePath变量
    @Value("${reggie.path}")
    private String basePath;

    //文件上传
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        //file 是一个临时文件，需要转存到指定位置，否则请求完成后临时文件会删除
        //log.info("file:{}",file.toString());

        //原始文件名
        //getOriginalFilename方法可以获取上传文件的原始名字
        String originalFilename = file.getOriginalFilename();
        //以最后一个.开始截取后面的部分
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用UUID随机生成文件名，防止因为文件名相同造成文件覆盖
        String fileName = UUID.randomUUID().toString()+suffix;

        //创建一个目录对象
        File dir = new File(basePath);
        //判断当前目录是否存在
        if(!dir.exists()){
            //目录不存在，则创建目录
            dir.mkdirs();
        }

        try {
            //将临时文件转存到指定位置
            file.transferTo(new File(basePath+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        FileInputStream fileInputStream = null;
        ServletOutputStream outputStream = null;
        try {
            //输入流，通过输入流读取文件内容
            fileInputStream = new FileInputStream(new File(basePath + name));

            //输出流，通过输出流将文件写回浏览器
            //注意输出流不能自己创建，要通过response来获取，这样才能输出到浏览器
            outputStream = response.getOutputStream();

            //以图片的形式响应到浏览器
            response.setContentType("image/jpeg");
            
            int len = 0;
            byte[] bytes = new byte[1024];
            // 每次读取一个数组长度的字节后立马输出，然后继续读取下一个数组长度的字节继续输出
            // read方法返回读取字节的长度，当读取完毕时，返回-1
            while ((len = fileInputStream.read(bytes)) != -1){ //
                outputStream.write(bytes,0,len);
                outputStream.flush();// 刷新缓存
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

