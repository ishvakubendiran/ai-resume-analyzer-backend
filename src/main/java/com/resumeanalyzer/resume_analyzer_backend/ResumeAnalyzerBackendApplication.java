package com.resumeanalyzer.resumeanalyzerbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.resumeanalyzer.resumeanalyzerbackend")
@EnableJpaRepositories(basePackages = "com.resumeanalyzer.resumeanalyzerbackend.repository")
@EntityScan(basePackages = "com.resumeanalyzer.resumeanalyzerbackend.entity")
public class ResumeAnalyzerBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(ResumeAnalyzerBackendApplication.class, args);
    }
}