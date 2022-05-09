package com.tweetapp.tweetapp.services;

import com.tweetapp.tweetapp.model.LoginCredentials;
import com.tweetapp.tweetapp.model.Users;
import com.tweetapp.tweetapp.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    public Users storeUserDetails(Users user){
        usersRepository.save(user);
        return user;
    }

    public boolean checkExistOrNot(Users user){
        return usersRepository.existsByLoginId(user.getLoginId());
    }

    public boolean checkUser(LoginCredentials loginCredentials){
        String tempLoginId = loginCredentials.getLoginId();
        String tempPassword = loginCredentials.getPassword();
        Users tempUser = usersRepository.findByLoginId(tempLoginId);
        return  (tempUser!=null && tempUser.getLoginId().equals(tempLoginId) && tempUser.getPassword().equals(tempPassword));
    }

    public Users getUser(LoginCredentials loginCredentials){
        return usersRepository.findUserByUsernameAndPassword(loginCredentials.getLoginId(),loginCredentials.getPassword());
    }

    public boolean forgotPassword(String userName, String newPassword){
       Users user= usersRepository.findByLoginId(userName);
       if(user !=null){
           user.setPassword(newPassword);
           usersRepository.save(user);
           return true;
       }
       return false;
    }

    public List<Users> getAllUsers(){
        List<Users> allUsers=usersRepository.findAll();
        return allUsers;
    }

    public Users getByUserName(String userName){
       Users user=usersRepository.findByLoginId(userName);
        return user;
    }

}
