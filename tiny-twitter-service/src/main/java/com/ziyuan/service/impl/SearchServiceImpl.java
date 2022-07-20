package com.ziyuan.service.impl;

import com.ziyuan.enums.YesOrNo;
import com.ziyuan.mapper.TweetMapper;
import com.ziyuan.pojo.es.pojo.TweetES;
import com.ziyuan.pojo.vo.TweetVO;
import com.ziyuan.pojo.vo.UserVO;
import com.ziyuan.service.LikeService;
import com.ziyuan.service.RelationService;
import com.ziyuan.service.SearchService;
import com.ziyuan.service.UserService;
import com.ziyuan.utils.KafkaOperator;
import com.ziyuan.utils.PagedGridResult;
import com.ziyuan.utils.RedisOperator;
import com.ziyuan.utils.SnowFlakeGenerator;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Autowired
    private SnowFlakeGenerator snowflake;

    @Autowired
    private RedisOperator redis;

    @Autowired
    private UserService userService;

    @Autowired
    private TweetMapper tweetMapper;

    @Autowired
    private KafkaOperator kafka;

    @Autowired
    private RelationService relationService;

    @Autowired
    private LikeService likeService;

    // logstash sync data from MySQL to Elasticsearch, insert new tweet and remove deleted tweets
    // refer configuration in software\logstash\
    @Override
    public PagedGridResult search(String keywords, Integer page, Integer pageSize) {

        String preTag = "<font color='yellow'>"; // highlight keywords
        String postTag = "</font>";

        Pageable pageable = PageRequest.of(page, pageSize);

        SortBuilder sortBuilder = new ScoreSortBuilder();
        String itemNameFiled = "content";

        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery(itemNameFiled, keywords))
                .withHighlightFields(new HighlightBuilder.Field(itemNameFiled)
                        .preTags(preTag)
                        .postTags(postTag)) //  default is <em></em> tags, can be customized in frontend
                .withSort(sortBuilder)
                .withPageable(pageable)
                .build();

        AggregatedPage<TweetES> pagedItems = esTemplate.queryForPage(query, TweetES.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {

                List<TweetES> tweetHighlightList = new ArrayList<>();

                SearchHits hits = response.getHits();
                for (SearchHit h : hits) {
                    HighlightField highlightField = h.getHighlightFields().get(itemNameFiled);
                    String content = highlightField.getFragments()[0].toString();
                    String tweetId = (String) h.getSourceAsMap().get("tweetId");
                    String userId = (String) h.getSourceAsMap().get("userId");
                    String attachedImg = (String) h.getSourceAsMap().get("attachedImg");
                    Integer status = (Integer) h.getSourceAsMap().get("status");
                    Date createdAt = (Date) h.getSourceAsMap().get("createdAt");
                    Date updatedAt = (Date) h.getSourceAsMap().get("updatedAt");
                    TweetES tweet = new TweetES(tweetId, userId, content, attachedImg, status, createdAt, updatedAt);
                    tweetHighlightList.add(tweet);
                }

                return new AggregatedPageImpl<>((List<T>) tweetHighlightList, pageable, response.getHits().totalHits);
            }

            @Override
            public <T> T mapSearchHit(SearchHit searchHit, Class<T> aClass) {
                return null;
            }
        });

        PagedGridResult gridResult = new PagedGridResult();
        gridResult.setPage(page + 1);
        gridResult.setTotal(pagedItems.getTotalPages());
        gridResult.setRecords(pagedItems.getTotalElements());

        List<TweetES> tweetHighlightList = pagedItems.getContent();
        List<TweetVO> tweetVOList = new ArrayList<>();
        for (TweetES tweet : tweetHighlightList) tweetVOList.add(convertToTweetVO(tweet));
        gridResult.setRows(tweetVOList);
        return gridResult;
    }

    private TweetVO convertToTweetVO(TweetES tweet) {
        if (tweet == null || Objects.equals(tweet.getDeleted(), YesOrNo.YES.value)) return null;
        TweetVO tweetVO = new TweetVO();
        BeanUtils.copyProperties(tweet, tweetVO);
        UserVO userVO = userService.getUserVOById(tweet.getUserId());
        tweetVO.setUser(userVO);
        tweetVO.setContent(tweet.getContent()); // use highlighted content
        tweetVO.setLikeCount(likeService.getLikeCount(tweet.getTweetId()));
        tweetVO.setLikeStatus(likeService.isLiked(tweet.getUserId(), tweet.getTweetId()));
        return tweetVO;
    }
}