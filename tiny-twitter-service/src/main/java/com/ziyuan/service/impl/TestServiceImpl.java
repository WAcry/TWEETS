package com.ziyuan.service.impl;

import com.ziyuan.mapper.TestMapper;
import com.ziyuan.pojo.Test;
import com.ziyuan.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private TestMapper testMapper;

    @Override
    public void test(Test test) {
        testMapper.insert(test);
    }

}
