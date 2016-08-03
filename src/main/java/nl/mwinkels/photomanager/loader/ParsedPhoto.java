package nl.mwinkels.photomanager.loader;


import nl.mwinkels.photomanager.data.Photo;
import org.apache.tika.metadata.Metadata;


public class ParsedPhoto {

    private final Photo    photo;
    private final Metadata metadata;

    public ParsedPhoto(Photo photo, Metadata metadata) {
        this.photo = photo;
        this.metadata = metadata;
    }

    public Photo getPhoto() {
        return photo;
    }

    public Metadata getMetadata() {
        return metadata;
    }
}
