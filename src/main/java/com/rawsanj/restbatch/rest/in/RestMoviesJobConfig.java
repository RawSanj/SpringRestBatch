package com.rawsanj.restbatch.rest.in;

import com.rawsanj.restbatch.common.MoviesItemProcessor;
import com.rawsanj.restbatch.common.MoviesItemWriter;
import com.rawsanj.restbatch.common.RestMovieReader;
import com.rawsanj.restbatch.entity.Movie;
import com.rawsanj.restbatch.jsontopojo.Result;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Sanjay on 7/24/2016.
 */
@Configuration
@EnableBatchProcessing
public class RestMoviesJobConfig {

//    @Autowired
//    private MovieRepository movieRepository;

    @Bean
    ItemReader<Result> restMovieReader(Environment env, RestTemplate restTemplate) {
        return new RestMovieReader(env.getProperty("REST_API_URL_WITH_KEY"), restTemplate);
    }

    @Bean
    ItemProcessor<Result, Movie> moviesItemProcessor() {
        return new MoviesItemProcessor();
    }

    @Bean
    ItemWriter<Movie> moviesItemWriter() {
        return new MoviesItemWriter();
    }

    @Bean
    Step restMovieStep(ItemReader<Result> restMovieReader,
                         ItemProcessor<Result, Movie> moviesItemProcessor,
                         ItemWriter<Movie> moviesItemWriter,
                         StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("restMovieStep")
                .<Result, Movie>chunk(10)
                .reader(restMovieReader)
                .processor(moviesItemProcessor)
                .writer(moviesItemWriter)
                .build();
    }

    @Bean
    Job restMovieJob(JobBuilderFactory jobBuilderFactory,
                       @Qualifier("restMovieStep") Step restMovieStep) {
        return jobBuilderFactory.get("restMovieJob")
                .incrementer(new RunIdIncrementer())
                .flow(restMovieStep)
                .end()
                .build();
    }

}
