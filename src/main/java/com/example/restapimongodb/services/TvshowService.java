package com.example.restapimongodb.services;


import com.example.restapimongodb.models.Tvshow;
import com.example.restapimongodb.models.TvshowRepository;
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
public class TvshowService

{

    @Autowired
    private TvshowRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Tvshow> getTvshows()

    {

        // business logics
        return repository.findAll();
    }

    public List<Tvshow> getFeaturedTvshows()

    {

        // business logics

        List<Tvshow> tvshows = mongoTemplate.find(new Query().addCriteria(Criteria.where("rating" ).is(4)), Tvshow.class);
        tvshows.addAll(mongoTemplate.find(new Query().addCriteria(Criteria.where("rating" ).is(5)), Tvshow.class));
        if (tvshows.size()>6){
            List<Tvshow> output =  tvshows.subList(0,6);
            return output;
        }
        else {
            return tvshows;
        }


    }

    public List<Tvshow> getTvshowsWithRating(int r)

    {

        // business logics
        Query query = new Query();
        query.addCriteria(Criteria.where("rating" ).is(r));
        List<Tvshow> tvshows = mongoTemplate.find(query, Tvshow.class);
        return tvshows;


    }

    public List<Tvshow> getTvshowsWithTitleFitsRegex(String r)

    {
        if(r == null || r.isEmpty()) {
            return new ArrayList<Tvshow>();
        }
        else{
            List<Tvshow> temp = mongoTemplate.find(new Query(Criteria.where("title").regex("\\w*" + r + "\\w*","i")), Tvshow.class);
            return temp;
        }



    }

    public boolean insertIntoTvshows(Tvshow tvshow)
    {


        if (isValid(tvshow)){
            repository.insert(tvshow);
            return true;
        }
        return false;


    }

    public Optional<Tvshow> getATvshow(String id) throws Exception
    {

        Optional<Tvshow> tvshow = repository.findById(id);

        // This is saying that if tvshow ref variable does not contain a value then

        if (!tvshow.isPresent())
        {
            throw new Exception (" Tvshow with " + id + " is not found ");
        }

        return tvshow;

    }

    public boolean deleteATvshow( String id)
    {

        Optional<Tvshow> tvshow = repository.findById(id);

        // This is saying that if tvshow ref variable does not contain a value then

        if (!tvshow.isPresent())
        {
            return false;
        }


        repository.deleteById(id);
        return true;
    }

    public boolean updateATvshow(Tvshow tvshow){
        Optional<Tvshow> searchResult = repository.findById(tvshow.get_id());

        if (!searchResult.isPresent())
        {
            return false;
        }
        else {
            if (isValid(tvshow)){
                repository.save(tvshow);
                return true;
            }
            else{
                return false;
            }

        }
    }

    private boolean isValid(Tvshow tvshow){
        if (tvshow.getTitle()!=null && tvshow.getTitle().length()>0 && tvshow.getDescription()!=null && tvshow.getDescription().length()>0 && tvshow.getCategory()!=null && tvshow.getCategory().length()>0 && tvshow.getRating()!=0 && tvshow.getRent()!=0 && tvshow.getBuy()!=0 && tvshow.getSmallPoster()!=null && tvshow.getSmallPoster().length()>0 && tvshow.getLargePoster()!=null && tvshow.getLargePoster().length()>0 ){
            if (tvshow.getRent()>0 && tvshow.getBuy()>0 && tvshow.getRating()>=0 && tvshow.getRating()<6){
                return true;
            }
            else {
                return false;
            }

        }
        return false;
    }


}