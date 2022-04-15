package com.ezenac.thunder_market.repository;

import com.ezenac.thunder_market.member.entity.QFavorite;
import com.ezenac.thunder_market.entity.Product;
import com.ezenac.thunder_market.entity.ProductState;
import com.ezenac.thunder_market.product.entity.QProduct;
import com.ezenac.thunder_market.entity.SmallGroup;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class ProductRepositoryImpl extends QuerydslRepositorySupport implements ProductRepositoryCustom {

    public ProductRepositoryImpl() {
        super(Product.class);
    }

    public Page<Object[]> getSearchList(String keyword, SmallGroup smallGroup, Pageable pageable) {
        QProduct product = QProduct.product;
        QFavorite favorite = QFavorite.favorite;

        JPQLQuery<Product> jpqlQuery = from(product).leftJoin(favorite).on(favorite.product.eq(product));
        JPQLQuery<Tuple> tuple = jpqlQuery.select(product, favorite.count());

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
        tuple.where(booleanBuilder);
        tuple.groupBy(product);

        Long count = tuple.fetchCount();

        List<Tuple> result = getQuerydsl().applyPagination(pageable, tuple).fetch();

        return new PageImpl<Object[]>(result.stream().map(Tuple::toArray).collect(Collectors.toList()), pageable, count);
    }

}
