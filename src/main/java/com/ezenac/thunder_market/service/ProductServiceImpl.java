package com.ezenac.thunder_market.service;

import com.ezenac.thunder_market.entity.Product;
import com.ezenac.thunder_market.entity.ProductImage;
import com.ezenac.thunder_market.entity.SmallGroup;
import com.ezenac.thunder_market.dto.PageRequestDTO;
import com.ezenac.thunder_market.dto.ProductDTO;
import com.ezenac.thunder_market.dto.ProductRegisterDTO;
import com.ezenac.thunder_market.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Value("${com.ezenac.thunder_market.upload.path}")
    private String uploadPath;

    @Transactional
    @Override
    public Long register(ProductRegisterDTO productRegisterDTO) {
        Product product = dtoToEntity(productRegisterDTO);

        for (MultipartFile file : productRegisterDTO.getFiles()) {
            // 유효성 검사
            if (!file.getContentType().startsWith("image")) {
                log.warn("this files is not imageFile");
                continue;   // 이미지 파일이 아닐시 저장하지 않음
            }

            String originalFilename = file.getOriginalFilename();
            // 파일이름이 전체 경로로 왔을 경우 경로 삭제
            String fileName = originalFilename.substring(originalFilename.lastIndexOf("\\") + 1);

            log.info("fileName => " + fileName);

            // 멤버아이디로 폴더 생성
            String folderPath = makeFolder(productRegisterDTO.getMemberId());
            // uuid 생성
            String uuid = UUID.randomUUID().toString();
            // 전체 경로
            String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;

            try {
                saveFile(file, saveName);
            } catch (IOException e) {
                log.warn("파일 저장에 실패했습니다. fileName => " + fileName);
                e.printStackTrace();
            }
            ProductImage image = ProductImage.builder().imageName(fileName).path(folderPath).uuid(uuid).product(product).build();
            product.setImage(image);
        }
        productRepository.save(product);
        log.info("product => " + product);
        return product.getId();
    }

    private String makeFolder(String memberId) {
        File uploadPathFolder = new File(uploadPath, memberId);

        if (!uploadPathFolder.exists()) {
            uploadPathFolder.mkdirs();
        }
        return memberId;
    }
    // 이미지 리사이즈 후 저
    private void saveFile(MultipartFile file, String saveName) throws IOException {

        Image image = ImageIO.read(file.getInputStream());
        // 이미지 가로 세로 측정
        int originWidth = image.getWidth(null);
        int originHeight = image.getHeight(null);
        // 변경할 세로 길이
        int newHeight = 1100;
        // 비율 유지하며 가로 길이 설정
        int newWidth = (originWidth * newHeight) / originHeight;

        Image resizeImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);

        BufferedImage newImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

        Graphics graphics = newImage.getGraphics();
        graphics.drawImage(resizeImage, 0, 0, null);
        graphics.dispose();
        File newFile = new File(saveName);
        ImageIO.write(newImage, "png", newFile);

    }

    @Transactional
    @Override
    public List<ProductDTO> list(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("id").descending());

        Page<Object[]> result = productRepository.getList(pageable);

        return result.stream().map(row -> entityToDTO((Product) row[0], (Long) row[1])).collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> searchList(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("id").descending());
        String keyword = pageRequestDTO.getKeyword();
        String sgNum = pageRequestDTO.getSgNum();
        SmallGroup smallGroup = null;
        if (!sgNum.equals("")) {
            smallGroup = SmallGroup.builder().sgNum(sgNum).build();
        }
        Page<Object[]> result = productRepository.getSearchList(keyword, smallGroup, pageable);
        return result.stream().map(row -> entityToDTO((Product) row[0], (Long) row[1])).collect(Collectors.toList());
    }

    @Override
    public ProductDTO read(Long id) {
        int i = productRepository.updateHit(id);
        if (i > 0) {
            Object result = productRepository.readWithFavorite(id);
            Object[] arr = (Object[]) result;
            return entityToDTO((Product) arr[0], (Long) arr[1]);
        }
        return null;
    }

    @Transactional
    @Override
    public void modify(Product product) {
        productRepository.save(product);
    }

    @Transactional
    @Override
    public void remove(Long id) {
        // todo 찜하기 삭제 후
        productRepository.deleteById(id);
    }

    @Override
    public File getImage(String filePath) {
        return new File(uploadPath + File.separator + filePath);
    }


}
