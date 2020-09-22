package mk.finki.ukim.mk.map_application.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "Pins")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Pin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Double latitude;
    private Double longitude;
    @Column(length = 700)
    private String description;
    @Enumerated(EnumType.STRING)
    private Category category;
    @Column(length = 500)
    private String image;
  //  private String pin_image;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Map map;
}
