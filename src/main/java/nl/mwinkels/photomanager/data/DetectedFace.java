package nl.mwinkels.photomanager.data;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.awt.*;


@Entity
public class DetectedFace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "fkPhoto", nullable = false)
    private Photo photo;

    private int x;
    private int y;
    private int width;
    private int height;

    private DetectedFace() {
    }

    public DetectedFace(Photo photo, int x, int y, int width, int height) {
        this.photo = photo;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle toRectangle(int scale) {
        return new Rectangle(x / scale, y / scale, width / scale, height / scale);
    }
}
