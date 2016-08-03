package nl.mwinkels.photomanager.loader;


import nl.mwinkels.photomanager.PhotoFacesWindow;
import nl.mwinkels.photomanager.data.Photo;
import nl.mwinkels.photomanager.suggestions.AbstractTestCase;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.IOException;


public class FacesRecognizerTest extends AbstractTestCase {

    private final FacesRecognizer facesRecognizer = new FacesRecognizer(path("/"));

    @Test
    public void shouldRecognizeFace() throws Exception {
        showImage("/DSCN2711.JPG");
        showImage("/DSCN2685.JPG");
        showImage("/20160513_175024.jpg");
        Thread.sleep(20000);
    }

    private void showImage(String resource) throws IOException {
        Photo processed = facesRecognizer.process(photo(path(resource)).getPhoto());
//        assertFalse(processed.getFaces().isEmpty());
//        PhotoFacesWindow window = new PhotoFacesWindow(ImageIO.read(processed.getPath(path("/")).toFile()), processed.getFaces());
//        window.setVisible(true);
    }
}