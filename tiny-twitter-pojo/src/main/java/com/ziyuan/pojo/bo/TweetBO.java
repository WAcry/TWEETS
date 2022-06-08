package com.ziyuan.pojo.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TweetBO {
    private String token;
    private String userId;
    private String tweetId;
    private String content;
    private String attachedImg;
}
