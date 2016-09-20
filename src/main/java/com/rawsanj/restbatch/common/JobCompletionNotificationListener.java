package com.rawsanj.restbatch.common;

import com.rawsanj.restbatch.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Sanjay on 7/30/2016.
 */

public class JobCompletionNotificationListener implements JobExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public void beforeJob(JobExecution jobExecution) {

    }

    @Override
    public void afterJob(JobExecution jobExecution) {

        LOGGER.info("RUNNING JobCompletion Check.");

        long cnt = movieRepository.count();

        LOGGER.info("Total Movies Saved: {}", cnt);

//        Shutdown when Job is Finished (for Windows PC)
//        Runtime runtime = Runtime.getRuntime();
//        String command = "shutdown /s";
//        try {
//            Process proc = runtime.exec(command);
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//            e.printStackTrace();
//        }

     }
}
