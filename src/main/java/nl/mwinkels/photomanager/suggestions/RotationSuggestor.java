package nl.mwinkels.photomanager.suggestions;


import nl.mwinkels.photomanager.data.Photo;
import nl.mwinkels.photomanager.data.RotationSuggestion;
import nl.mwinkels.photomanager.data.Suggestion;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TIFF;

import static nl.mwinkels.photomanager.data.RotationSuggestion.Direction.CLOCK;
import static nl.mwinkels.photomanager.data.RotationSuggestion.Direction.COUNTER_CLOCK;


public class RotationSuggestor implements Suggestor {
    @Override
    public Suggestion propose(Photo photo, Metadata metadata) {
        String orientation = metadata.get(TIFF.ORIENTATION);
        if (orientation != null) {
            switch (orientation) {
                case "6":
                    return new RotationSuggestion(CLOCK);
                case "8":
                    return new RotationSuggestion(COUNTER_CLOCK);
            }
        }
        return null;
    }
}
