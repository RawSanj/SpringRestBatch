package com.rawsanj.restbatch;

import com.rawsanj.restbatch.jsontopojo.Movies;
import org.junit.*;
import org.junit.runner.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class RestMoviesTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestMoviesTest.class);

	private RestTemplate restTemplate;

    private String URL;

    @Before
    public void getRestTemplate(){
        this.restTemplate = new RestTemplate();
        this.URL = "https://api.themoviedb.org/3/movie/top_rated?api_key=YOUR_API_KEY";
    }

	@Test
	public void loadMoviesinMovies() {

        final String url = URL;

		Movies movies = restTemplate.getForObject(url,
				Movies.class);

        System.out.println("Total No of Pages: "+ movies.getTotalPages());
        assertThat(movies.getPage()).isEqualTo(1);

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

    @Test
    public void checkForAPILimit(){
        final String url = URL;

        ResponseEntity<Movies> response = restTemplate.getForEntity(url,
                Movies.class);

        LOGGER.info("HEADER INFO: {}. Status Code: {}", response.getHeaders(), response.getStatusCode());
        LOGGER.info("HEADER INFO: {}. Status Code: {}", Integer.parseInt(response.getHeaders().get("X-RateLimit-Remaining").get(0)));
        int apiRateLimit = Integer.parseInt(response.getHeaders().get("X-RateLimit-Limit").get(0));
        int apiRateLimitRemaining = Integer.parseInt(response.getHeaders().get("X-RateLimit-Remaining").get(0));
        assertThat(apiRateLimit).isEqualTo(40);
        assertThat(apiRateLimitRemaining).isLessThan(40);

    }

}
