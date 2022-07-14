package com.ziyuan.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomInboxMsgMapper {
    public List<String> select5nextTweetIds(@Param("userId") String userId,
                                            @Param("lastTweetId") String lastTweetId);
}