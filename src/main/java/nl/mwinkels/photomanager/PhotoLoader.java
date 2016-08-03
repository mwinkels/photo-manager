package nl.mwinkels.photomanager;


import nl.mwinkels.photomanager.config.PhotoLoaderConfig;
import nl.mwinkels.photomanager.dao.PhotoDao;
import nl.mwinkels.photomanager.data.Photo;
import nl.mwinkels.photomanager.loader.FacesRecognizer;
import nl.mwinkels.photomanager.loader.ParsedPhoto;
import nl.mwinkels.photomanager.loader.PhotoFileParser;
import nl.mwinkels.photomanager.suggestions.SuggestionsProposer;
import org.apache.poi.ss.formula.functions.T;
import org.apache.tika.Tika;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.stream.Stream;


public class PhotoLoader {

    private final Path                photoPath;
    private final PhotoFileParser     photoFileParser;
    private final SuggestionsProposer suggestions;
    private final PhotoDao            photoDao;
    private final FacesRecognizer     facesRecognizer;

    public static void main(String... args) throws IOException {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.getEnvironment().getPropertySources().addFirst(new SimpleCommandLinePropertySource(args));
        applicationContext.register(PhotoLoaderConfig.class);
        applicationContext.refresh();
        PhotoLoader photoLoader = applicationContext.getBean(PhotoLoader.class);
        photoLoader.start();
    }

    public PhotoLoader(Path photoPath, PhotoFileParser photoFileParser, SuggestionsProposer suggestions, PhotoDao photoDao, FacesRecognizer facesRecognizer) {
        this.photoPath = photoPath;
        this.photoFileParser = photoFileParser;
        this.suggestions = suggestions;
        this.photoDao = photoDao;
        this.facesRecognizer = facesRecognizer;
    }

    private void start() throws IOException {
        System.out.printf("Start loading (from: %s).%n", photoPath);
        try (Stream<Path> s = Files.walk(photoPath)) {
            s.filter(p -> p.toFile().isFile())
             .peek(System.out::println)
             .map(photoFileParser::parse)
             .map(suggestions::propose)
             .map(facesRecognizer::process)
             .forEach(photoDao::persist);
        }
    }

}

