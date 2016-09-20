package com.rawsanj.restbatch.configuration;

import com.rawsanj.restbatch.common.JobCompletionNotificationListener;
import com.rawsanj.restbatch.common.MoviesItemProcessor;
import com.rawsanj.restbatch.common.RestMovieReader;
import com.rawsanj.restbatch.domain.Movie;
import com.rawsanj.restbatch.jsontopojo.Result;
import com.rawsanj.restbatch.repository.MovieRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Sanjay on 9/21/2016.
 */
@Configuration
public class JobConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private MovieRepository movieRepository;

    @Bean
    ItemReader<Result> restMovieReader(Environment env, RestTemplate restTemplate) {
        return new RestMovieReader(env.getProperty("REST_API_URL_WITH_KEY"), restTemplate);
    }

    @Bean
    ItemProcessor<Result, Movie> moviesItemProcessor() {
        return new MoviesItemProcessor();
    }

    @Bean
    RepositoryItemWriter<Movie> movieRepositoryItemWriter(){

        RepositoryItemWriter<Movie> repositoryItemWriter = new RepositoryItemWriter<>();
        repositoryItemWriter.setRepository(movieRepository);
        repositoryItemWriter.setMethodName("save");
        return repositoryItemWriter;

    }

    @Bean
    public JobExecutionListener listener() {
        return new JobCompletionNotificationListener();
    }

    @Bean
    public Step step1(ItemReader<Result> restMovieReader,
                        ItemProcessor<Result, Movie> moviesItemProcessor,
                        RepositoryItemWriter<Movie> movieRepositoryItemWriter) throws Exception {
        return stepBuilderFactory.get("step1")
                .<Result, Movie>chunk(10)
                .reader(restMovieReader)
                .processor(moviesItemProcessor)
                .writer(movieRepositoryItemWriter)
                .build();
    }

    @Bean
    public Job job(Step step1) throws Exception {
        return jobBuilderFactory.get("job")
                .start(step1)
                .listener(listener())
                .build();
    }


}
