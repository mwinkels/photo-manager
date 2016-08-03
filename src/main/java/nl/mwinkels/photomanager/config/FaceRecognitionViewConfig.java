package nl.mwinkels.photomanager.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@Import(value = {DatabaseConfig.class, FileConfig.class})
public class FaceRecognitionViewConfig {
}
