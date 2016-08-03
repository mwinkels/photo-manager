package nl.mwinkels.photomanager.data;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(
        indexes = {
                @Index(name = "idx_file", columnList = "fileName, directory", unique = true),
                @Index(name = "idx_taken", columnList = "taken")
        }
)
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String directory;

    @Column(nullable = false)
    private String fileName;

    private Instant taken;
    private String latitude;
    private String longitude;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            joinColumns = @JoinColumn(name = "fkPhoto"),
            inverseJoinColumns = @JoinColumn(name = "fkSuggestion"))
    @OrderColumn(name = "pos", nullable = false)
    private List<Suggestion> suggestions;

    @OneToMany(mappedBy = "photo", cascade = CascadeType.ALL)
    private Set<DetectedFace> faces;

    private Photo() {
    }

    public Photo(String directory, String fileName, Instant taken, String latitude, String longitude) {
        this.directory = directory;
        this.fileName = fileName;
        this.taken = taken;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getFileName() {
        return fileName;
    }

    public List<Suggestion> getSuggestions() {
        if (suggestions == null) {
            suggestions = new ArrayList<>();
        }
        return suggestions;
    }

    public Set<DetectedFace> getFaces() {
        if (faces == null) {
            faces = new HashSet<>();
        }
        return faces;
    }

    public Path getPath(Path photoPath) {
        return photoPath.resolve(directory).resolve(fileName);
    }

    @Override
    public String toString() {
        return Paths.get(directory, fileName).toString();
    }

    public Instant getTaken() {
        return taken;
    }
}
