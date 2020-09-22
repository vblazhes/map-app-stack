package finki.ukim.mk.map_application.model;

import androidx.room.*;
import finki.ukim.mk.map_application.database.converters.VisibilityTypeConverter;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

@Entity
@TypeConverters(VisibilityTypeConverter.class)
public class Map implements Serializable {

    @PrimaryKey
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

    @Ignore
    private byte[] imageBytes;
    @Ignore
    private String imageName;


    @Embedded(prefix = "owner_")
    private User owner;

    public Map(){
    }

    public Map(int id, int default_zoom, double center_latitude, double center_longitude, String style, String name, String background, String description, Visibility visibility, int approved, int imageFile, User owner) {
        this.id = id;
        this.default_zoom = default_zoom;
        this.center_latitude = center_latitude;
        this.center_longitude = center_longitude;
        this.style = style;
        this.name = name;
        this.background = background;
        this.description = description;
        this.visibility = visibility;
        this.approved = approved;
        this.imageFile = imageFile;
        this.owner = owner;
        this.last_update = new Date().getTime();
    }
    //Getters
    public int getId() {
        return id;
    }

    public int getDefault_zoom() {
        return default_zoom;
    }

    public double getCenter_latitude() {
        return center_latitude;
    }

    public double getCenter_longitude() {
        return center_longitude;
    }

    public String getStyle() {
        return style;
    }

    public String getName() {
        return name;
    }

    public String getBackground() {
        return background;
    }

    public String getDescription() {
        return description;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public int getApproved() {
        return approved;
    }

    public int getImageFile() {
        return imageFile;
    }

    public User getOwner() {
        return owner;
    }

    public Long getLast_update() {
        return last_update;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setDefault_zoom(int default_zoom) {
        this.default_zoom = default_zoom;
    }

    public void setCenter_latitude(double center_latitude) {
        this.center_latitude = center_latitude;
    }

    public void setCenter_longitude(double center_longitude) {
        this.center_longitude = center_longitude;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public void setImageFile(int imageFile) {
        this.imageFile = imageFile;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setLast_update(Long last_update) {
        this.last_update = last_update;
    }

    ///////////////////////////////
    public byte[] getImageBytes() {
        return imageBytes;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
