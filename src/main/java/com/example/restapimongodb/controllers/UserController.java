package com.example.restapimongodb.controllers;

import com.example.restapimongodb.CustomizedResponse;
import com.example.restapimongodb.models.User;
import com.example.restapimongodb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private MongoTemplate mongoTemplate;


    @GetMapping("/users")
    public ResponseEntity getusers() {

        var customizedResponse = new CustomizedResponse(" A list of users", service.getUsers());

        return new ResponseEntity(customizedResponse, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity getAUser(@PathVariable("id") String id) {


        CustomizedResponse customizedResponse = null;
        try {
            customizedResponse = new CustomizedResponse(" User with id " + id, Collections.singletonList(service.getAUser(id)));
        } catch (Exception e) {

            customizedResponse = new CustomizedResponse(e.getMessage(), null);

            return new ResponseEntity(customizedResponse, HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity(customizedResponse, HttpStatus.OK);

    }

    @PostMapping(value = "/users", consumes = {
            MediaType.APPLICATION_JSON_VALUE
    })
    public ResponseEntity register(@RequestBody User user) {
        try {
            String output = service.register(user);
            if (output.equals("success")) {
                return new ResponseEntity(user, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(output);
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping(value = "/login", consumes = {
            MediaType.APPLICATION_JSON_VALUE
    })
    public ResponseEntity login(@RequestBody User user) {
        try {
            if (service.verify(user)){
                return new ResponseEntity(HttpStatus.OK);
            }
            else {
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PutMapping(value = "/users", consumes = {
            MediaType.APPLICATION_JSON_VALUE
    })
    public ResponseEntity updateAUser(@RequestBody User user) {
        try {
            if (service.updateAUser(user)) {
                return new ResponseEntity(user, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity deleteAUser(@PathVariable("id") String id) {

        if (service.deleteAUser(id)){
            return new ResponseEntity(HttpStatus.OK);
        }
        else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }


    }
}