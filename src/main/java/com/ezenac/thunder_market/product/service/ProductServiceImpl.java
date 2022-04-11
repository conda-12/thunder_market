package com.ezenac.thunder_market.product.service;

import com.ezenac.thunder_market.product.domain.Product;
import com.ezenac.thunder_market.product.domain.ProductImage;
import com.ezenac.thunder_market.product.domain.SmallGroup;
import com.ezenac.thunder_market.product.dto.PageRequestDTO;
import com.ezenac.thunder_market.product.dto.ProductDTO;
import com.ezenac.thunder_market.product.dto.RegisterDTO;
import com.ezenac.thunder_market.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
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
    public Long register(RegisterDTO registerDTO) {
        Product product = dtoToEntity(registerDTO);

        for (MultipartFile file : registerDTO.getFiles()) {
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
            String folderPath = makeFolder(registerDTO.getMemberId());
            // uuid 생성
            String uuid = UUID.randomUUID().toString();
            // 전체 경로
            String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;
            // todo 파일 사이즈 변환하여 저장하기
            try {
                // 파일 저장
                file.transferTo(new File(saveName));
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
        Optional<Object> result = productRepository.readWithFavorite(id);
        if (result.isPresent()) {
            Object[] arr = (Object[]) result.get();
            return entityToDTO((Product) arr[0], (Long) arr[1]);
        }
        return null;
    }

    @Override
    public File getImage(String filePath) {
        return new File(uploadPath + File.separator + filePath);
    }


}
