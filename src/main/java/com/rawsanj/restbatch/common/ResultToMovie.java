package com.rawsanj.restbatch.common;


import com.rawsanj.restbatch.entity.Movie;
import com.rawsanj.restbatch.jsontopojo.Result;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Created by Sanjay on 7/25/2016.
 */
@Component
public class ResultToMovie implements Converter<Result, Movie> {

    @Override
    public Movie convert(Result source) {
        Movie mov = new Movie(source.getPosterPath(), source.getAdult(),
                                source.getOverview(), source.getReleaseDate(),
                                source.getGenreIds(), source.getId(),
                                source.getOriginalTitle(), source.getOriginalLanguage(),
                                source.getTitle(), source.getBackdropPath(),
                                source.getPopularity(),source.getVoteCount(),
                                source.getVideo(), source.getVoteAverage());

        return mov;
    }
}
