package com.lml.demos.demo1.service;

import com.lml.demos.demo1.util.PriceResult;

import java.util.Comparator;
import java.util.concurrent.*;
import java.util.stream.Stream;

import static com.lml.demos.demo1.util.ShopUtil.getDisCount;
import static com.lml.demos.demo1.util.ShopUtil.getPrice;

/**
 * @author ：limaolin
 * @date ：Created in 2022/11/15 16:28
 * @description：
 * @modified By：
 */

public class ShopServiceImpl implements ShopService {
    @Override
    public void calculatePrice() throws Exception {
        PriceResult pdd = ShopServiceImpl.computeRealPrice("PDD");
        PriceResult jd = ShopServiceImpl.computeRealPrice("JD");
        PriceResult tb = ShopServiceImpl.computeRealPrice("TB");
        PriceResult result = Stream.of(pdd, jd, tb).min((o1, o2) -> o1.getPrice().compareTo(o2.getPrice())).get();
        System.out.println("最低价格平台为" + result.getName() + "价格为" + result.getPrice());
    }

    @Override
    public void calculatePriceByFuture() throws ExecutionException, InterruptedException, TimeoutException {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        Future<PriceResult> pddFuture = threadPool.submit(() -> ShopServiceImpl.computeRealPrice("PDD"));
        Future<PriceResult> jdFuture = threadPool.submit(() -> ShopServiceImpl.computeRealPrice("JD"));
        Future<PriceResult> tbFuture = threadPool.submit(() -> ShopServiceImpl.computeRealPrice("TB"));
        PriceResult result = Stream.of(pddFuture.get(), jdFuture.get(), tbFuture.get()).min(Comparator.comparing(PriceResult::getPrice)).get();
        System.out.println("最低价格平台为" + result.getName() + "价格为" + result.getPrice());
    }


    @Override
    public void calculatePriceByCompletableFuture() throws Exception {

        CompletableFuture<PriceResult> pddFuture = CompletableFuture
                .supplyAsync(() -> getPrice("PDD"))
                .thenCombine(CompletableFuture.supplyAsync(() -> getDisCount("PDD")), (price, disCount) -> ShopServiceImpl.computeRealPriceTT("PDD", disCount, price));

        CompletableFuture<PriceResult> jdFuture = CompletableFuture
                .supplyAsync(() -> getPrice("JD"))
                .thenCombine(CompletableFuture.supplyAsync(() -> getDisCount("JD")), (price, disCount) -> ShopServiceImpl.computeRealPriceTT("JD", disCount, price));

        CompletableFuture<PriceResult> tbFuture = CompletableFuture
                .supplyAsync(() -> getPrice("TB"))
                .thenCombine(CompletableFuture.supplyAsync(() -> getDisCount("TB")), (price, disCount) -> ShopServiceImpl.computeRealPriceTT("TB", disCount, price));

        PriceResult result = Stream.of(pddFuture.get(), jdFuture.get(), tbFuture.get()).min(((o1, o2) -> o1.getPrice().compareTo(o2.getPrice()))).get();
        System.out.println("最低价格平台为" + result.getName() + "价格为" + result.getPrice());
    }


    public static PriceResult computeRealPriceTT(String type, Double disCount, Integer price) {

        try {
            if ("PDD".equals(type)) {
                PriceResult pdd = new PriceResult();
                pdd.setName("PDD");
                pdd.setOldPrice(price);
                System.out.println(Thread.currentThread().getName() + "获取PDD原价成功，" + "价格为" + price);
                pdd.setDisCount(disCount);
                System.out.println(Thread.currentThread().getName() + "获取PDD折扣成功，" + "折扣为" + disCount);
                pdd.setPrice(disCount * price);
                System.out.println(Thread.currentThread().getName() + "PDD折扣后的价格为" + disCount * price);
                return pdd;
            }
            if ("JD".equals(type)) {
                PriceResult jd = new PriceResult();
                jd.setName("JD");
                jd.setOldPrice(price);
                System.out.println(Thread.currentThread().getName() + "获取JD原价成功，" + "价格为" + price);
                jd.setDisCount(disCount);
                System.out.println(Thread.currentThread().getName() + "获取JD折扣成功，" + "折扣为" + disCount);
                jd.setPrice(disCount * price);
                System.out.println(Thread.currentThread().getName() + "JD折扣后的价格为" + disCount * price);
                return jd;
            }
            if ("TB".equals(type)) {
                PriceResult tb = new PriceResult();
                tb.setName("TB");
                tb.setOldPrice(price);
                System.out.println(Thread.currentThread().getName() + "获取TB原价成功，" + "价格为" + price);
                tb.setDisCount(disCount);
                System.out.println(Thread.currentThread().getName() + "获取TB折扣成功，" + "折扣为" + disCount);
                tb.setPrice(disCount * price);
                System.out.println(Thread.currentThread().getName() + "TB折扣后的价格为" + disCount * price);
                return tb;

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        throw new RuntimeException("不存在的类型");
    }

    public static PriceResult computeRealPrice(String type) {
        try {
            if ("PDD".equals(type)) {
                PriceResult pdd = new PriceResult();
                pdd.setName("PDD");
                Integer price = getPrice(type);
                pdd.setOldPrice(price);
                System.out.println(Thread.currentThread().getName() + "获取PDD原价成功，" + "价格为" + price);
                Double disCount = getDisCount(type);
                pdd.setDisCount(disCount);
                System.out.println(Thread.currentThread().getName() + "获取PDD折扣成功，" + "折扣为" + disCount);
                pdd.setPrice(disCount * price);
                System.out.println(Thread.currentThread().getName() + "PDD折扣后的价格为" + disCount * price);
                return pdd;
            }
            if ("JD".equals(type)) {
                PriceResult jd = new PriceResult();
                jd.setName("JD");
                Integer price = getPrice(type);
                jd.setOldPrice(price);
                System.out.println(Thread.currentThread().getName() + "获取JD原价成功，" + "价格为" + price);
                Double disCount = getDisCount(type);
                jd.setDisCount(disCount);
                System.out.println(Thread.currentThread().getName() + "获取JD折扣成功，" + "折扣为" + disCount);
                jd.setPrice(disCount * price);
                System.out.println(Thread.currentThread().getName() + "JD折扣后的价格为" + disCount * price);
                return jd;
            }
            if ("TB".equals(type)) {
                PriceResult tb = new PriceResult();
                tb.setName("TB");
                Integer price = getPrice(type);
                tb.setOldPrice(price);
                System.out.println(Thread.currentThread().getName() + "获取TB原价成功，" + "价格为" + price);
                Double disCount = getDisCount(type);
                tb.setDisCount(disCount);
                System.out.println(Thread.currentThread().getName() + "获取TB折扣成功，" + "折扣为" + disCount);
                tb.setPrice(disCount * price);
                System.out.println(Thread.currentThread().getName() + "TB折扣后的价格为" + disCount * price);
                return tb;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("不存在的类型");
    }

}
