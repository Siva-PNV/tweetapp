package com.tweetapp.tweetapp.services;

import com.tweetapp.tweetapp.model.UserModel;
import com.tweetapp.tweetapp.model.Users;
import com.tweetapp.tweetapp.repository.UserModelRepository;
import com.tweetapp.tweetapp.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserModelRepository userModelRepository;

    public Users storeUserDetails(Users user){
        usersRepository.save(user);
        return user;
    }

    public boolean checkEmailAndLoginId(Users user){
        return user.getEmailId().equals(user.getLoginId());
    }

    public boolean checkExistOrNot(Users user){
        return usersRepository.existsByLoginId(user.getLoginId());
    }

    public boolean checkUser(String userName,String password){
        Users tempUser = usersRepository.findByLoginId(userName);
        return  (tempUser!=null && tempUser.getLoginId().equals(userName) && tempUser.getPassword().equals(password));
    }

    public UserModel getUser(String userName, String password){
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

    public List<UserModel> getAllUsers(){
        return userModelRepository.findAll();
    }

    public Users getByUserName(String userName){
        return usersRepository.findByLoginId(userName);
    }


    public UserModel getDetailsOfUser(String userName){
        return userModelRepository.findByLoginId(userName);
    }

}
