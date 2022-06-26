package com.ziyuan.pojo.es.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "tweets", type = "doc", createIndex = false)
public class TweetES {

    @Id
    @Field(store = true, type = FieldType.Text, index = false)
    private String tweetId;

    @Field(store = true, type = FieldType.Text, index = false)
    private String userId;

    @Field(store = true, type = FieldType.Text, index = true)
    private String content;

    @Field(store = true, type = FieldType.Text, index = false)
    private String attachedImg;

    @Field(store = true, type = FieldType.Integer)
    private Integer deleted;

    @Field(store = true, type = FieldType.Date)
    private Date createdAt;

    @Field(store = true, type = FieldType.Date)
    private Date updatedAt;
}
