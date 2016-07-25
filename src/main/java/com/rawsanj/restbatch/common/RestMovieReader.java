package com.rawsanj.restbatch.common;

import com.rawsanj.restbatch.jsontopojo.Movies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sanjay on 7/24/2016.
 */
public class RestMovieReader implements ItemReader<Movies> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestMovieReader.class);

    private final String apiUrl;
    private final RestTemplate restTemplate;

    private int nextPageNo;
    private int totalPages;

    private Movies moviesData = null;

    public RestMovieReader(String apiUrl, RestTemplate restTemplate) {
        this.apiUrl = apiUrl;
        this.restTemplate = restTemplate;
        this.nextPageNo = 1;
        this.totalPages = -100; //Set to -100 as flag to starting point. USe this to Set the totalPages from Rest call only once.
    }

    @Override
    public Movies read() throws Exception {

        LOGGER.info("Reading the information of the next page of Movies");

        Movies nextMovies = null;

        if (totalPages != 0){

            nextMovies = fetchmoviesDataFromAPI();
            nextPageNo++;
            totalPages--;

        }else {
            return null;
        }

        LOGGER.info("Found movies: {}", nextMovies);

        return nextMovies;
    }

    private Movies fetchmoviesDataFromAPI() {

        LOGGER.debug("Fetching Movies data from an external API by using the url: {}", apiUrl);

        Movies movies = restTemplate.getForObject(apiUrl+nextPageNo,
                Movies.class);
        if (totalPages == -100){
            this.totalPages = movies.getTotalPages();
        }

        return movies;
    }
}
