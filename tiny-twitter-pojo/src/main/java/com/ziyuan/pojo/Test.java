package com.ziyuan.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Test {
    private String id;
    private String str;
    private Date datetime;
    private String a;
    private String b;
    private String c;
    private String json;
}