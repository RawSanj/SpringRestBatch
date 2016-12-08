package com.rawsanj.restbatch;

import com.rawsanj.restbatch.common.MoviesDates;
import com.rawsanj.restbatch.jsontopojo.Movies;
import com.rawsanj.restbatch.jsontopojo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableBatchProcessing
public class SpringRestBatchApplication {


	private static final Logger LOGGER = LoggerFactory.getLogger(SpringRestBatchApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringRestBatchApplication.class, args);
	}

	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private Environment environment;

	// Initialize the Lowest and Highest Year when a Movie was/wud be released.
	// Call Rest Endpoint of MovieDb with sorted by release_date param.
	@Bean
	CommandLineRunner getCommandLineRunner(){
		return args -> {

			String apiUrl = environment.getProperty("REST_API_URL_WITH_KEY");

			ResponseEntity<Movies> oldestMovies = restTemplate.getForEntity(apiUrl+"&sort_by=release_date.asc",
					Movies.class);

			Result oldestMovieResult = oldestMovies.getBody().getResults().get(0);
			String[] release_date_oldestMovie = oldestMovieResult.getReleaseDate().split("-");
			MoviesDates.LOWEST_YEAR = Integer.parseInt(release_date_oldestMovie[0]);

			ResponseEntity<Movies> lastestMovies = restTemplate.getForEntity(apiUrl+"&sort_by=release_date.desc",
					Movies.class);
			Result lastestMovieResult = lastestMovies.getBody().getResults().get(0);
			String[] release_date_lastestMovie = lastestMovieResult.getReleaseDate().split("-");
			MoviesDates.HIGHEST_YEAR = Integer.parseInt(release_date_lastestMovie[0]);

			LOGGER.info("*************************************************************************************");
			LOGGER.info("The Movie DB Service Contains moves from the Year - {} till the Year - {}",
					MoviesDates.LOWEST_YEAR, MoviesDates.HIGHEST_YEAR);
			LOGGER.info("*************************************************************************************");

		};
	}
}
