package mk.finki.ukim.mk.map_application.model.android;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mk.finki.ukim.mk.map_application.model.Auth.User;
import mk.finki.ukim.mk.map_application.model.Visibility;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MapAndroid {
    private int id;
    private int default_zoom;
    private double center_latitude;
    private double center_longitude;
    private String style;
    private String name;
    private String background;
    private String description;
    private Visibility visibility;
    private int approved;
    private int imageFile;
    private Long last_update;
    private byte [] imageBytes;
    private String imageName;
    private User owner;

}
