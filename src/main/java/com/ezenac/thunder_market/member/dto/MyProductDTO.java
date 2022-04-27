package com.ezenac.thunder_market.member.dto;

import com.ezenac.thunder_market.product.entity.Product;
import com.ezenac.thunder_market.product.entity.ProductState;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
public class MyProductDTO {

    private static final long SEC = 60;
    private static final long MIN = 60;
    private static final long HOUR = 24;
    private static final long DAY = 30;
    private static final long MONTH = 12;
    private Long productId;

    private String title;

    private int price;

    private LocalDateTime regDate;

    private String imageURL;

    private ProductState state;

    public String getRegDateString() {

        long diffTime = regDate.until(LocalDateTime.now(), ChronoUnit.SECONDS);
        String msg = null;
        if (diffTime < SEC){
            return diffTime + "초전";
        }
        diffTime = diffTime / SEC;
        if (diffTime < MIN) {
            return diffTime + "분 전";
        }
        diffTime = diffTime / MIN;
        if (diffTime < HOUR) {
            return diffTime + "시간 전";
        }
        diffTime = diffTime / HOUR;
        if (diffTime < DAY) {
            return diffTime + "일 전";
        }
        diffTime = diffTime / DAY;
        if (diffTime < MONTH) {
            return diffTime + "개월 전";
        }

        diffTime = diffTime / MONTH;
        return diffTime + "년 전";
    }

    public MyProductDTO(Product entity) {
        this.productId = entity.getProductId();
        this.title = entity.getTitle();
        this.price = entity.getPrice();
        this.regDate = entity.getRegDate();
        this.imageURL = entity.getImages().get(0).toDto().getImageURL();
        this.state = entity.getState();
    }
}
