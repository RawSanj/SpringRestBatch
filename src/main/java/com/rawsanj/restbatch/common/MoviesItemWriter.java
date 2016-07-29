package com.rawsanj.restbatch.common;

import com.rawsanj.restbatch.entity.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import java.util.List;

/**
 * Created by Sanjay on 7/24/2016.
 */
public class MoviesItemWriter implements ItemWriter<Movie> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MoviesItemWriter.class);

    private static int movieNo = 1;

    @Override
    public void write(List<? extends Movie> items) throws Exception {

        LOGGER.info("Writing Movies with size {}", items.size() );

        items.forEach(movie-> {
            LOGGER.info("{}. {} - Released on {}", movieNo, movie.getTitle(), movie.getReleaseDate());
            movieNo++;
        });
    }

}
