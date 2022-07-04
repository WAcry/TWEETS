package com.ziyuan.enums;

public enum CookieKey {
    USER_COOKIE("tweets_user"),
    TOKEN_COOKIE("tweets_token");
    public final String value;

    CookieKey(String value) {
        this.value = value;
    }
}
