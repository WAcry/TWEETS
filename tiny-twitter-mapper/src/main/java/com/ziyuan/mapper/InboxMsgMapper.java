package com.ziyuan.mapper;

import com.ziyuan.pojo.InboxMsg;
import com.ziyuan.pojo.InboxMsgExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InboxMsgMapper {
    long countByExample(InboxMsgExample example);

    int deleteByExample(InboxMsgExample example);

    int deleteByPrimaryKey(String id);

    int insert(InboxMsg record);

    int insertSelective(InboxMsg record);

    List<InboxMsg> selectByExample(InboxMsgExample example);

    InboxMsg selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") InboxMsg record, @Param("example") InboxMsgExample example);

    int updateByExample(@Param("record") InboxMsg record, @Param("example") InboxMsgExample example);

    int updateByPrimaryKeySelective(InboxMsg record);

    int updateByPrimaryKey(InboxMsg record);
}