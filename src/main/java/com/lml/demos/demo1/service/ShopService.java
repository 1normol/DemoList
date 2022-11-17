package com.lml.demos.demo1.service;

public interface ShopService {

    void calculatePrice() throws Exception;

    void calculatePriceByFuture() throws Exception;

    void calculatePriceByCompletableFuture() throws Exception;


}
