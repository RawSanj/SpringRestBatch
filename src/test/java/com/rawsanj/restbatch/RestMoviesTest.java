package com.rawsanj.restbatch;

import com.rawsanj.restbatch.jsontopojo.Movies;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class RestMoviesTest {

	private RestTemplate restTemplate;

    @Before
    public void getRestTemplate(){
        this.restTemplate = new RestTemplate();
    }

	@Test
	public void loadMoviesinMovies() {

        final String url = "https://api.themoviedb.org/3/movie/top_rated?api_key=YOUR_API_KEY";

		Movies movies = restTemplate.getForObject(url,
				Movies.class);

        System.out.println("Total No of Pages: "+ movies.getTotalPages());

//        List<Movies> allMovies = new ArrayList<>();
//
//        for (int i=1; i<=movies.getTotalPages();i++){
//
//            Movies m = restTemplate.getForObject(url+"&page="+i, Movies.class);
//            allMovies.add(m);
//
//        }
//
//        allMovies.forEach(movPage -> {
//            movies.getResults().forEach(mov -> System.out.println(mov.getTitle()));
//        });
    }

}
