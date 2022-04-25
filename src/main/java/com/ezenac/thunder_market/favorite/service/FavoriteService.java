package com.ezenac.thunder_market.favorite.service;

public interface FavoriteService {

    boolean isFavorite(String memberId, Long productId);

    void add(String memberId, Long productId);

    void remove(String memberId, Long productId);

    Long count(Long productId);
}
