package com.ezenac.thunder_market.utils;

import com.ezenac.thunder_market.product.entity.ProductImage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class FileUploadUtil {

    @Value("${com.ezenac.thunder_market.upload.path}")
    private String uploadPath;

    public File getImage(String filePath) {
        return new File(uploadPath + File.separator + filePath);
    }

    public Map<String, String> saveFile(MultipartFile file, String folderPath) throws IOException {
        // 유효성 검사
        if (!file.getContentType().startsWith("image")) {
            log.warn("이미지 파일이 아닙니다.");
            throw new IOException();
        }
        // 파일이름이 전체 경로로 왔을 경우 경로 삭제
        String originalFilename = file.getOriginalFilename();
        String fileName = originalFilename.substring(originalFilename.lastIndexOf("\\") + 1);

        log.info("fileName => " + fileName);
        // 멤버아이디로 폴더 생성
        String path = makeFolder(folderPath);
        // uuid 생성
        String uuid = UUID.randomUUID().toString();
        // 전체 경로
        String fullPath = uploadPath + File.separator + path + File.separator + uuid + "_" + fileName;

        save(file, fullPath);
        Map<String, String> map = new HashMap<>();
        map.put("path", path);
        map.put("uuid", uuid);
        map.put("fileName", fileName);
        return map;

    }

    private String makeFolder(String folderPath) {

        File uploadPathFolder = new File(uploadPath, folderPath);

        if (!uploadPathFolder.exists()) {
            uploadPathFolder.mkdirs();
        }
        return folderPath;
    }

    // 이미지 리사이즈 후 저장
    private void save(MultipartFile file, String saveName) throws IOException {

        Image image = ImageIO.read(file.getInputStream());
        // 이미지 가로 세로 측정
        int originWidth = image.getWidth(null);
        int originHeight = image.getHeight(null);
        // 변경할 세로 길이
        int newHeight = 1100;
        // 비율 유지하며 가로 길이 설정
        //int newWidth = (originWidth * newHeight) / originHeight;
        int newWidth = 1100;

        Image resizeImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);

        BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

        Graphics graphics = newImage.getGraphics();
        graphics.drawImage(resizeImage, 0, 0, null);
        graphics.dispose();
        File newFile = new File(saveName);
        ImageIO.write(newImage, "png", newFile);

    }

    // 이미지 파일 삭제
    public boolean removeFile(ProductImage productImage) {

        String filePath = uploadPath + File.separator + productImage.getPath() + File.separator + productImage.getUuid() + "_" + productImage.getImageName();
        File file = new File(filePath);
        return file.delete();
    }
}
