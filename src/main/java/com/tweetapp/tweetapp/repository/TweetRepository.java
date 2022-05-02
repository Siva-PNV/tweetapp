package com.tweetapp.tweetapp.repository;

import com.tweetapp.tweetapp.model.Tweets;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetRepository extends MongoRepository<Tweets, String>{

    List<Tweets> findByUsername(String username);
}
