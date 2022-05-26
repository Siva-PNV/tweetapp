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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1.0/tweets")
public class TweetController {

    public static final String USER_NAME_NOT_FOUND = "User name not found";
    @Autowired
    private TweetsService tweetService;

    @Autowired
    private UsersService usersService;

    @PostMapping("/{userName}/add")
    public ResponseEntity<?> postNewTweet(@PathVariable String userName, @RequestBody Tweets tweets, HttpServletRequest request) {
        Users user=usersService.getByUserName(userName);
        if(user==null){
            return new ResponseEntity<>(USER_NAME_NOT_FOUND,HttpStatus.BAD_REQUEST);
        }
//        if(request.getSession().getAttribute("userName")==null){
//            return new ResponseEntity<>("Please login to add the tweet",HttpStatus.UNAUTHORIZED);
//        }
//        if(!checkAttribute(userName,request)){
//            return new ResponseEntity<>("\"You don`t have access to add the tweet\"",HttpStatus.UNAUTHORIZED);
//        }
        tweetService.postNewTweet(userName, tweets);
        return new ResponseEntity<>("\"Tweet created\"",HttpStatus.CREATED);
    }

    @GetMapping( "/all")
    public ResponseEntity<?> getAllTweets() {
            return new ResponseEntity<>(tweetService.getAllTweets(), HttpStatus.OK);
    }

    @GetMapping( "/{userName}")
    public ResponseEntity<?> getUserTweets(@PathVariable String userName ) throws InvalidUsernameException {
        Users user=usersService.getByUserName(userName);
        if(user==null){
            return new ResponseEntity<>(USER_NAME_NOT_FOUND,
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(tweetService.getUserTweets(userName), HttpStatus.OK);

    }

    @PutMapping( "/{userName}/update/{tweetId}")
    public ResponseEntity<?> updateTweet(@PathVariable String userName, @PathVariable String tweetId,@RequestBody TweetUpdate tweetUpdate, HttpServletRequest request) {
        Users user=usersService.getByUserName(userName);
//        if(request.getSession().getAttribute("userName")==null){
//            return new ResponseEntity<>("Please login to update the tweet",HttpStatus.UNAUTHORIZED);
//        }
        if(user==null){
            return new ResponseEntity<>(USER_NAME_NOT_FOUND,
                    HttpStatus.NOT_FOUND);
        }
//        if(!checkAttribute(userName,request)){
//            return new ResponseEntity<>("You don`t have access to edit the tweet",HttpStatus.UNAUTHORIZED);
//        }

        try {

            return new ResponseEntity<>(tweetService.updateTweet(userName,tweetId, tweetUpdate.getTweetText()), HttpStatus.OK);
        } catch (TweetDoesNotExistException e) {
            return new ResponseEntity<>("Given tweetId cannot be found",
                    HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping( "/{userName}/delete/{tweetId}")
    public ResponseEntity<?> deleteTweet( @PathVariable String userName,
                                          @PathVariable String tweetId,HttpServletRequest request) {
        Users user=usersService.getByUserName(userName);
//        if(request.getSession().getAttribute("userName")==null){
//            return new ResponseEntity<>("Please login to delete the tweet",HttpStatus.UNAUTHORIZED);
//        }
//        if(user==null){
//            return new ResponseEntity<>("User name not found",
//                    HttpStatus.NOT_FOUND);
//        }
//        if(!checkAttribute(userName,request)){
//            return new ResponseEntity<>("You don`t have access to delete the tweet",HttpStatus.UNAUTHORIZED);
//        }

        try {
            tweetService.deleteTweet(userName,tweetId);
            return new ResponseEntity<>("\"Tweet deleted successfully\"", HttpStatus.OK);
        } catch (TweetDoesNotExistException e) {
            return new ResponseEntity<>("\"Given tweetId cannot be found\"",
                    HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping( "/{userName}/like/{tweetId}")
    public ResponseEntity<?> likeATweet(@PathVariable String userName, @PathVariable String tweetId,HttpServletRequest request) throws TweetDoesNotExistException {
//        if(request.getSession().getAttribute("userName")==null){
//            return new ResponseEntity<>("Please login to like for the tweet",HttpStatus.UNAUTHORIZED);
//        }
        Users user=usersService.getByUserName(userName);
        if(user==null){
            return new ResponseEntity<>("User name not found",
                    HttpStatus.NOT_FOUND);
        }
        try {
            if(!tweetService.checkLikedOrNot(userName, tweetId)){
                tweetService.likeTweet(userName, tweetId);
                return new ResponseEntity<>("\" liked tweet \"", HttpStatus.OK);
            }else{
                tweetService.disLikeTweet(userName, tweetId);
                return new ResponseEntity<>("\"Disliked tweet\"", HttpStatus.OK);
            }
        } catch (TweetDoesNotExistException e) {
            return new ResponseEntity<>("\"Given tweetId cannot be found\"",
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{userName}/reply/{tweetId}")
    public ResponseEntity<?> replyToTweet(@PathVariable String userName,
                                          @PathVariable String tweetId, @RequestBody Reply tweetReply, HttpServletRequest request) {
//        if(request.getSession().getAttribute("userName")==null){
//            return new ResponseEntity<>("Please login to reply for the tweet",HttpStatus.UNAUTHORIZED);
//        }
        Users user=usersService.getByUserName(userName);
        if(user==null){
            return new ResponseEntity<>("User name not found",
                    HttpStatus.NOT_FOUND);
        }
        try {
            tweetService.replyTweet(userName, tweetId, tweetReply.getComment());
            return new ResponseEntity<>("\"Replied\"", HttpStatus.OK);
        } catch (TweetDoesNotExistException e) {
            return new ResponseEntity<>("\"Given tweetId cannot be found\"",
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getLike/{tweetId}")
    public ResponseEntity<?> getLikes(@PathVariable String tweetId){
        List<Tweets> tweet=tweetService.findByTweetId(tweetId);
        if(tweet==null){
            return new ResponseEntity<>("\"Tweet id not found\"",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(tweetService.findByTweetId(tweetId),HttpStatus.OK);
    }

    public boolean checkAttribute(String userName, HttpServletRequest request){
         return request.getSession().getAttribute("\"userName\"").equals(userName);
    }

}
