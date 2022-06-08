package com.ziyuan.pojo.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimelineBO {
    private String token;
    private String userId;
    private String lastTweetId; // last tweet we got, we want more tweets with lower id
}
