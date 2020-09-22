package mk.finki.ukim.mk.map_application.model.android;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PinAndroid {

    private Integer id;
    private String name;
    private Double latitude;
    private Double longitude;
    private String description;
    private String image;
    private int map_id;
    private byte[] imageFile;
    private String imageName;
    private boolean imageUploaded;
}
