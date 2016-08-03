package nl.mwinkels.photomanager.data;


import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;


@Entity
public class RotationSuggestion extends Suggestion {

    public enum Direction {CLOCK, COUNTER_CLOCK}

//    @Enumerated(EnumType.STRING)
    private Direction direction;

    private RotationSuggestion() {}

    public RotationSuggestion(Direction direction) {
        this.direction = direction;
    }
}
