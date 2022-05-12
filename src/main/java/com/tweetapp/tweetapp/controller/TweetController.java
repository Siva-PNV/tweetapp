package com.tweetapp.tweetapp.controller;

import com.tweetapp.tweetapp.exception.InvalidUsernameException;
import com.tweetapp.tweetapp.exception.TweetDoesNotExistException;
import com.tweetapp.tweetapp.model.Reply;
import com.tweetapp.tweetapp.model.TweetUpdate;
import com.tweetapp.tweetapp.model.Tweets;
import com.tweetapp.tweetapp.model.Users;
import com.tweetapp.tweetapp.services.TweetsService;
import com.tweetapp.tweetapp.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1.0/tweets")
public class TweetController {

    @Autowired
    private TweetsService tweetService;

    @Autowired
    private UsersService usersService;

    @PostMapping("/{userName}/add")
    public ResponseEntity<?> postNewTweet(@PathVariable String userName, @RequestBody Tweets newTweet, HttpServletRequest request) {
        if(!checkAttribute(userName,request)){
            return new ResponseEntity<>("Please login to add the tweet",HttpStatus.UNAUTHORIZED);
        }
        Users user=usersService.getByUserName(userName);
        if(user!=null){
            tweetService.postNewTweet(userName, newTweet);
            return new ResponseEntity<>("Tweet created",HttpStatus.CREATED);
        }
        return new ResponseEntity<>("User name not found",HttpStatus.BAD_REQUEST);

    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllTweets() {

        try {
            return new ResponseEntity<>(tweetService.getAllTweets(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Application has faced an issue",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{userName}")
    public ResponseEntity<?> getUserTweets(@PathVariable String userName ) {
        Users user=usersService.getByUserName(userName);
        if(user==null){
            return new ResponseEntity<>("User name not found",
                    HttpStatus.NOT_FOUND);
        }
        try {
            return new ResponseEntity<>(tweetService.getUserTweets(userName), HttpStatus.OK);
        } catch (InvalidUsernameException e) {
            return new ResponseEntity<>("Invalid User param received",
                    HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            return new ResponseEntity<>("Application has faced an issue",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/{userName}/update")
    public ResponseEntity<?> updateTweet(@PathVariable String userName, @RequestBody TweetUpdate tweetUpdate, HttpServletRequest request) {
        if(!checkAttribute(userName,request)){
            return new ResponseEntity<>("Please login to update the tweet",HttpStatus.UNAUTHORIZED);
        }
        Users user=usersService.getByUserName(userName);
        if(user==null){
            return new ResponseEntity<>("User name not found",
                    HttpStatus.NOT_FOUND);
        }
        try {
            return new ResponseEntity<>(tweetService.updateTweet(userName,tweetUpdate.getTweetId(), tweetUpdate.getTweetText()), HttpStatus.OK);
        } catch (TweetDoesNotExistException e) {
            return new ResponseEntity<>("Given tweetId cannot be found",
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Application has faced an issue",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{userName}/delete/{tweetId}")
    public ResponseEntity<?> deleteTweet( @PathVariable String userName,
                                          @PathVariable String tweetId,HttpServletRequest request) {
        if(!checkAttribute(userName,request)){
            return new ResponseEntity<>("Please login to delete the tweet",HttpStatus.UNAUTHORIZED);
        }
        Users user=usersService.getByUserName(userName);
        if(user==null){
            return new ResponseEntity<>("User name not found",
                    HttpStatus.NOT_FOUND);
        }
        try {
            return new ResponseEntity<>(tweetService.deleteTweet(userName,tweetId), HttpStatus.OK);
        } catch (TweetDoesNotExistException e) {
            return new ResponseEntity<>("Given tweetId cannot be found",
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Application has faced an issue",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/{userName}/like/{tweetId}")
    public ResponseEntity<?> likeATweet(@PathVariable String userName, @PathVariable String tweetId,HttpServletRequest request){
        if(request.getSession().getAttribute("userName")==null){
            return new ResponseEntity<>("Please login to like for the tweet",HttpStatus.UNAUTHORIZED);
        }
        Users user=usersService.getByUserName(userName);
        if(user==null){
            return new ResponseEntity<>("User name not found",
                    HttpStatus.NOT_FOUND);
        }
        try {
            return new ResponseEntity<>(tweetService.likeTweet(userName, tweetId), HttpStatus.OK);
        } catch (TweetDoesNotExistException e) {
            return new ResponseEntity<>("Given tweetId cannot be found",
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Application has faced an issue",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/{userName}/reply/{tweetId}")
    public ResponseEntity<?> replyToTweet(@PathVariable String userName,
                                          @PathVariable String tweetId, @RequestBody Reply tweetReply, HttpServletRequest request) {
        if(request.getSession().getAttribute("userName")==null){
            return new ResponseEntity<>("Please login to reply for the tweet",HttpStatus.UNAUTHORIZED);
        }
        Users user=usersService.getByUserName(userName);
        if(user==null){
            return new ResponseEntity<>("User name not found",
                    HttpStatus.NOT_FOUND);
        }
        try {
            return new ResponseEntity<>(tweetService.replyTweet(userName, tweetId, tweetReply.getComment()), HttpStatus.OK);
        } catch (TweetDoesNotExistException e) {
            return new ResponseEntity<>("Given tweetId cannot be found",
                    HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Application has faced an issue",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean checkAttribute(String userName, HttpServletRequest request){
        return request.getSession().getAttribute("userName").equals(userName);
    }

}
