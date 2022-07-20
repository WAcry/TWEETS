package com.ziyuan.controller;

import com.ziyuan.service.SearchService;
import com.ziyuan.utils.JSONResult;
import com.ziyuan.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/search")
    public JSONResult search(
            String keywords,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize) {
        if (StringUtils.isBlank(keywords)) return JSONResult.errorMsg(null);

        if (pageSize == null) pageSize = 5;
        if (page == null) page = 1;
        page--;

        PagedGridResult grid = searchService.search(keywords, page, pageSize);
        return JSONResult.ok(grid);
    }
}
