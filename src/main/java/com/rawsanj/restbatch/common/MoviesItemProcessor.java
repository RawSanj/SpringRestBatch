package com.rawsanj.restbatch.common;

import com.rawsanj.restbatch.jsontopojo.Movie;
import com.rawsanj.restbatch.jsontopojo.Movies;
import com.rawsanj.restbatch.jsontopojo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Sanjay on 7/24/2016.
 */
public class MoviesItemProcessor implements ItemProcessor<Movies, Movies> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MoviesItemProcessor.class);

    @Autowired
    private ResultToMovie resultToMovie;

    @Override
    public Movies process(Movies item) throws Exception {

        LOGGER.info("Processing Movie information of page no: {}", item.getPage());

        return item;
    }
}
