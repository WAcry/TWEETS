package com.ziyuan.enums;

/**
 * @Desc: Yes: 1 No: 2
 */
public enum YesOrNo {
    yes(2),
    no(1);

    public final Integer value;

    YesOrNo(Integer value) {
        this.value = value;
    }
}
