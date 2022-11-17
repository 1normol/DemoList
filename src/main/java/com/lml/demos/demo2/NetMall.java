package com.lml.demos.demo2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ：limaolin
 * @date ：Created in 2022/11/16 15:07
 * @description：商城实体
 * @modified By：
 */

@AllArgsConstructor
@Data
@NoArgsConstructor
public class NetMall {
    public String netMail;
    List<String> mailList;

    public NetMall(String netMail) {
        this.netMail = netMail;
    }
}