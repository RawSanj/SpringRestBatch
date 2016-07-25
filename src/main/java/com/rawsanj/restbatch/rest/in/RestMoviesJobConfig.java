package com.rawsanj.restbatch.rest.in;

import com.rawsanj.restbatch.common.MoviesItemProcessor;
import com.rawsanj.restbatch.common.MoviesItemWriter;
import com.rawsanj.restbatch.common.RestMovieReader;
import com.rawsanj.restbatch.jsontopojo.Movie;
import com.rawsanj.restbatch.jsontopojo.Movies;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by Sanjay on 7/24/2016.
 */
@Configuration
@EnableBatchProcessing
public class RestMoviesJobConfig {

    @Bean
    ItemReader<Movies> restMovieReader(Environment env, RestTemplate restTemplate) {
        return new RestMovieReader(env.getProperty("REST_API_URL_WITH_KEY"), restTemplate);
    }

    @Bean
    ItemProcessor<Movies, Movies> moviesItemProcessor() {
        return new MoviesItemProcessor();
    }

    @Bean
    ItemWriter<Movies> moviesItemWriter() {
        return new MoviesItemWriter();
    }
//    @Bean
//    public JdbcBatchItemWriter<Result> writer() {
//        JdbcBatchItemWriter<Result> writer = new JdbcBatchItemWriter<Result>();
//        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Result>());
//        writer.setSql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)");
//        writer.setDataSource(dataSource);
//        return writer;
//    }

    @Bean
    Step restMovieStep(ItemReader<Movies> restMovieReader,
                         ItemProcessor<Movies, Movies> moviesItemProcessor,
                         ItemWriter<Movies> moviesItemWriter,
                         StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("restMovieStep")
                .<Movies, Movies>chunk(1)
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
