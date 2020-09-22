package mk.finki.ukim.mk.map_application.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import mk.finki.ukim.mk.map_application.model.Auth.User;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "Maps")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Map {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int default_zoom;
    private double center_latitude;
    private double center_longitude;
    private String style;
    private String name;
    @Column(length = 500)
    private String background;
    private String description;
    @Enumerated(EnumType.STRING)
    private Visibility visibility;
    private int approved;
    private int imageFile;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "map")
    private List<Pin> pins;

    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;
}
