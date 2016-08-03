package nl.mwinkels.photomanager.loader;


import nl.mwinkels.photomanager.data.Photo;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Geographic;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TIFF;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.jpeg.JpegParser;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;


public class PhotoFileParser {

    private final Tika tika = new Tika();

    private final Path photoPath;

    public PhotoFileParser(Path photoPath) {
        this.photoPath = photoPath;
    }

    public ParsedPhoto parse(Path path) {
        Metadata metadata = new Metadata();
        try (Reader r = tika.parse(path, metadata)) {
            Date originalDate = metadata.getDate(TIFF.ORIGINAL_DATE);

            return new ParsedPhoto(
                    new Photo(
                            photoPath.relativize(path.getParent()).toString(),
                            path.getFileName().toString(),
                            originalDate == null ? null : originalDate.toInstant(),
                            metadata.get(Geographic.LATITUDE),
                            metadata.get(Geographic.LONGITUDE)),
                    metadata);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
