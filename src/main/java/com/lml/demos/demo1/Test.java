package com.lml.demos.demo1;


import cn.hutool.core.date.StopWatch;
import com.lml.demos.demo1.service.ShopService;
import com.lml.demos.demo1.service.ShopServiceImpl;

/**
 * @author ：limaolin
 * @date ：Created in 2022/11/15 14:23
 * @description： 需求描述：
 * 实现一个全网比价服务，比如可以从某宝、某东、某夕夕去获取某个商品的价格、优惠金额，并计算出实际付款金额，最终返回价格最优的平台与价格信息
 * @modified By：
 */

public class Test {

    public static void main(String[] args) {
        StopWatch sw = new StopWatch();
        ShopService sp = new ShopServiceImpl();
        try {
            sw.start();
            sp.calculatePrice();
            sw.stop();
            System.out.println("串行执行costTime:" + sw.getLastTaskTimeMillis());
            sw.start();
            sp.calculatePriceByFuture();
            sw.stop();
            System.out.println("线程池+Future执行costTime:" + sw.getLastTaskTimeMillis());
            sw.start();
            sp.calculatePriceByCompletableFuture();
            sw.stop();
            System.out.println("线程池+CompletableFuture执行costTime:" + sw.getLastTaskTimeMillis());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
