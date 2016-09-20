package com.rawsanj.restbatch.repository;

import com.rawsanj.restbatch.domain.Movie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Sanjay on 7/27/2016.
 */
@Repository
public interface MovieRepository extends CrudRepository<Movie, Long> {
}
