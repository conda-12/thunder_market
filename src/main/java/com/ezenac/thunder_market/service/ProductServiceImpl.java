package com.ezenac.thunder_market.service;

import com.ezenac.thunder_market.dto.PageRequestDTO;
import com.ezenac.thunder_market.dto.ProductDTO;
import com.ezenac.thunder_market.dto.ProductRegisterDTO;
import com.ezenac.thunder_market.entity.Product;
import com.ezenac.thunder_market.entity.ProductImage;
import com.ezenac.thunder_market.entity.SmallGroup;
import com.ezenac.thunder_market.repository.ProductImageRepository;
import com.ezenac.thunder_market.repository.ProductRepository;
import com.ezenac.thunder_market.utils.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductImageRepository imageRepository;
    private final FileUploadUtil fileUploadUtil;


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

            try {
                Map<String, String> fileInfo = fileUploadUtil.saveFile(file, productRegisterDTO.getMemberId());

                ProductImage image = ProductImage.builder()
                        .imageName(fileInfo.get("fileName"))
                        .path(fileInfo.get("path"))
                        .uuid(fileInfo.get("uuid"))
                        .product(product)
                        .build();
                product.setImage(image);

            } catch (IOException e) {
                log.warn("파일저장에 실패했습니다 => " + file.getName());
                e.printStackTrace();
            }

        }
        productRepository.save(product);
        log.info("product => " + product);
        return product.getId();
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

    @Transactional
    @Override
    public ProductDTO read(Long id) {
        productRepository.updateHit(id);
        Optional<Object> result = productRepository.readWithFavorite(id);

        if (result.isPresent()) {
            Object[] arr = (Object[]) result.get();
            return entityToDTO((Product) arr[0], (Long) arr[1]);
        }
        return null;
    }

    @Override
    public ProductDTO modifyGet(Long id) {
        Optional<Product> result = productRepository.findById(id);
        return result.map(product -> entityToDTO(product, null)).orElse(null);
    }

    @Modifying
    @Override
    public Long modifyPost(Product product) {
        return null;
    }

    @Transactional
    @Override
    public void remove(Long id) {
        // todo 찜하기 삭제 후
        productRepository.deleteById(id);
    }

    @Override
    public File getImage(String filePath) {
        return fileUploadUtil.getImage(filePath);
    }

    @Override
    public void changeImage(Long imageId, MultipartFile file) {
        Optional<ProductImage> result = imageRepository.findById(imageId);
        if (result.isEmpty()) {
            return;
        }
        // 파일 삭제
        ProductImage productImage = result.get();
        fileUploadUtil.removeFile(productImage);
        // 새 이미지 저장
        try {
            Map<String, String> fileInfo = fileUploadUtil.saveFile(file, productImage.getPath());
            String path = fileInfo.get("path");
            String uuid = fileInfo.get("uuid");
            String fileName = fileInfo.get("fileName");
            productImage.changeFile(path, uuid, fileName);
            imageRepository.save(productImage);
                   } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Transactional
    @Override
    public void removeImage(Long imageId) {
        Optional<ProductImage> result = imageRepository.findById(imageId);
        if (result.isEmpty()) {
            return;
        }
        // 파일 삭제
        ProductImage productImage = result.get();
        if (fileUploadUtil.removeFile(productImage)) {
            // 데이터베이스 삭제
            imageRepository.deleteById(imageId);

        }
    }


    @Transactional
    @Override
    public Boolean authorityValidate(Long id, String memberId) {
        Optional<Product> result = productRepository.findById(id);
        if (result.isEmpty()) {
            return false;
        }
        Product product = result.get();
        return memberId.equals(product.getMember().getMemberId());
    }


}
