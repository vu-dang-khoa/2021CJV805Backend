package com.example.restapimongodb.controllers;

import com.example.restapimongodb.CustomizedResponse;
import com.example.restapimongodb.models.Tvshow;
import com.example.restapimongodb.services.TvshowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@CrossOrigin
@RestController
public class TvshowController {

    @Autowired
    private TvshowService service;


    @GetMapping("/tvshows")
    public ResponseEntity gettvshows() {

        var customizedResponse = new CustomizedResponse(" A list of tvshows", service.getTvshows());

        return new ResponseEntity(customizedResponse, HttpStatus.OK);
    }

    @GetMapping("/tvshows/featured")
    public ResponseEntity getFeaturedTvshows() {
        var customizedResponse = new CustomizedResponse(" Featured Tvshows " + service.getFeaturedTvshows().size(), service.getFeaturedTvshows());

        return new ResponseEntity(customizedResponse, HttpStatus.OK);
    }


    @GetMapping("/tvshows/rating")
    public ResponseEntity gettvshowsByRating(@RequestParam(value = "rate") int r) {

        var customizedResponse = new CustomizedResponse(" A list of tvshows with the rating : ", service.getTvshowsWithRating(r));

        return new ResponseEntity(customizedResponse, HttpStatus.OK);
    }

    @GetMapping("/tvshows/searchTitle")
    public ResponseEntity getTvshowsWithTitleFitsRegex(@RequestParam(value = "contain") String r) {

        var customizedResponse = new CustomizedResponse(" A list of tvshows with title containing " + r + " :", service.getTvshowsWithTitleFitsRegex(r));

        return new ResponseEntity(customizedResponse, HttpStatus.OK);
    }

    @GetMapping("/tvshows/{id}")
    public ResponseEntity getATvshow(@PathVariable("id") String id) {


        CustomizedResponse customizedResponse = null;
        try {
            customizedResponse = new CustomizedResponse(" Tvshow with id " + id, Collections.singletonList(service.getATvshow(id)));
        } catch (Exception e) {

            customizedResponse = new CustomizedResponse(e.getMessage(), null);

            return new ResponseEntity(customizedResponse, HttpStatus.NOT_FOUND);

        }
//        System.out.println(id);
//        System.out.println(service.getATvshow(id));
        return new ResponseEntity(customizedResponse, HttpStatus.OK);

    }


    @DeleteMapping("/tvshows/{id}")
    public ResponseEntity deleteATvshow(@PathVariable("id") String id) {

        if (service.deleteATvshow(id)){
            return new ResponseEntity(HttpStatus.OK);
        }
        else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }


    }

    @PostMapping(value = "/tvshows", consumes = {
            MediaType.APPLICATION_JSON_VALUE
    })
    public ResponseEntity addTvshow(@RequestBody Tvshow tvshow) {
        try {
            if (service.insertIntoTvshows(tvshow)) {
                return new ResponseEntity(tvshow, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }

    }

    @PutMapping(value = "/tvshows", consumes = {
            MediaType.APPLICATION_JSON_VALUE
    })
    public ResponseEntity updateTvshow(@RequestBody Tvshow tvshow) {
        try {
            if (service.updateATvshow(tvshow)) {
                return new ResponseEntity(tvshow, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }

    }

}