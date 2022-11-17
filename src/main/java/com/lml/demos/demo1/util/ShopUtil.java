package com.lml.demos.demo1.util;

/**
 * @author ：limaolin
 * @date ：Created in 2022/11/16 11:23
 * @description：
 * @modified By：
 */

public class ShopUtil {
    public static Double getDisCount(String type) {
        try {
            if ("PDD".equals(type)) {
                Thread.sleep(900);
                return 0.5;
            }
            if ("JD".equals(type)) {
                Thread.sleep(1000);
                return 0.9;
            }
            if ("TB".equals(type)) {
                Thread.sleep(1100);
                return 0.8;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("不存在的类型");
    }


    public static Integer getPrice(String type) {

        try {
            if ("PDD".equals(type)) {
                Thread.sleep(1000);
                return 500;
            }
            if ("JD".equals(type)) {
                Thread.sleep(1100);
                return 600;
            }
            if ("TB".equals(type)) {
                Thread.sleep(1000);
                return 550;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("不存在的类型");
    }
}
