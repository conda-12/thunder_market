package com.ezenac.thunder_market.product.dto;

import com.ezenac.thunder_market.member.entity.Member;
import com.ezenac.thunder_market.group.entity.SmallGroup;
import com.ezenac.thunder_market.product.entity.Product;
import com.ezenac.thunder_market.product.entity.ProductState;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
public class ProductRegisterDTO {

    private Long productId;

    private List<MultipartFile> files;

    private String title;

    private String sgNum;

    private String address;

    private int price;

    private String content;

    private String memberId;

    public Product toEntity() {

        Member member = Member.builder().memberId(this.getMemberId()).build();

        return Product.builder()
                .title(title)
                .price(price)
                .content(content)
                .address(address)
                .smallGroup(SmallGroup.builder().sgNum(sgNum).build())
                .member(member)
                .state(ProductState.SELLING)
                .build();

    }
}
