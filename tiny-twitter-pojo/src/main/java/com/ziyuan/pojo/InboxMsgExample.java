package com.ziyuan.pojo;

import java.util.ArrayList;
import java.util.List;

public class InboxMsgExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public InboxMsgExample() {
        oredCriteria = new ArrayList<>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andInboxMsgIdIsNull() {
            addCriterion("inbox_msg_id is null");
            return (Criteria) this;
        }

        public Criteria andInboxMsgIdIsNotNull() {
            addCriterion("inbox_msg_id is not null");
            return (Criteria) this;
        }

        public Criteria andInboxMsgIdEqualTo(String value) {
            addCriterion("inbox_msg_id =", value, "inboxMsgId");
            return (Criteria) this;
        }

        public Criteria andInboxMsgIdNotEqualTo(String value) {
            addCriterion("inbox_msg_id <>", value, "inboxMsgId");
            return (Criteria) this;
        }

        public Criteria andInboxMsgIdGreaterThan(String value) {
            addCriterion("inbox_msg_id >", value, "inboxMsgId");
            return (Criteria) this;
        }

        public Criteria andInboxMsgIdGreaterThanOrEqualTo(String value) {
            addCriterion("inbox_msg_id >=", value, "inboxMsgId");
            return (Criteria) this;
        }

        public Criteria andInboxMsgIdLessThan(String value) {
            addCriterion("inbox_msg_id <", value, "inboxMsgId");
            return (Criteria) this;
        }

        public Criteria andInboxMsgIdLessThanOrEqualTo(String value) {
            addCriterion("inbox_msg_id <=", value, "inboxMsgId");
            return (Criteria) this;
        }

        public Criteria andInboxMsgIdLike(String value) {
            addCriterion("inbox_msg_id like", value, "inboxMsgId");
            return (Criteria) this;
        }

        public Criteria andInboxMsgIdNotLike(String value) {
            addCriterion("inbox_msg_id not like", value, "inboxMsgId");
            return (Criteria) this;
        }

        public Criteria andInboxMsgIdIn(List<String> values) {
            addCriterion("inbox_msg_id in", values, "inboxMsgId");
            return (Criteria) this;
        }

        public Criteria andInboxMsgIdNotIn(List<String> values) {
            addCriterion("inbox_msg_id not in", values, "inboxMsgId");
            return (Criteria) this;
        }

        public Criteria andInboxMsgIdBetween(String value1, String value2) {
            addCriterion("inbox_msg_id between", value1, value2, "inboxMsgId");
            return (Criteria) this;
        }

        public Criteria andInboxMsgIdNotBetween(String value1, String value2) {
            addCriterion("inbox_msg_id not between", value1, value2, "inboxMsgId");
            return (Criteria) this;
        }

        public Criteria andFromUserIdIsNull() {
            addCriterion("from_user_id is null");
            return (Criteria) this;
        }

        public Criteria andFromUserIdIsNotNull() {
            addCriterion("from_user_id is not null");
            return (Criteria) this;
        }

        public Criteria andFromUserIdEqualTo(String value) {
            addCriterion("from_user_id =", value, "fromUserId");
            return (Criteria) this;
        }

        public Criteria andFromUserIdNotEqualTo(String value) {
            addCriterion("from_user_id <>", value, "fromUserId");
            return (Criteria) this;
        }

        public Criteria andFromUserIdGreaterThan(String value) {
            addCriterion("from_user_id >", value, "fromUserId");
            return (Criteria) this;
        }

