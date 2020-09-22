package mk.finki.ukim.mk.map_application.service;

import mk.finki.ukim.mk.map_application.model.Category;
import mk.finki.ukim.mk.map_application.model.Pin;
import mk.finki.ukim.mk.map_application.model.android.PinAndroid;

import java.io.IOException;
import java.util.List;

public interface PinsService {
    List<Pin> getAllPins();

    List<Pin> gatAllPinsByCategory(Category category);

    List<Pin> getAllMapPins(Integer map_id);

    Pin addPin(Integer map_id, Double latitude, Double longitude, String name, String description, Category category, String image);

    Pin savePinAndroid(Integer map_id,PinAndroid newPin) throws IOException;

    Pin updatePin(Pin updatedPin, int map_id);

    Pin updatePinAndroid(PinAndroid updatedPin, int map_id) throws IOException;

    void deletePinById(Integer pin_id);
}
