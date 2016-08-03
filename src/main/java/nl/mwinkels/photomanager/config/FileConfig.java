package nl.mwinkels.photomanager.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;


@Configuration
public class FileConfig {

    @Bean
    public Path photoPath(@Value("${photoPath:.}") String photoPath) {
        return Paths.get(photoPath);
    }
}
