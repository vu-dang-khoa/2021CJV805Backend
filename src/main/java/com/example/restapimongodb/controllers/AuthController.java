package com.example.restapimongodb.controllers;


import com.example.restapimongodb.CustomizedResponse;
import com.example.restapimongodb.models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.restapimongodb.services.UserService;

import java.util.Collections;

@CrossOrigin
@RestController
public class AuthController

{
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userservice;

    @PostMapping(value = "/auth", consumes = {
            MediaType.APPLICATION_JSON_VALUE
    })
    public ResponseEntity login(@RequestBody UserModel user)
    {


        try{

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

            var response = new CustomizedResponse("you logged in",Collections.singletonList(userservice.getUserByUsernamePassword(user.getUsername(), user.getPassword())));

                UserModel userReturned = userservice.getUserByUsernamePassword(user.getUsername(), user.getPassword());
                return new ResponseEntity( response, HttpStatus.OK);



        }

        catch (BadCredentialsException ex)
        {


            var response = new CustomizedResponse( "You username/password were entered incorrectly..", null);

            return new ResponseEntity( response, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





}