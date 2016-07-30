package com.rawsanj.restbatch.common;

import com.rawsanj.restbatch.entity.Movie;
import com.rawsanj.restbatch.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Sanjay on 7/30/2016.
 */

public class JobCompletionNotificationListener implements JobExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public void beforeJob(JobExecution jobExecution) {

    }

    @Override
    public void afterJob(JobExecution jobExecution) {

        LOGGER.info("RUNNING JobCompletion Check.");

        List<Movie> moviesInDB = new ArrayList<>();
        movieRepository.findAll().forEach(moviesInDB::add);

        LOGGER.info("Movies stored in Database: {}", moviesInDB.size());
        moviesInDB.forEach(movie -> LOGGER.info("Title - {}. Release Data - {}. Language - {}", movie.getTitle(), movie.getReleaseDate(), movie.getOriginalLanguage()));
    }
}
