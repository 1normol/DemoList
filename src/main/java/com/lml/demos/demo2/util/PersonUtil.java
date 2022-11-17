package com.lml.demos.demo2.util;

/**
 * @author ：limaolin
 * @date ：Created in 2022/11/16 15:27
 * @description：
 * @modified By：
 */

public class PersonUtil {

    public static Person sleep(Person person) {
        System.out.println(person.getName() + "开始睡觉");
        person.setState("睡着的");
        System.out.println(person.getName() + "现在的状态:" + person);
        return person;
    }

    public static Person takeShower(Person person) {
        System.out.println(person.getName() + "开始洗澡");
        person.setState("干净的");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(person.getName() + "现在的状态:" + person);
        return person;
    }

    public static Person playGame(Person person) {
        System.out.println(person.getName() + "正在玩游戏");
        person.setState("脏的");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(person.getName() + "现在的状态:" + person);
        return person;
    }
}
