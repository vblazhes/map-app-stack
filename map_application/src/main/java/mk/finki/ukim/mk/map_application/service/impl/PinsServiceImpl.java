package mk.finki.ukim.mk.map_application.service.impl;

import mk.finki.ukim.mk.map_application.model.Category;
import mk.finki.ukim.mk.map_application.model.Map;
import mk.finki.ukim.mk.map_application.model.Pin;
import mk.finki.ukim.mk.map_application.model.Exception.MapNotFoundException;
import mk.finki.ukim.mk.map_application.model.android.PinAndroid;
import mk.finki.ukim.mk.map_application.repository.MapsJPARepository;
import mk.finki.ukim.mk.map_application.repository.PinsJPARepository;
import mk.finki.ukim.mk.map_application.service.PinsService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PinsServiceImpl implements PinsService {

    private final PinsJPARepository pinsJPARepository;
    private final MapsJPARepository mapsJPARepository;

    PinsServiceImpl(PinsJPARepository pinsJPARepository, MapsJPARepository mapsJPARepository) {
        this.mapsJPARepository = mapsJPARepository;
        this.pinsJPARepository = pinsJPARepository;
    }

    @Override
    public List<Pin> getAllPins() {
        return pinsJPARepository.findAll();
    }

    @Override
    public List<Pin> gatAllPinsByCategory(Category category) {
        return pinsJPARepository.findAllByCategory(category);
    }

    @Override
    public List<Pin> getAllMapPins(Integer map_id) {
        Map map = mapsJPARepository.findById(map_id).orElseThrow(MapNotFoundException::new);
        return map.getPins();
    }

    @Override
    public Pin addPin(Integer map_id, Double latitude, Double longitude, String name, String description, Category category, String image) {
        System.out.println("PINS CONTROLLER");

        Map currentMap = mapsJPARepository.findById(map_id).orElseThrow(MapNotFoundException::new);

        Pin newPin = new Pin();
        newPin.setName(name);
        newPin.setLatitude(latitude);
        newPin.setLongitude(longitude);
        newPin.setDescription(description);
        newPin.setCategory(category);
        newPin.setImage(image);
        newPin.setMap(currentMap);
        return pinsJPARepository.save(newPin);
    }

    @Override
    public Pin savePinAndroid(Integer map_id,PinAndroid newPin) throws IOException {
        Map currentMap = mapsJPARepository.findById(map_id).orElseThrow(MapNotFoundException::new);


        if(newPin.isImageUploaded()){
            // Save the image localy

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            String folder = "C:\\Users\\Pc\\IntelliJIDEAProjects\\wp_mpip_project\\map_application_frontend\\public\\images\\";
            byte[] bytes = newPin.getImageFile();
            String filename = newPin.getImageName();
            Path path = Paths.get(folder + filename);
            Files.write(path, bytes);

            newPin.setImage("/images/"+filename);
        }

        Pin pin = new Pin();
        pin.setName(newPin.getName());
        pin.setLatitude(newPin.getLatitude());
        pin.setLongitude(newPin.getLongitude());
        pin.setDescription(newPin.getDescription());
        pin.setCategory(Category.CHURCH);
        pin.setImage(newPin.getImage());
        pin.setMap(currentMap);

        return pinsJPARepository.save(pin);
    }

    @Override
    public Pin updatePin(Pin updatedPin, int map_id) {

        Map pin_map = mapsJPARepository.findById(map_id).orElseThrow(MapNotFoundException::new);
        updatedPin.setMap(pin_map);
        return pinsJPARepository.save(updatedPin);
    }

    @Override
    public Pin updatePinAndroid(PinAndroid updatedPin, int map_id) throws IOException {
        Map pin_map = mapsJPARepository.findById(map_id).orElseThrow(MapNotFoundException::new);

        if(updatedPin.isImageUploaded()){
            // Save the image localy

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            String folder = "C:\\Users\\Pc\\IntelliJIDEAProjects\\wp_mpip_project\\map_application_frontend\\public\\images\\";
            byte[] bytes = updatedPin.getImageFile();
            String filename = updatedPin.getImageName();
            Path path = Paths.get(folder + filename);
            Files.write(path, bytes);

            updatedPin.setImage("/images/"+filename);
        }

        Pin pin = new Pin();
        pin.setId(updatedPin.getId());
        pin.setName(updatedPin.getName());
        pin.setLatitude(updatedPin.getLatitude());
        pin.setLongitude(updatedPin.getLongitude());
        pin.setDescription(updatedPin.getDescription());
        pin.setCategory(Category.CHURCH);
        pin.setImage(updatedPin.getImage());
        pin.setMap(pin_map);
        return pinsJPARepository.save(pin);
    }

    @Override
    public void deletePinById(Integer pin_id) {
        pinsJPARepository.deleteById(pin_id);
    }

}
