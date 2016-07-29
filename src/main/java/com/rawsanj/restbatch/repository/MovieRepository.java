package com.rawsanj.restbatch.repository;

import com.rawsanj.restbatch.entity.Movie;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Sanjay on 7/27/2016.
 */
public interface MovieRepository extends CrudRepository<Movie, Long> {
}
