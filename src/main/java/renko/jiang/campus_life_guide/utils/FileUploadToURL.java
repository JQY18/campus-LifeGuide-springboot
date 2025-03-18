package renko.jiang.campus_life_guide.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author 86132
 */

@Component
public class FileUploadToURL {
    // 文件上传路径
    @Value("${upload.path}")
    private String path;


    public String handleFileUpload(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("No file uploaded");
        }
        String url = "";
        try {
            if (!file.isEmpty()) {
                try {
                    // 生成UUID
                    String uuid = UUID.randomUUID().toString();
                    // 文件扩展名
                    String originalFilename = file.getOriginalFilename();
                    String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                    // 构建新的文件名
                    String newFileName = uuid + fileExtension;

                    // 构建文件路径
                    Path path = Paths.get(this.path, newFileName);
                    // 将MultipartFile写入文件系统
                    Files.write(path, file.getBytes());

                    // 收集该帖子id对应的图片的静态资源映射地址
                    url = "http://localhost:8080/image/" + newFileName;

                    System.out.println("File saved successfully at: " + path.toString());
                } catch (IOException e) {
                    throw new RuntimeException("Failed to save file", e);
                }
            }
        }catch (RuntimeException e){
            throw new RuntimeException("上传失败");
        }
        return url;
    }


    /**
     * 处理多文件上传
     *
     * @param files
     * @return
     */
    public List<String> handleMultipleFileUpload(List<MultipartFile> files) {
        // 存储上传的图片的路径
        List<String> images = new ArrayList<>();
        if (files == null || files.isEmpty()) {
            throw new RuntimeException("No files uploaded");
        }
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    // 生成UUID
                    String uuid = UUID.randomUUID().toString();
                    // 文件扩展名
                    String originalFilename = file.getOriginalFilename();
                    String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                    // 构建新的文件名
                    String newFileName = uuid + fileExtension;

                    // 构建文件路径
                    Path path = Paths.get(this.path, newFileName);
                    // 将MultipartFile写入文件系统
                    Files.write(path, file.getBytes());

                    // 收集该帖子id对应的图片的静态资源映射地址
                    images.add("http://localhost:8080/image/" + newFileName);

                    System.out.println("File saved successfully at: " + path.toString());
                } catch (IOException e) {
                    throw new RuntimeException("Failed to save file", e);
                }
            }
        }
        // 将生成的静态资源映射地址返回
        return images;
    }
}
