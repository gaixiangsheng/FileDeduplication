package com.erlin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) {
        Pattern pattern = Pattern.compile(
                "^\\w+(\\.\\w+)?" +//邮件登录名
                        "\\@\\w+" + //主机名
                        "\\.(\\w+){2,4}"//域名
        );
        Matcher matcher = pattern.matcher("javatianxia@163.com");
        if (matcher.matches()) {
            System.out.println("合法");
        } else {
            System.out.println("不合法");
        }
    }
}
