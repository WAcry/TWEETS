package com.ziyuan.pojo;

import java.util.ArrayList;
import java.util.List;

public class LikeExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public LikeExample() {
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

        public Criteria andLikeIdIsNull() {
            addCriterion("like_id is null");
            return (Criteria) this;
        }

        public Criteria andLikeIdIsNotNull() {
            addCriterion("like_id is not null");
            return (Criteria) this;
        }

        public Criteria andLikeIdEqualTo(String value) {
            addCriterion("like_id =", value, "likeId");
            return (Criteria) this;
        }

        public Criteria andLikeIdNotEqualTo(String value) {
            addCriterion("like_id <>", value, "likeId");
            return (Criteria) this;
        }

        public Criteria andLikeIdGreaterThan(String value) {
            addCriterion("like_id >", value, "likeId");
            return (Criteria) this;
        }

        public Criteria andLikeIdGreaterThanOrEqualTo(String value) {
            addCriterion("like_id >=", value, "likeId");
            return (Criteria) this;
        }

        public Criteria andLikeIdLessThan(String value) {
            addCriterion("like_id <", value, "likeId");
            return (Criteria) this;
        }

        public Criteria andLikeIdLessThanOrEqualTo(String value) {
            addCriterion("like_id <=", value, "likeId");
            return (Criteria) this;
        }

        public Criteria andLikeIdLike(String value) {
            addCriterion("like_id like", value, "likeId");
            return (Criteria) this;
        }

        public Criteria andLikeIdNotLike(String value) {
            addCriterion("like_id not like", value, "likeId");
            return (Criteria) this;
        }

        public Criteria andLikeIdIn(List<String> values) {
            addCriterion("like_id in", values, "likeId");
            return (Criteria) this;
        }

        public Criteria andLikeIdNotIn(List<String> values) {
            addCriterion("like_id not in", values, "likeId");
            return (Criteria) this;
        }

        public Criteria andLikeIdBetween(String value1, String value2) {
            addCriterion("like_id between", value1, value2, "likeId");
            return (Criteria) this;
        }

        public Criteria andLikeIdNotBetween(String value1, String value2) {
            addCriterion("like_id not between", value1, value2, "likeId");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(String value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(String value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(String value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(String value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(String value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLike(String value) {
            addCriterion("user_id like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotLike(String value) {
            addCriterion("user_id not like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<String> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<String> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(String value1, String value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(String value1, String value2) {
            addCriterion("user_id not between", value1, value2, "userId");
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