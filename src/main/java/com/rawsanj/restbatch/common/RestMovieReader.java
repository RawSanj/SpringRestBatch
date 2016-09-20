package com.rawsanj.restbatch.common;

import com.rawsanj.restbatch.jsontopojo.Movies;
import com.rawsanj.restbatch.jsontopojo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
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

    //Stack to store Movies fetched from REST API temporarily.
    private static Stack<Result> resultSatck = new Stack<>();

    public RestMovieReader(String apiUrl, RestTemplate restTemplate) {
        this.apiUrl = apiUrl;
        this.restTemplate = restTemplate;
        this.nextPageNo = 1;
        this.totalPages = -100; //Set to -100 as flag to starting point. USe this to Set the totalPages from Rest call only once.
    }

    @Override
    public Result read() throws Exception {

        LOGGER.info("Reading the information of the next page of Movies");

        if (resultSatck.isEmpty()){          // Check If resultSatck is empty else pop Movie from stack and return it.

            if (totalPages != 0){           // Check if totalPage!=0 (i.e REST API has not reached the last page) continue else return null and complete the Job.
                pushMoviesInStackFromAPI(); // Push results from REST API into resultSatck.
                nextPageNo++;               // Increment nextPageNo for REST API page no.
                totalPages--;               // Decrement totalPages to signal read() to stop the job and API reached last page.
            }else {
                return null;
            }
        }

        Result nextResult = resultSatck.pop();
        LOGGER.info("Found movie with Title: {}", nextResult.getTitle());

        Thread.sleep(100); // Add sleep to avoid API Rate Limit Lock

        return nextResult;
    }

    private void pushMoviesInStackFromAPI() throws InterruptedException {

        LOGGER.debug("Pushing Movies in Stack from an external API by using the url: {}", apiUrl);

//        Movies moviesFromRestCall = restTemplate.getForObject(apiUrl+nextPageNo,
//                Movies.class);

        ResponseEntity<Movies> response = restTemplate.getForEntity(apiUrl+nextPageNo,
                Movies.class);
        if (totalPages == -100){
            this.totalPages = response.getBody().getTotalPages();
            LOGGER.info("Total No. of Movies is {}. Total Pages: {}", response.getBody().getTotalResults(), response.getBody().getTotalPages());
//            LOGGER.info("HEADER INFO: {}. Status Code: {}", response.getHeaders(), response.getStatusCode());
//            LOGGER.info("HEADER INFO: {}. Status Code: {}", response.getHeaders().get("X-RateLimit-Limit"));

            if (Integer.parseInt(response.getHeaders().get("X-RateLimit-Remaining").get(0))==1){
                Thread.sleep(60_000);
                LOGGER.info("HEADER INFO: {}.", response.getHeaders().get("X-RateLimit-Limit"));
                LOGGER.info("********************************************Sleeping for 1 MINUTE********************************************");
            }
        }

        response.getBody().getResults().forEach(result -> resultSatck.push(result));

    }
}
