package com.ezenac.thunder_market.dto;

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
public class ProductRegisterDTO {

    private Long id;

    private List<MultipartFile> files;

    private String title;

    private String sgNum;

    private String address;

    private int price;

    private String content;

    private String memberId;
}
