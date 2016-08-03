package nl.mwinkels.photomanager.suggestions;


import nl.mwinkels.photomanager.data.Photo;
import nl.mwinkels.photomanager.data.Suggestion;
import org.apache.tika.metadata.Metadata;


public interface Suggestor {
    Suggestion propose(Photo photo, Metadata metadata);
}
