package com.ziyuan.enums;

public enum Limits {
    // REDIS LIMITS
    USERNAME_INFO_TTL(60 * 60 * 24 * 7),
    USERID_INFO_TTL(60 * 60 * 24 * 7),
    USER_TOKEN_TTL(60 * 60 * 24 * 7),
    USER_TOKEN_REFRESH_THRESHOLD(60 * 60 * 24 * 3),
    TWEET_INFO_TTL(60 * 60 * 24 * 7),
    TWEET_ID_LIST_MAX_COUNT(100),
    INBOX_ID_LIST_MAX_COUNT(100),
    FANS_TTL(60 * 60 * 24 * 7),
    FOLLOWING_TTL(60 * 60 * 24 * 7),
    LIKED_TTL(60 * 60 * 24 * 7),
    LIKED_COUNT_TTL(60 * 60 * 24 * 7),

    // Limits
    MAX_FOLLOWING_COUNT(500), // one user can follow up to 500 people
    STAR_THRESHOLD(500); // one user owns STAR_THRESHOLD followers once will become a star forever.

    public final Integer value;

    Limits(Integer value) {
        this.value = value;
    }
}
