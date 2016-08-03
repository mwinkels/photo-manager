package nl.mwinkels.photomanager.suggestions;


import nl.mwinkels.photomanager.data.Photo;
import nl.mwinkels.photomanager.data.Suggestion;
import nl.mwinkels.photomanager.loader.ParsedPhoto;


public class SuggestionsProposer {

    private final Suggestor[] suggestors;

    public SuggestionsProposer(Suggestor[] suggestors) {
        this.suggestors = suggestors;
    }

    public Photo propose(ParsedPhoto parsedPhoto) {
        Photo photo = parsedPhoto.getPhoto();
        for (Suggestor suggestor : suggestors) {
            Suggestion s = suggestor.propose(photo, parsedPhoto.getMetadata());
            if (s != null) {
                photo.getSuggestions().add(s);
            }
        }
        return photo;
    }
}
