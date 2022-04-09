package com.ziyuan.pojo.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBO {
    private String token;
    private String userId;
    private String username;
    private String password;
    private String profileImg;
    private String ext;
}
