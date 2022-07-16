package com.ziyuan.service;

import com.ziyuan.pojo.bo.TimelineBO;
import com.ziyuan.pojo.vo.TimelineVO;

public interface TimelineService {
    public TimelineVO homeflow5items(TimelineBO timelineBO);

    public TimelineVO userflow5items(TimelineBO timelineBO);
}
