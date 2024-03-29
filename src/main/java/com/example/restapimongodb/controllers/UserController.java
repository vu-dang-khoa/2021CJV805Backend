package com.example.restapimongodb.controllers;

import com.example.restapimongodb.CustomizedResponse;
import com.example.restapimongodb.models.UserModel;
import com.example.restapimongodb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@CrossOrigin
@Controller
public class UserController

{

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity getUsers()
    {

        var response = new CustomizedResponse( " A list of all users ", userService.getUsers());

        return new ResponseEntity( response, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity getAUser(@PathVariable("id") String id)
    {

        var response = new CustomizedResponse( " User with id  : " + id, Collections.singletonList(userService.getAUser(id)));

        return new ResponseEntity( response, HttpStatus.OK);

    }

    @PostMapping(value = "/users", consumes = {
            MediaType.APPLICATION_JSON_VALUE
    })
    public ResponseEntity createUsers(@RequestBody UserModel user)
    {
        try{
            var response = new CustomizedResponse( " User created successfully", Collections.singletonList(userService.addUser(user)));

            return new ResponseEntity( response, HttpStatus.OK);
        }
        catch(Exception e){
            List<String> empty = new ArrayList<String>();
            String temp = e.getMessage();
            var response = new CustomizedResponse( temp,empty);
            return new ResponseEntity(response,HttpStatus.BAD_REQUEST);



        }



    }
}