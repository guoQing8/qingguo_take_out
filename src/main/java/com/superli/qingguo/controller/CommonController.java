package com.superli.qingguo.controller;

import com.superli.qingguo.common.R;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * @Author superli
 * @Description
 * @Date 2022/5/1 11:27
 */
@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {
    @Value("${qingguo.path}")
    private String basePath;
    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        log.info("2"+file.toString());
        //原始文件名
        String originalFilename = file.getOriginalFilename();
        String fileLast = originalFilename.substring(originalFilename.lastIndexOf("."));
        //判断文件路径是否存在
        File dir = new File(basePath);
        if(!dir.exists()){
            dir.mkdirs();
        }

        //使用uuid生成文件名,防止文件名重复
        String fileName = UUID.randomUUID().toString();
        try {
            file.transferTo(new File(basePath+fileName+fileLast));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName+fileLast);

    }

    /**
     *  文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            //输入流,读取文件
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

            //输出流,在浏览器展示
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            int len=0;
            byte[] bytes=new byte[1024];
            while((len=fileInputStream.read(bytes))!=-1){
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
