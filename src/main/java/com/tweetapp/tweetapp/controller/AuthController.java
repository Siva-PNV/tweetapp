package com.tweetapp.tweetapp.controller;

import com.tweetapp.tweetapp.model.LoginCredentials;
import com.tweetapp.tweetapp.model.Users;
import com.tweetapp.tweetapp.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1.0/tweets/")
public class AuthController {

    @Autowired
    private UsersService usersService;

   // @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("register")
    public ResponseEntity<?> subscribeClient(@RequestBody Users userModel) {

        try {
            Users savedUser = usersService.storeUserDetails(userModel);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Application has faced an issue",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("login")
    public ResponseEntity<String> loginUser(@RequestBody LoginCredentials loginCredentials) {
            if(usersService.checkUser(loginCredentials)){
                return new ResponseEntity<>("success",HttpStatus.OK);
            }
        return new ResponseEntity<>("Invalid credentials",HttpStatus.BAD_REQUEST);
    }
}
