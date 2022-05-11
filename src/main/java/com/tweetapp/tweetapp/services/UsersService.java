package com.tweetapp.tweetapp.services;

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

    public boolean checkUser(String userName,String password){
        Users tempUser = usersRepository.findByLoginId(userName);
        return  (tempUser!=null && tempUser.getLoginId().equals(userName) && tempUser.getPassword().equals(password));
    }

    public Users getUser(String userName,String password){
        return usersRepository.findUserByUsernameAndPassword(userName,password);
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
        return usersRepository.findAll();
    }

    public Users getByUserName(String userName){
        return usersRepository.findByLoginId(userName);
    }

}
