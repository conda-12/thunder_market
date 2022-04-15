package com.ezenac.thunder_market.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {
    // todo productDTO registerDTO 통합하기
    // 지네릭을 T로 바꾸고 리스트 이름을 바꾸고 뷰에서 인풋 이름을 바꾼다.
    // 꼭 테스트 할 것
    private List<MultipartFile> files;

    private String title;

    private String bgNum;

    private String sgNum;

    private String address;

    private int price;

    private String content;

    private String memberId;
}
