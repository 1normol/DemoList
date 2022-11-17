package com.lml.demos.demo3.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ：limaolin
 * @date ：Created in 2022/11/17 15:14
 * @description：
 * @modified By：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    private String name;
    private Integer age;
    private List<Hobby> hobbyList;
}
