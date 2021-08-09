package com.example.restapimongodb.controllers;

import com.example.restapimongodb.CustomizedResponse;
import com.example.restapimongodb.models.Movie;
import com.example.restapimongodb.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@CrossOrigin
@RestController
public class MovieController {

    @Autowired
    private MovieService service;


    @GetMapping("/movies")
    public ResponseEntity getmovies() {

        var customizedResponse = new CustomizedResponse(" A list of movies", service.getMovies());

        return new ResponseEntity(customizedResponse, HttpStatus.OK);
    }

    @GetMapping("/movies/featured")
    public ResponseEntity getFeaturedMovies() {
        var customizedResponse = new CustomizedResponse(" Featured Movies " + service.getFeaturedMovies().size(), service.getFeaturedMovies());

        return new ResponseEntity(customizedResponse, HttpStatus.OK);
    }


    @GetMapping("/movies/rating")
    public ResponseEntity getmoviesByRating(@RequestParam("rate") int r) {

        System.out.println(r);
        var customizedResponse = new CustomizedResponse(" A list of movies with the rating : ", service.getMoviesWithRating(r));

        return new ResponseEntity(customizedResponse, HttpStatus.OK);
    }

    @GetMapping("/movies/searchTitle")
    public ResponseEntity getMoviesWithTitleFitsRegex(@RequestParam(value = "contain") String r) {

        var customizedResponse = new CustomizedResponse(" A list of movies with title containing " + r + " :", service.getMoviesWithTitleFitsRegex(r));

        return new ResponseEntity(customizedResponse, HttpStatus.OK);
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity getAMovie(@PathVariable("id") String id) {


        CustomizedResponse customizedResponse = null;
        try {
            customizedResponse = new CustomizedResponse(" Movie with id " + id, Collections.singletonList(service.getAMovie(id)));
        } catch (Exception e) {

            customizedResponse = new CustomizedResponse(e.getMessage(), null);

            return new ResponseEntity(customizedResponse, HttpStatus.NOT_FOUND);

        }

        return new ResponseEntity(customizedResponse, HttpStatus.OK);

    }


    @DeleteMapping("/movies/{id}")
    public ResponseEntity deleteAMovie(@PathVariable("id") String id) {

        if (service.deleteAMovie(id)){
            return new ResponseEntity(HttpStatus.OK);
        }
        else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }


    }

    @PostMapping(value = "/movies", consumes = {
            MediaType.APPLICATION_JSON_VALUE
    })
    public ResponseEntity addMovie(@RequestBody Movie movie) {
        try {
            if (service.insertIntoMovies(movie)) {
                return new ResponseEntity(movie, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }

    }

    @PutMapping(value = "/movies", consumes = {
            MediaType.APPLICATION_JSON_VALUE
    })
    public ResponseEntity updateMovie(@RequestBody Movie movie) {
        try {
            if (service.updateAMovie(movie)) {
                return new ResponseEntity(movie, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }

    }

}