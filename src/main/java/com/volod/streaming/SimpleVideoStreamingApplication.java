package com.volod.streaming;

import com.volod.streaming.domain.model.Video;
import com.volod.streaming.repositories.VideoRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@OpenAPIDefinition(
        info = @Info(
                title = "${spring.application.name}"
        )
)
@SpringBootApplication
public class SimpleVideoStreamingApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleVideoStreamingApplication.class, args);
    }

    @Profile("dev")
    @Bean
    CommandLineRunner commandLineRunner(
            VideoRepository videoRepository
    ) {
        return args -> {
            if (videoRepository.count() == 0) {
                var films = IntStream.range(0, 250)
                        .mapToObj(i -> Video.random(false))
                        .collect(Collectors.toSet());
                videoRepository.saveAll(films);
            }
        };
    }

}
