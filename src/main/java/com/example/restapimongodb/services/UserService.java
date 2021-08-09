package com.example.restapimongodb.services;

import com.example.restapimongodb.models.UserModel;
import com.example.restapimongodb.models.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService implements UserDetailsService

{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserModel addUser(UserModel user) throws Exception
    {

        String validCheckOutput = isValid(user);
        String uniqueCheckOutput = userNameAndEmailDidntExist(user);

        if (validCheckOutput.equals("valid") && uniqueCheckOutput.equals("valid")){
            String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());

            user.setPassword(encodedPassword);

            UserModel insertedUser = userRepository.insert(user);

            return insertedUser;
        }
        else {
            String temp = validCheckOutput + " ";
            temp = temp + uniqueCheckOutput;
            throw new Exception(temp);
        }


    }

    public List<UserModel> getUsers()
    {
        return userRepository.findAll();
    }

    public Optional<UserModel> getAUser(String id)
    {
        return userRepository.findById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException


    {


        UserModel foundUser = userRepository.findByUsername(username);

        String userN = foundUser.getUsername();
        String password = foundUser.getPassword();

        return new User(userN, password, new ArrayList<>());


    }

    public String isValid(UserModel user){
        String output = "";

        //email
        if (user.getEmail()!=null && !user.getEmail().equals("")){
            if (!Pattern.matches("\\w+@\\w+.\\w+",user.getEmail())){
                output = output + " output should be informat \\w+@\\w+.\\w+";
            }

        }

        //username
        if (!(user.getUsername()!=null && !user.getUsername().equals(""))){
            output = output + " username can't be empty ";
        }

        //password
        if (user.getPassword()!=null && !user.getPassword().equals("")){
            if (user.getPassword().length()<6){
                output = output + " length of password should at least be 6 ";
            }
        }

        //lastname
        if (!(user.getLastName()!=null && !user.getLastName().equals(""))){
            output = output + " lastname can't be empty ";
        }

        //firstname
        if (!(user.getFirstName()!=null && !user.getFirstName().equals(""))){
            output = output + " firstname can't be empty ";
        }



        if (output.equals("")){
            return ("valid");
        }
        else{
            return output;
        }
    }

    private String userNameAndEmailDidntExist(UserModel user){
        String output = "";
        List<User> usernameList = mongoTemplate.find(new Query().addCriteria(Criteria.where("username" ).is(user.getUsername())), User.class);
        if (usernameList.size()!=0){
            output = output + "username already exist";
        }

        List<User> emailList = mongoTemplate.find(new Query().addCriteria(Criteria.where("email" ).is(user.getEmail())), User.class);
        if (emailList.size()!=0){
            output = output + "Useremail already exist";
        }
        if (output.equals("")){
            return "valid";
        }
        else {
            return output;
        }

    }
}