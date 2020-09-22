package mk.finki.ukim.mk.map_application.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import mk.finki.ukim.mk.map_application.model.Auth.User;
import mk.finki.ukim.mk.map_application.model.Exception.Auth.UserNotFoundException;
import mk.finki.ukim.mk.map_application.model.Map;
import mk.finki.ukim.mk.map_application.model.android.MapAndroid;
import mk.finki.ukim.mk.map_application.model.Visibility;
import mk.finki.ukim.mk.map_application.repository.Auth.UserRepository;
import mk.finki.ukim.mk.map_application.service.MapsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "/maps", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
public class MapsController {

    private static final Logger LOG = LoggerFactory.getLogger(MapsController.class.getName());

    private final MapsService mapsService;
    private final UserRepository userRepository;

    MapsController(MapsService mapsService, UserRepository userRepository) {
        this.mapsService = mapsService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Map> getAllMaps() {
        LOG.info("METHOD NAME: getAllMaps - GET request" + new Date() );

        return mapsService.getAllMaps();
    }

//    @GetMapping
//    public Page<Map> getAllIngredients(@PageableDefault(value = 5) Pageable pageable){
//        return mapsService.getExploreMaps(pageable);
//    }


    @GetMapping("/{map_id}")
    public Map getMapById(@PathVariable Integer map_id) {
        return mapsService.getMapById(map_id);
    }

    @GetMapping("/owner/{owner_id}")
    public List<Map> getMapsByOwnerId(@PathVariable Long owner_id) {
        LOG.info("METHOD NAME: getMapByOwnerId - GET request" + new Date() );

        return mapsService.getAllMapsByOwnerId(owner_id);
    }

    @DeleteMapping("/{map_id}")
    public void deleteMap(@PathVariable Integer map_id) {
        LOG.info("METHOD NAME: deleteMap - DELETE request" + new Date() );

        mapsService.deleteMapById(map_id);
    }

    @PostMapping(consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.CREATED)
    public Map createMap(@RequestParam double default_zoom, @RequestParam double center_latitude,
                         @RequestParam double center_longitude, @RequestParam String style,
                         @RequestParam String name, @RequestParam String background, @RequestParam String description,
                         @RequestParam Visibility visibility, @RequestParam int approved,
                         @RequestParam(required = false) MultipartFile imageFile, @RequestParam(required = false) String owner) throws IOException {
        System.out.println("Post map controller");

        LOG.info("METHOD NAME: createMap - POST request" + new Date() );

        User owner_obj;

        if(owner == null || owner.equals("")){
            owner_obj = userRepository.findByUsername("vblazhes").orElseThrow(UserNotFoundException::new);
        }else {
            owner_obj = new ObjectMapper().readValue(owner,User.class);
//            owner_obj = userRepository.findByUsername(owner).orElseThrow(UserNotFoundException::new);
        }

        Map newMap = new Map();
        newMap.setDefault_zoom((int) default_zoom);
        newMap.setCenter_latitude(center_latitude);
        newMap.setCenter_longitude(center_longitude);
        newMap.setStyle(style);
        newMap.setName(name);
        newMap.setBackground(background);
        newMap.setVisibility(visibility);
        newMap.setDescription(description);
        newMap.setApproved(approved);
        //newMap.setImageFile(imageFile);
        newMap.setOwner(owner_obj);

        if(imageFile!=null && !imageFile.isEmpty()){
            System.out.println("Successful upload!");
            System.out.println(imageFile.getOriginalFilename());
        }else {
            System.out.println("empty file");
        }
        //return new Map();
        return mapsService.saveMap(newMap, imageFile);
        //return null;
    }

    @PostMapping(path = "/android")
    @ResponseStatus(HttpStatus.CREATED)
    public Map createMapAndroid(@RequestBody MapAndroid newMap) throws IOException {
        System.out.println("Post map controller");

        LOG.info("METHOD NAME: createMap - POST request" + new Date() );

        if(newMap.getImageFile() == 1){
            System.out.println("SUCCESS!! IMAGE TRANSFERRED");
        }


        //return new Map();
        return mapsService.saveMapAndroid(newMap);
        //return null;
    }

    @GetMapping("/pending")
    public List<Map> getAllPendingMaps() {
        LOG.info("METHOD NAME: createMap - GET request , USER: Admin" + new Date() );
        return mapsService.getAllPendingMaps();
    }

    @PutMapping("/update")
    public Map updateMap(@RequestBody Map updatedMap) {
//        System.out.println("Map Controller - Update");
//        System.out.println(updatedMap.getId());

        LOG.info("METHOD NAME: updateMap - PUT request , USER: "+ updatedMap.getOwner().getUsername()+" " + new Date() );

        return mapsService.updateMap(updatedMap);
    }

    @PutMapping("/update/android")
    public Map updateMapAndroid(@RequestBody MapAndroid updatedMap) throws IOException {

        LOG.info("METHOD NAME: updateMap - PUT request , USER: "+ updatedMap.getOwner().getUsername()+" " + new Date() );

        return mapsService.updateMapAndroid(updatedMap);
    }

    @PatchMapping("/{map_id}/admin-approval")
    public Map mapApproval(@PathVariable int map_id, @RequestParam("decision") int decision) {
        LOG.info("METHOD NAME: mapApproval - PATCH request , USER: Admin" + new Date());
        return mapsService.approveMap(map_id, decision);
    }
}
