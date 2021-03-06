package com.ezenac.thunder_market.product.repository;

import com.ezenac.thunder_market.group.entity.SmallGroup;
import com.ezenac.thunder_market.product.entity.Product;
import com.ezenac.thunder_market.product.entity.ProductState;
import com.ezenac.thunder_market.product.entity.QProduct;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

@Slf4j
public class ProductRepositoryImpl extends QuerydslRepositorySupport implements ProductRepositoryCustom {

    public ProductRepositoryImpl() {
        super(Product.class);
    }

    public Page<Product> getSearchList(String keyword, SmallGroup smallGroup, Pageable pageable) {
        QProduct product = QProduct.product;

        JPQLQuery<Product> jpqlQuery = from(product);

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression booleanExpression = product.state.eq(ProductState.SELLING);
        booleanBuilder.and(booleanExpression);

        if (!keyword.equals("")) {
            BooleanExpression keywordSearch = product.title.contains(keyword).or(product.content.contains(keyword));
            booleanBuilder.and(keywordSearch);
        }
        if (smallGroup != null) {
            BooleanExpression cateSearch = product.smallGroup.eq(smallGroup);
            booleanBuilder.and(cateSearch);
        }
        jpqlQuery.where(booleanBuilder);


        long count = jpqlQuery.fetchCount();
        List<Product> result = getQuerydsl().applyPagination(pageable, jpqlQuery).fetch();

        return new PageImpl<Product>(result, pageable, count);
    }

}
