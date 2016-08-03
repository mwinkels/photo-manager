package nl.mwinkels.photomanager.suggestions;


import nl.mwinkels.photomanager.data.Suggestion;
import nl.mwinkels.photomanager.loader.ParsedPhoto;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


public class RotationSuggestorTest extends AbstractTestCase {

    private final RotationSuggestor rotationSuggestor = new RotationSuggestor();

    @Test
    public void shouldSuggestRotation() throws Exception {
        assertNotNull(suggestion(photo(path("/20160513_175024.jpg"))));
    }

    @Test
    public void shouldNotSuggestRotation() throws Exception {
        assertNull(suggestion(photo(path("/DSCN2711.JPG"))));
    }

    private Suggestion suggestion(ParsedPhoto parsedPhoto) {
        return rotationSuggestor.propose(parsedPhoto.getPhoto(), parsedPhoto.getMetadata());
    }
}