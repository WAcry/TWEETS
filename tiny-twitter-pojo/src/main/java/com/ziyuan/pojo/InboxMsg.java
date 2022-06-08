package com.ziyuan.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InboxMsg {
    private String inboxMsgId;
    private String fromUserId;
    private String toUserId;
    private String tweetId;
}