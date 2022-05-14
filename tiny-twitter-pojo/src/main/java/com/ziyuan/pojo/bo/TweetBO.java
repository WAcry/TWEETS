package com.ziyuan.pojo.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TweetBO {
    private String token;
    private String userId;
    private String tweetId;
    private String content;
    private String attachedImg;
}
