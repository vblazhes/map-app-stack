package mk.finki.ukim.mk.map_application.web;

import mk.finki.ukim.mk.map_application.model.Category;
import mk.finki.ukim.mk.map_application.model.Pin;
import mk.finki.ukim.mk.map_application.model.android.PinAndroid;
import mk.finki.ukim.mk.map_application.service.PinsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "/maps/{map_id}/pins", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
public class PinsController {
    private final PinsService pinService;

    private static final Logger LOG = LoggerFactory.getLogger(MapsController.class.getName());

    PinsController(PinsService pinService) {
        this.pinService = pinService;
    }

    @GetMapping
    public List<Pin> getAllMapPins(@PathVariable Integer map_id) {
        return pinService.getAllMapPins(map_id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pin createPin(@PathVariable Integer map_id,
                         @RequestParam("latitude") Double latitude,
                         @RequestParam("longitude") Double longitude,
                         @RequestParam("name") String name,
                         @RequestParam("description") String description,
                         @RequestParam("image") String image) {
        Category category = Category.CHURCH;
        return pinService.addPin(map_id, latitude, longitude, name, description, category, image);
    }

    @PostMapping(path = "/android")
    @ResponseStatus(HttpStatus.CREATED)
    public Pin createPinAndroid(@PathVariable int map_id, @RequestBody PinAndroid newPin) throws IOException {
//        Category category = Category.CHURCH;
        return pinService.savePinAndroid(map_id,newPin);
    }

    @DeleteMapping({"/{pin_id}"})
    public void deletePinById(@PathVariable Integer pin_id) {
        pinService.deletePinById(pin_id);
    }

    @PutMapping("/update")
    public Pin updatePin(@PathVariable int map_id, @RequestBody Pin updatedPin) {
        System.out.println("Pin Controller - Update");
        System.out.println(updatedPin.getId());
        return pinService.updatePin(updatedPin, map_id);
    }

    @PutMapping("/update/android")
    public Pin updatePinAndroid(@PathVariable int map_id, @RequestBody PinAndroid updatedPin) throws IOException {
        System.out.println("Pin Controller - Update");
        System.out.println(updatedPin.getId());
        return pinService.updatePinAndroid(updatedPin, map_id);
    }

}
