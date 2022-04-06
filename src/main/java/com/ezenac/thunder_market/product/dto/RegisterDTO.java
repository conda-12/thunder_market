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

    private List<MultipartFile> files;

    private String title;

    private String bgNum;

    private String sgNum;

    private String address;

    private int price;

    private String content;

    private String memberId;
}
