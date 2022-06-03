package com.ziyuan.mapper;

import com.ziyuan.pojo.Tweet;
import com.ziyuan.pojo.TweetExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetMapper {
    long countByExample(TweetExample example);

    int deleteByExample(TweetExample example);

    int deleteByPrimaryKey(String tweetId);

    int insert(Tweet record);

    int insertSelective(Tweet record);

    List<Tweet> selectByExample(TweetExample example);

    Tweet selectByPrimaryKey(String tweetId);

    int updateByExampleSelective(@Param("record") Tweet record, @Param("example") TweetExample example);

    int updateByExample(@Param("record") Tweet record, @Param("example") TweetExample example);

    int updateByPrimaryKeySelective(Tweet record);

    int updateByPrimaryKey(Tweet record);
}