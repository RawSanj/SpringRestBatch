package com.rawsanj.restbatch.common;

import com.rawsanj.restbatch.domain.Movie;
import com.rawsanj.restbatch.jsontopojo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Sanjay on 7/24/2016.
 */
public class MoviesItemProcessor implements ItemProcessor<Result, Movie> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MoviesItemProcessor.class);

    @Autowired
    private ResultToMovie resultToMovie;

    private static int movieNo = 1;

    @Override
    public Movie process(Result result) throws Exception {

        //LOGGER.info("Converting Result to Movie. Number# {}",movieNo);
        movieNo++;

        Movie movie = resultToMovie.convert(result);
        return movie;

    }
}