        public Criteria andFromUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("from_user_id >=", value, "fromUserId");
            return (Criteria) this;
        }

        public Criteria andFromUserIdLessThan(String value) {
            addCriterion("from_user_id <", value, "fromUserId");
            return (Criteria) this;
        }

        public Criteria andFromUserIdLessThanOrEqualTo(String value) {
            addCriterion("from_user_id <=", value, "fromUserId");
            return (Criteria) this;
        }

        public Criteria andFromUserIdLike(String value) {
            addCriterion("from_user_id like", value, "fromUserId");
            return (Criteria) this;
        }

        public Criteria andFromUserIdNotLike(String value) {
            addCriterion("from_user_id not like", value, "fromUserId");
            return (Criteria) this;
        }

        public Criteria andFromUserIdIn(List<String> values) {
            addCriterion("from_user_id in", values, "fromUserId");
            return (Criteria) this;
        }

        public Criteria andFromUserIdNotIn(List<String> values) {
            addCriterion("from_user_id not in", values, "fromUserId");
            return (Criteria) this;
        }

        public Criteria andFromUserIdBetween(String value1, String value2) {
            addCriterion("from_user_id between", value1, value2, "fromUserId");
            return (Criteria) this;
        }

        public Criteria andFromUserIdNotBetween(String value1, String value2) {
            addCriterion("from_user_id not between", value1, value2, "fromUserId");
            return (Criteria) this;
        }

        public Criteria andToUserIdIsNull() {
            addCriterion("to_user_id is null");
            return (Criteria) this;
        }

        public Criteria andToUserIdIsNotNull() {
            addCriterion("to_user_id is not null");
            return (Criteria) this;
        }

        public Criteria andToUserIdEqualTo(String value) {
            addCriterion("to_user_id =", value, "toUserId");
            return (Criteria) this;
        }

        public Criteria andToUserIdNotEqualTo(String value) {
            addCriterion("to_user_id <>", value, "toUserId");
            return (Criteria) this;
        }

        public Criteria andToUserIdGreaterThan(String value) {
            addCriterion("to_user_id >", value, "toUserId");
            return (Criteria) this;
        }

        public Criteria andToUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("to_user_id >=", value, "toUserId");
            return (Criteria) this;
        }

        public Criteria andToUserIdLessThan(String value) {
            addCriterion("to_user_id <", value, "toUserId");
            return (Criteria) this;
        }

        public Criteria andToUserIdLessThanOrEqualTo(String value) {
            addCriterion("to_user_id <=", value, "toUserId");
            return (Criteria) this;
        }

        public Criteria andToUserIdLike(String value) {
            addCriterion("to_user_id like", value, "toUserId");
            return (Criteria) this;
        }

        public Criteria andToUserIdNotLike(String value) {
            addCriterion("to_user_id not like", value, "toUserId");
            return (Criteria) this;
        }

        public Criteria andToUserIdIn(List<String> values) {
            addCriterion("to_user_id in", values, "toUserId");
            return (Criteria) this;
        }

        public Criteria andToUserIdNotIn(List<String> values) {
            addCriterion("to_user_id not in", values, "toUserId");
            return (Criteria) this;
        }

        public Criteria andToUserIdBetween(String value1, String value2) {
            addCriterion("to_user_id between", value1, value2, "toUserId");
            return (Criteria) this;
        }

        public Criteria andToUserIdNotBetween(String value1, String value2) {
            addCriterion("to_user_id not between", value1, value2, "toUserId");
            return (Criteria) this;
        }

        public Criteria andTweetIdIsNull() {
            addCriterion("tweet_id is null");
            return (Criteria) this;
        }

        public Criteria andTweetIdIsNotNull() {
            addCriterion("tweet_id is not null");
            return (Criteria) this;
        }

        public Criteria andTweetIdEqualTo(String value) {
            addCriterion("tweet_id =", value, "tweetId");
            return (Criteria) this;
        }

        public Criteria andTweetIdNotEqualTo(String value) {
            addCriterion("tweet_id <>", value, "tweetId");
            return (Criteria) this;
        }

        public Criteria andTweetIdGreaterThan(String value) {
            addCriterion("tweet_id >", value, "tweetId");
            return (Criteria) this;
        }

        public Criteria andTweetIdGreaterThanOrEqualTo(String value) {
            addCriterion("tweet_id >=", value, "tweetId");
            return (Criteria) this;
        }

        public Criteria andTweetIdLessThan(String value) {
            addCriterion("tweet_id <", value, "tweetId");
            return (Criteria) this;
        }

        public Criteria andTweetIdLessThanOrEqualTo(String value) {
            addCriterion("tweet_id <=", value, "tweetId");
            return (Criteria) this;
        }

        public Criteria andTweetIdLike(String value) {
            addCriterion("tweet_id like", value, "tweetId");
            return (Criteria) this;
        }

        public Criteria andTweetIdNotLike(String value) {
            addCriterion("tweet_id not like", value, "tweetId");
            return (Criteria) this;
        }

        public Criteria andTweetIdIn(List<String> values) {
            addCriterion("tweet_id in", values, "tweetId");
            return (Criteria) this;
        }

        public Criteria andTweetIdNotIn(List<String> values) {
            addCriterion("tweet_id not in", values, "tweetId");
            return (Criteria) this;
        }

        public Criteria andTweetIdBetween(String value1, String value2) {
            addCriterion("tweet_id between", value1, value2, "tweetId");
            return (Criteria) this;
        }

        public Criteria andTweetIdNotBetween(String value1, String value2) {
            addCriterion("tweet_id not between", value1, value2, "tweetId");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}