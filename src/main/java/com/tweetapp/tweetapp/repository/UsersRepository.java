package com.tweetapp.tweetapp.repository;

import com.tweetapp.tweetapp.model.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends MongoRepository<Users,String> {
    Users findByLoginId(String loginId);
    Boolean existsByLoginId(String loginId);
}
