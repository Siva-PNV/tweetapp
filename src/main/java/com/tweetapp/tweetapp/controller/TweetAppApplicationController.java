package com.tweetapp.tweetapp.controller;

import com.tweetapp.tweetapp.model.LoginCredentials;
import com.tweetapp.tweetapp.model.Users;
import com.tweetapp.tweetapp.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1.0/tweets")
public class TweetAppApplicationController {

    @Autowired
    private UsersService usersService;

    // @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> registerNewUser(@RequestBody Users userModel) {
        if(!usersService.checkExistOrNot(userModel)){

            return new ResponseEntity<>(usersService.storeUserDetails(userModel), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(userModel,
                HttpStatus.OK);
    }

    @GetMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam String userName, @RequestParam String password) {
        LoginCredentials loginCredentials=new LoginCredentials();
        loginCredentials.setLoginId(userName);
        loginCredentials.setPassword(password);
        if(usersService.checkUser(loginCredentials)){
            return new ResponseEntity<>(usersService.getUser(loginCredentials),HttpStatus.OK);
        }
        return new ResponseEntity<>("wrong",HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/{username}/forgot")
    public ResponseEntity<String> forgotPassword(@PathVariable String username, @RequestBody String newPassword){
        if(usersService.forgotPassword(username,newPassword)){
            return new ResponseEntity<>("success",HttpStatus.OK);
        }
        return new ResponseEntity<>("user name not found",HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/users/all")
    public ResponseEntity<?> getAllUsers(){
        return new ResponseEntity<>(usersService.getAllUsers(), HttpStatus.OK);

    }

    @GetMapping("/user/search/{userName}")
    public ResponseEntity<?> getByUserName(@PathVariable String userName){
        if(usersService.getByUserName(userName)!=null){
            return new ResponseEntity<>(usersService.getByUserName(userName), HttpStatus.OK);
        }
        return new ResponseEntity<>("User name not found", HttpStatus.BAD_REQUEST);
    }
}
