SELECT tweet_id     as tweetId,
       user_id      as userId,
       content      as content,
       attached_img as attachedImg,
       status       as status,
       created_at   as createdAt,
       updated_at   as updatedAt
FROM tweet
WHERE updated_at >= :sql_last_value