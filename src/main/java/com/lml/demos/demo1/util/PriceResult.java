package com.lml.demos.demo1.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ：limaolin
 * @date ：Created in 2022/11/15 14:24
 * @description：接口返回
 * @modified By：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceResult {
    private String name;
    private Integer oldPrice;
    private Double disCount;
    private Double price;
}
