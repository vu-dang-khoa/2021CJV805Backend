package com.example.restapimongodb.services;


import com.example.restapimongodb.models.Movie;
import com.example.restapimongodb.models.User;
import com.example.restapimongodb.models.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

//import javax.management.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService

{

    @Autowired
    private UserRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> getUsers()

    {

        // business logics
        return repository.findAll();
    }

    public Optional<User> getAUser(String id) throws Exception
    {

        Optional<User> user = repository.findById(id);

        // This is saying that if movie ref variable does not contain a value then

        if (!user.isPresent())
        {
            throw new Exception (" user with " + id + " is not found ");
        }

        return user;

    }

    public boolean verify (User user){
        List<User> searchResult = mongoTemplate.find(new Query().addCriteria(Criteria.where("username" ).is(user.getUsername())), User.class);

        if (searchResult.size()==0)
        {
            return false;
        }
        else{
            if (BCrypt.checkpw(user.getPassword(), searchResult.get(0).getPassword())){
                return true;
            }
            else {
                return false;
            }
        }

    }

    public String register(User user){

        String isValidOutput = isValid(user);
        if (isValidOutput.equals("valid")){
            if (user.get_id()==null){
                String output = userNameAndEmailDidntExist(user);
                if(output.equals("valid")){
                    user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt()));
                    repository.insert(user);
                    return "success";
                }
                else{
                    return output;
                }
            }
            else{
                return "why is there an id provided in this newly created user";
            }
        }
        else{
            return isValidOutput;
        }
    }

    public boolean deleteUser( String id)
    {

        Optional<User> user = repository.findById(id);

        // This is saying that if movie ref variable does not contain a value then

        if (!user.isPresent())
        {
            return false;
        }


        repository.deleteById(id);
        return true;
    }

    public boolean updateAUser(User user){
        Optional<User> searchResult = repository.findById(user.get_id());

        if (!searchResult.isPresent())
        {
            return false;
        }
        else {
            if (isValid(user).equals("valid")){

                User temp = searchResult.get();
                //username should not be changed
                if (!temp.getUsername().equals(user.getUsername())){
                    return false;
                }
                else {
                    user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt()));
                    repository.save(user);
                    return true;
                }
            }
            else{
                return false;
            }

        }
    }

    public boolean deleteAUser( String id)
    {

        Optional<User> user = repository.findById(id);


        if (!user.isPresent())
        {
            return false;
        }


        repository.deleteById(id);
        return true;
    }

    public String isValid(User user){
        String output = "";

        //email
        if (user.getEmail()!=null && !user.getEmail().equals("")){
            if (!Pattern.matches("\\w+@\\w+.\\w+",user.getEmail())){
                output = output + " output should be informat \\w+@\\w+.\\w+";
            }

        }

        //username
        if (!(user.getUsername()!=null && !user.getUsername().equals(""))){
            output = output + " username can't be empty";
        }

        //password
        if (user.getPassword()!=null && !user.getPassword().equals("")){
            if (user.getPassword().length()<6){
                output = output + " length of password should at least be 6";
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

    private String userNameAndEmailDidntExist(User user){
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