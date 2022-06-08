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
public class Tweet {
    private String tweetId;
    private String userId;
    private String content;
    private String attachedImg;
    private Integer deleted;
    private Date createdAt;
    private Date updatedAt;
}