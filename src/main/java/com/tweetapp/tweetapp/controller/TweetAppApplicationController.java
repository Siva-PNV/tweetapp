package com.tweetapp.tweetapp.controller;

import com.tweetapp.tweetapp.model.Users;
import com.tweetapp.tweetapp.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1.0/tweets")
public class TweetAppApplicationController {

    @Autowired
    private UsersService usersService;

    @PostMapping("/register")
    public ResponseEntity<?> registerNewUser(@RequestBody Users userModel) {
        if(!usersService.checkEmailAndLoginId(userModel)){
            return new ResponseEntity<>("Email Id and Login Id must be same.", HttpStatus.BAD_REQUEST);
        }
        if(!usersService.checkExistOrNot(userModel)){

            return new ResponseEntity<>(usersService.storeUserDetails(userModel), HttpStatus.CREATED);
        }
        return new ResponseEntity<>("User name already exist, please login",
                HttpStatus.CONFLICT);
    }

    @GetMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam String userName, @RequestParam String password, HttpServletRequest request) {
        if (usersService.checkUser(userName, password)) {
            request.getSession().setAttribute("userName",userName);
            return new ResponseEntity<>(usersService.getUser(userName, password), HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid credentials", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/{username}/forgot")
    public ResponseEntity<String> forgotPassword(@PathVariable String username, @RequestBody String newPassword){

        if(usersService.forgotPassword(username,newPassword)){
            return new ResponseEntity<>("password changed",HttpStatus.OK);
        }
        return new ResponseEntity<>("user name not found",HttpStatus.NOT_FOUND);
    }

    @GetMapping("/users/all")
    public ResponseEntity<?> getAllUsers(){
        return new ResponseEntity<>(usersService.getAllUsers(), HttpStatus.OK);

    }

    @GetMapping("/user/search/{userName}")
    public ResponseEntity<?> getByUserName(@PathVariable String userName){
        if(usersService.getByUserName(userName)!=null){
            return new ResponseEntity<>(usersService.getDetailsOfUser(userName), HttpStatus.OK);
        }
        return new ResponseEntity<>("User name not found", HttpStatus.NOT_FOUND);
    }
}
