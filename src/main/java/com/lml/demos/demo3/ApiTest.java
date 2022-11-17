package com.lml.demos.demo3;

import com.lml.demos.demo3.util.Hobby;
import com.lml.demos.demo3.util.Person;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ：limaolin
 * @date ：Created in 2022/11/17 15:16
 * @description：
 * @modified By：
 */

public class ApiTest {
    List<Hobby> hobbyList = Arrays.asList(
            new Hobby("play", "玩游戏玩游戏玩游戏"),
            new Hobby("watch", "看电视剧看电视剧"),
            new Hobby("exercise", "锻炼锻炼锻炼"));

    List<Hobby> hobbyList1 = Arrays.asList(
            new Hobby("play", "玩游戏玩游戏玩游戏"),
            new Hobby("work", "上班狂人"));

    List<Hobby> hobbyList2 = Arrays.asList(
            new Hobby("play", "玩游戏玩游戏玩游戏"),
            new Hobby("watch", "看电视剧看电视剧"));

    List<Person> personList = Arrays.asList(new Person("小明", 18, hobbyList),
            new Person("小王", 20, hobbyList1),
            new Person("小红", 16, hobbyList2));
    Person ming = new Person("小明", 18, hobbyList);
    Person wang = new Person("小王", 20, hobbyList1);
    Person hong = new Person("小红", 16, hobbyList2);

    //开始管道
    @Test
    public void t1() {
        List<Person> list = personList.stream().collect(Collectors.toList());
        list.forEach(people -> {
            System.out.println(people);
        });
        List<Person> list2 = Stream.of(ming, wang, hong).collect(Collectors.toList());
        list2.forEach(people -> {
            System.out.println(people);
        });

        List<Person> list3 = personList.parallelStream().filter(person -> {
            System.out.println(Thread.currentThread().getName());
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (person.getAge() < 18) return false;
            return true;
        }).collect(Collectors.toList());
        list3.forEach(people -> {
            System.out.println(people);
        });
    }

    @Test
    public void t2() {
        Person person = personList.stream().min(Comparator.comparing(Person::getAge)).get();
        System.out.println(person);


        List<Person> list = personList.stream().filter(person1 -> {
            if (person1.getAge() < 18) return false;
            return true;
        }).collect(Collectors.toList());
        list.forEach(person1 -> System.out.println(person1));
        
    }
}
