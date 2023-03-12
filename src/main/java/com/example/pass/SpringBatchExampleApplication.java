package com.example.pass;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@EnableBatchProcessing
@SpringBootApplication
@RequiredArgsConstructor
public class SpringBatchExampleApplication {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchExampleApplication.class, args);
    }

    @Bean
    public Step passStep() {
        return this.stepBuilderFactory.get("passStep")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("Execute PassTep");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Job passJob() {
        return this.jobBuilderFactory.get("passJob")
                .start(passStep())
                .build();
    }

}
