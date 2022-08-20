# tiny-twitter

This project is a tiny twitter based on Spring Boot. The front end uses React.js + Ant Design.

The project focuses on high performance, distributed, MVC architecture, and provides RESTful like APIs.

A highlight is that timeline service implements the classic Push Model+Pull Model.

It is being refactored to split into microservices  @WAcry/tweets-cloud.

## Tech Stack

Java, Spring Boot, Redis, Elastic Search, Logstash, Kafka, MySQL+Mycat, React.js, Nginx, Docker

## Features

A user can post tweets, upload images, follow others, and like others' tweets. 

A user can access timeline in chronological order, which contains tweets posted by those he followed.

A user can search for tweet contents, and fuzzy search is supported.

## Project Structure

#### Descriptions

tiny-twitter-api: (C) Controllers, offer RESTful-like APIs, check if the input is legal, and authenticate users' token.

tiny-twitter-service: (V) Services, responsible for handling most of the business logic code.

tiny-twitter-mapper: (M) Daos, helps to operate databases

tiny-twitter-pojo: Pojos used in the project

tiny-twitter-common: utils, enums and configs

#### Dependencies

tiny-twitter-api -> tiny-twitter-service -> tiny-twitter-mapper -> tiny-twitter-pojo -> tiny-twitter-common

## Demo Images

![image](https://user-images.githubusercontent.com/61440144/185563783-d56d7059-5f27-4dea-8b42-11eea660801b.png)

## Deployment

1. Use JDK 1.8
2. Set up MySQL 8, Elastic Search 6.4.3 & Logstash 6.4.3, Kafka, Redis 5.
3. Use scripts in `docs/software/logstash-6.4.3/sync` to set up Logstash to sync data between MySQL and Elastic Search.
4. Use SQLs in `docs/sql` to set up MySQL.
5. Change settings in `/tiny-twitter-api/src/main/resources`.
6. Install the Spring Boot Application using Maven, and start `tiny-twitter-api/src/main/java/com/ziyuan/Application.java`

## Details

Here's an image showing the architecture of the project. Most of the read/write requests goes to Redis first, and most of the items in Redis has a 7 day TTL and refreshes every time accessing. If the read requests doesn't hit in Redis, we load the data from MySQL and add into Redis. For the write requests, we write into Redis first, then send corresponding messages to Kafka. Consumers consume the message and write into MySQL, which is able to handle higher than usual QPS in short period. Mycat middleware helps to create Read/Write Cluster for MySQL, to improve ability to handle read requests. Logstash sync data between MySQL and Elasticsearch every seconds. Also, Nginx and LVS can be used for load balance.



![image](https://raw.githubusercontent.com/Quakiq/tinyimages/main/img202208190837244.png)

What's more, here's how pull model and push model is implemented. We define a star user as those who had 1000 followers or had post 1000 tweets. 

Thus, Tweet Service and Relation Service may write into Star User List. 

When one post a tweet, he will store this tweet in his outbox. If he is not a star, he will also send the tweet to inboxes of all his followers. When one follow someone who is not a star, he should copy that person's outbox into his inbox... Similar when one delete his posts or unfollow someone.

Thus Tweet Service and Relation Service may read Star User List, and write inboxes and outboxes.

The timeline service offers an aggregation timeline for a user. It combine user's inbox and the stars' outboxes that user followed.

![image](https://raw.githubusercontent.com/Quakiq/tinyimages/main/img202208190848324.png)



