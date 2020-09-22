package finki.ukim.mk.map_application.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Pin implements Serializable {

    @PrimaryKey
    private Integer id;

    private String name;

    private Double latitude;

    private Double longitude;

    private String description;

    private String image;

    private int map_id;

    @Ignore
    private byte[] imageFile;
    @Ignore
    private String imageName;
    @Ignore boolean imageUploaded;

//    @Ignore
//    private boolean isSaved;

    public Pin(){

    }

    public Pin(Integer id, String name, Double latitude, Double longitude, String description, String image) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.image = image;
    }

    //Getters

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public int getMap_id() {
        return map_id;
    }


    //Setters


    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setMap_id(int map_id) {
        this.map_id = map_id;
    }


    // IMAGE PROPS


    public byte[] getImageFile() {
        return imageFile;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageFile(byte[] imageFile) {
        this.imageFile = imageFile;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public boolean isImageUploaded() {
        return imageUploaded;
    }

    public void setImageUploaded(boolean imageUploaded) {
        this.imageUploaded = imageUploaded;
    }
}
