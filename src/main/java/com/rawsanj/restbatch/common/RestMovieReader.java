package com.rawsanj.restbatch.common;

import com.rawsanj.restbatch.jsontopojo.Movies;
import com.rawsanj.restbatch.jsontopojo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Stack;

/**
 * Created by Sanjay on 7/24/2016.
 */
public class RestMovieReader implements ItemReader<Result> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestMovieReader.class);

    private final String apiUrl;
    private final RestTemplate restTemplate;

    private int nextPageNo;
    private int totalPages;
    private int releaseYear;

    //Stack to store Movies fetched from REST API temporarily.
    private static Stack<Result> resultSatck = new Stack<>();

    public RestMovieReader(String apiUrl, RestTemplate restTemplate) {
        this.apiUrl = apiUrl;
        this.restTemplate = restTemplate;
        this.nextPageNo = 1;
        this.releaseYear = 0;
        this.totalPages = Integer.MAX_VALUE; //Set to Integer.MAX_VALUE as flag to starting point. Use this to Set the totalPages from Rest call for each YEAR.
    }

    @Override
    public Result read()  {

        readFromRestAPI();

        Result nextResult = null;
        if (!resultSatck.isEmpty()){
            nextResult = resultSatck.pop();
            LOGGER.info("Found movie with Title: {}", nextResult.getTitle());
        }

        return nextResult;
    }

    private void readFromRestAPI(){

        LOGGER.info("Reading the information with params=> nextPageNo: {}. releaseYear: {}. totalPages: {}", nextPageNo,releaseYear, totalPages);

        if (resultSatck.isEmpty()){                         // If resultSatck is empty, push Movies into resultSatck via REST call.

            if (releaseYear==0){                            // On init i.e. when releaseYear=0, set releaseYear=Lowest_Year from Rest Call from Movie DB.
                releaseYear = MoviesDates.LOWEST_YEAR;
            }

            if (releaseYear <= MoviesDates.HIGHEST_YEAR){    // Execute this block only if releaseYear is less between LOWEST_YEAR and HIGHEST_YEAR.
                if (nextPageNo>=totalPages){                 // Check if nextPageNo>=totalPages (i.e REST API for current releaseYear has not reached the last page). If nextPageNo>=totalPages reset params for Next Year.
                    resetReadParamsForNextYear();
                }
                if (nextPageNo <=totalPages){               // If nextPageNo <=totalPages, Push results from REST API into resultSatck.
                    pushMoviesInStackFromAPI();
                }
                nextPageNo++;                               // Increment nextPageNo for REST API page no.
            }
        }

    }

    // Reset ReadParams : Increment releaseYear, reset nextPageNo=1 and totalPages=Max_Value.
    private void resetReadParamsForNextYear(){
        LOGGER.info("Reseting Read Params For Year: {}.", releaseYear+1);
        releaseYear++;
        nextPageNo=1;
        totalPages=Integer.MAX_VALUE;
    }

    private void pushMoviesInStackFromAPI() {

        LOGGER.info("****************************************************************************");
        LOGGER.info("URL: {}. Page No: {}. Total Page: {}. For Year {}", apiUrl+ "&primary_release_year="+releaseYear+"&page="+ nextPageNo, nextPageNo, totalPages, releaseYear);
        LOGGER.info("****************************************************************************");

        ResponseEntity<Movies> response = restTemplate.getForEntity(apiUrl+ "&primary_release_year="+releaseYear+"&page="+ nextPageNo,
                Movies.class);

        if (totalPages == Integer.MAX_VALUE){
            this.totalPages = response.getBody().getTotalPages();
            LOGGER.info("Total No. of Movies is {}. Total Pages: {}", response.getBody().getTotalResults(), response.getBody().getTotalPages());
        }

        if (Integer.parseInt(response.getHeaders().get("X-RateLimit-Remaining").get(0))<5){
            LOGGER.info("HEADER INFO: {}.", response.getHeaders().get("X-RateLimit-Limit"));
            LOGGER.info("********************************************Sleeping for 1 MINUTE********************************************");
            try {
                Thread.sleep(60_000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        // Check if response has Movies i.e. there are Movies released in X years and push movies in resultSatck.
        // If response is empty i.e. No Movie data for X Year then call readFromRestAPI() to continue for next Year.
        if (response.getBody().getTotalResults()!=0){
            response.getBody().getResults().forEach(result -> resultSatck.push(result));
        }else {
            readFromRestAPI();
        }

//        LOGGER.info("All HEADERS INFO: ");
//        HttpHeaders headers = response.getHeaders();
//        headers.forEach((key, value)->  LOGGER.info("Key: {}. Value: {}", key, value.toString()) );

    }
}
