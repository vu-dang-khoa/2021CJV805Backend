package com.example.restapimongodb.services;


import com.example.restapimongodb.models.Movie;
import com.example.restapimongodb.models.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

//import javax.management.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService

{

    @Autowired
    private MovieRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Movie> getMovies()

    {

        // business logics
        return repository.findAll();
    }

    public List<Movie> getFeaturedMovies()

    {

        // business logics

        List<Movie> movies = mongoTemplate.find(new Query().addCriteria(Criteria.where("rating" ).is(4)), Movie.class);
        movies.addAll(mongoTemplate.find(new Query().addCriteria(Criteria.where("rating" ).is(5)), Movie.class));
        if (movies.size()>6){
            List<Movie> output =  movies.subList(0,6);
            return output;
        }
        else {
            return movies;
        }


    }

    public List<Movie> getMoviesWithRating(int r)

    {

        // business logics
        Query query = new Query();
        query.addCriteria(Criteria.where(" rating" ).is(r));
        List<Movie> movies = mongoTemplate.find(query, Movie.class);
        return movies;


    }

    public List<Movie> getMoviesWithTitleFitsRegex(String r)

    {
        if(r == null || r.isEmpty()) {
            return new ArrayList<Movie>();
        }
        else{

            List<Movie> temp = mongoTemplate.find(new Query(Criteria.where("title").regex("\\w*" + r + "\\w*","i")), Movie.class);
            temp.addAll(temp);
            return temp;
        }



    }

    public boolean insertIntoMovies(Movie movie)
    {


        if (isValid(movie)){
            repository.insert(movie);
            return true;
        }
        return false;


    }

    public Optional<Movie> getAMovie(String id) throws Exception
    {

        Optional<Movie> movie = repository.findById(id);

        // This is saying that if movie ref variable does not contain a value then

        if (!movie.isPresent())
        {
            throw new Exception (" Movie with " + id + " is not found ");
        }

        return movie;

    }

    public boolean deleteAMovie( String id)
    {

        Optional<Movie> movie = repository.findById(id);

        // This is saying that if movie ref variable does not contain a value then

        if (!movie.isPresent())
        {
            return false;
        }


        repository.deleteById(id);
        return true;
    }

    public boolean updateAMovie(Movie movie){
        Optional<Movie> searchResult = repository.findById(movie.get_id());

        if (!searchResult.isPresent())
        {
            return false;
        }
        else {
            if (isValid(movie)){
                repository.save(movie);
                return true;
            }
            else{
                return false;
            }

        }
    }

    private boolean isValid(Movie movie){
        if (movie.getTitle()!=null && movie.getDescription()!=null && movie.getCategory()!=null && movie.getRating()!=0 && movie.getRent()!=0 && movie.getBuy()!=0 && movie.getSmallPoster()!=null && movie.getLargePoster()!=null ){
            if (movie.getRent()>0 && movie.getBuy()>0 && movie.getRating()>=0 && movie.getRating()<6){
                return true;
            }
            else {
                return false;
            }

        }
        return false;
    }


}