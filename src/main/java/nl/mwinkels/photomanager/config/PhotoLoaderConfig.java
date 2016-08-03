package nl.mwinkels.photomanager.config;


import nl.mwinkels.photomanager.PhotoLoader;
import nl.mwinkels.photomanager.dao.PhotoDao;
import nl.mwinkels.photomanager.loader.FacesRecognizer;
import nl.mwinkels.photomanager.loader.PhotoFileParser;
import nl.mwinkels.photomanager.suggestions.RotationSuggestor;
import nl.mwinkels.photomanager.suggestions.SuggestionsProposer;
import nl.mwinkels.photomanager.suggestions.Suggestor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.nio.file.Path;


@Configuration
@Import({DatabaseConfig.class, FileConfig.class})
public class PhotoLoaderConfig {

    @Bean
    public PhotoLoader photoLoader(Path photoPath, PhotoFileParser photoFileParser, PhotoDao photoDao, SuggestionsProposer suggestions, FacesRecognizer facesRecognizer) {
        return new PhotoLoader(photoPath, photoFileParser, suggestions, photoDao, facesRecognizer);
    }

    @Bean
    public PhotoFileParser photoFileParser(Path photoPath) {
        return new PhotoFileParser(photoPath);
    }

    @Bean
    public SuggestionsProposer suggestions(Suggestor[] suggestors) {
        return new SuggestionsProposer(suggestors);
    }

    @Bean
    public Suggestor rotationSuggestor() {
        return new RotationSuggestor();
    }

    @Bean
    public FacesRecognizer facesRecognizer(Path photoPath) {
        return new FacesRecognizer(photoPath);
    }
}
