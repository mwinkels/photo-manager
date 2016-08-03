package nl.mwinkels.photomanager.suggestions;


import nl.mwinkels.photomanager.loader.ParsedPhoto;
import nl.mwinkels.photomanager.loader.PhotoFileParser;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class AbstractTestCase {
    private final PhotoFileParser parser = new PhotoFileParser(path("/"));

    protected static Path path(String resource) {
        try {
            return Paths.get(RotationSuggestorTest.class.getResource(resource).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    protected ParsedPhoto photo(Path path) {
        return parser.parse(path);
    }
}
