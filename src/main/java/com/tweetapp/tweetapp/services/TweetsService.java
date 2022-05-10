package com.tweetapp.tweetapp.services;

import com.tweetapp.tweetapp.exception.InvalidUsernameException;
import com.tweetapp.tweetapp.exception.TweetDoesNotExistException;
import com.tweetapp.tweetapp.model.Comment;
import com.tweetapp.tweetapp.model.TweetResponse;
import com.tweetapp.tweetapp.model.Tweets;
import com.tweetapp.tweetapp.repository.TweetRepository;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TweetsService {

    @Autowired
    private TweetRepository tweetRepository;

    //Methos to return all tweets
    public List<Tweets> getAllTweets() {
        return tweetRepository.findAll();
    }

    //Method to return all of a user's tweets
    public List<TweetResponse> getUserTweets(String username, String loggedInUser) throws InvalidUsernameException {
        // use username as login id
        if(!StringUtils.isBlank(username)) {
            List<Tweets> tweets = tweetRepository.findByUsername(username);
            //List<TweetResponse> tweetResponse= new ArrayList<>();
            List<TweetResponse> tweetResponse = tweets.stream().map(tweet ->{
                        Integer likesCount = tweet.getLikes().size();
                        Boolean likeStatus = tweet.getLikes().contains(loggedInUser);
                        Integer commentsCount = tweet.getComments().size();
                        return new TweetResponse(tweet.getTweetId(), username, tweet.getTweetText(),
                                tweet.getFirstName(), tweet.getLastName(), tweet.getTweetDate(),
                                likesCount, commentsCount, likeStatus, tweet.getComments());
                    })
                    .collect(Collectors.toList());
            //	.stream().filter(tweet -> tweet.getUsername().contentEquals(username))
            //	.collect(Collectors.toList());
            return tweetResponse;
        } else {
            throw new InvalidUsernameException("Username/loginId provided is invalid");
        }

    }

    //Method to post a new tweet
    public Tweets postNewTweet(String username, Tweets newTweet) {

        newTweet.setTweetId(UUID.randomUUID().toString());
        //logProducer.logNewPostEvents(newTweet);
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String formattedDate = myDateObj.format(myFormatObj);
        newTweet.setTweetDate(formattedDate);
        return tweetRepository.insert(newTweet);
    }

    //method to get tweet data by id
    public TweetResponse getTweet(String tweetId,String username) throws TweetDoesNotExistException {
        Optional<Tweets> tweetFounded =  tweetRepository.findById(tweetId);
        if(tweetFounded.isPresent()) {
            Tweets tweet = tweetFounded.get();
            Integer likesCount = tweet.getLikes().size();
            Boolean likeStatus = tweet.getLikes().contains(username);
            Integer commentsCount = tweet.getComments().size();
            return new TweetResponse(tweet.getTweetId(), tweet.getUsername(), tweet.getTweetText(),
                    tweet.getFirstName(), tweet.getLastName(), tweet.getTweetDate(),
                    likesCount, commentsCount, likeStatus, tweet.getComments());
        }else {
            throw new TweetDoesNotExistException("This tweet does not exist anymore.");
        }

    }

    //Method to update an existing tweet
    public Tweets updateTweet(String userId, String tweetId, String updatedTweetText) throws TweetDoesNotExistException {

        Optional<Tweets> originalTweetOptional = Optional.ofNullable(tweetRepository.findUserByUsernameAndTweetId(userId, tweetId));
        if(originalTweetOptional.isPresent()) {
            Tweets tweet = originalTweetOptional.get();
            tweet.setTweetText(updatedTweetText);

            return tweetRepository.save(tweet);
        } else {
            throw new TweetDoesNotExistException("This tweet does not exist anymore.");
        }

    }

    //Method to delete a tweet
    public boolean deleteTweet(String userName,String tweetId) throws TweetDoesNotExistException {
        if(tweetRepository.findByUsername(userName)!=null && tweetRepository.existsById(tweetId) && !StringUtils.isBlank(tweetId)) {
            tweetRepository.deleteById(tweetId);
            return true;
        }else {

            throw new TweetDoesNotExistException("This tweet does not exist anymore.");
        }
    }

    //Method to like a tweet
    public Tweets likeTweet(String username, String tweetId) throws TweetDoesNotExistException{
        Optional<Tweets> tweetOptional = tweetRepository.findById(tweetId);
        if(tweetOptional.isPresent()) {
            Tweets tweet = tweetOptional.get();
            tweet.getLikes().add(username);
            return tweetRepository.save(tweet);
        } else {
            throw new TweetDoesNotExistException("This tweet does not exist anymore.");
        }
    }

    //Method to comment on a tweet
    public Tweets replyTweet(String username, String tweetId, String tweetReply) throws TweetDoesNotExistException {
        Optional<Tweets> tweetOptional = tweetRepository.findById(tweetId);
        if(tweetOptional.isPresent()) {
            Tweets tweet = tweetOptional.get();
            tweet.getComments().add(new Comment(username, tweetReply));
            return tweetRepository.save(tweet);
            //return tweetReply;
        } else {
            throw new TweetDoesNotExistException("This tweet does not exist anymore.");
        }
    }

}
