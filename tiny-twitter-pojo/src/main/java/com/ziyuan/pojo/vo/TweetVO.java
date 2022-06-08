package com.ziyuan.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TweetVO {
    private UserVO user;
    private String tweetId;
    private String content;
    private String attachedImg;
    private Long likeCount;
    private Integer likeStatus;
    private Date createdAt;
}
