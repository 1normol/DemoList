package com.lml.demos.demo2.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ：limaolin
 * @date ：Created in 2022/11/16 15:26
 * @description：人实体
 * @modified By：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    private String name;
    private Integer age;
    private String state;
}
