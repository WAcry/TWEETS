package com.ziyuan.pojo.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelationBO {
    private String token;
    private String fromUserId;
    private String toUserId;
}
