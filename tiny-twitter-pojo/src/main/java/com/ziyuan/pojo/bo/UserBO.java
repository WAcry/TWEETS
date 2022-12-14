package com.ziyuan.pojo.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBO {
    private String token;
    private String userId;
    private String username;
    private String password;
    private String profileImg;
}
