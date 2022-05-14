package com.ziyuan.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TweetVO {
    private UserVO user;
    private String tweetId;
    private String content;
    private String attachedImg;
    private String likeCount;
    private String likeOrNot;
}
