package com.ziyuan.service;

import com.ziyuan.utils.PagedGridResult;

public interface SearchService {

    public PagedGridResult search(String keywords, Integer page, Integer pageSize);
}
