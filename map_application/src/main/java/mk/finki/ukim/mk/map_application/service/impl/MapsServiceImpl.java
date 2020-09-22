package mk.finki.ukim.mk.map_application.service.impl;

import mk.finki.ukim.mk.map_application.model.Auth.User;
import mk.finki.ukim.mk.map_application.model.Exception.Auth.UserNotFoundException;
import mk.finki.ukim.mk.map_application.model.Map;
import mk.finki.ukim.mk.map_application.model.Exception.MapNotFoundException;
import mk.finki.ukim.mk.map_application.model.android.MapAndroid;
import mk.finki.ukim.mk.map_application.repository.Auth.UserRepository;
import mk.finki.ukim.mk.map_application.repository.MapsJPARepository;
import mk.finki.ukim.mk.map_application.service.MapsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class MapsServiceImpl implements MapsService {

    private final MapsJPARepository mapsRepository;
    private final UserRepository userRepository;

    MapsServiceImpl(MapsJPARepository mapsJPARepository, UserRepository userRepository){
        this.mapsRepository = mapsJPARepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Map> getAllMaps() {
        return mapsRepository.findAll();
    }

    @Override
    public List<Map> getAllMapsByOwnerId(Long id) {
        return mapsRepository.findByOwnerId(id);
    }

    @Override
    public Map getMapById(Integer map_id) {
        return mapsRepository.findById(map_id).orElseThrow(MapNotFoundException::new);    }

    @Override
    public void deleteMapById(Integer map_id) {
         mapsRepository.deleteById(map_id);
    }

    @Override
    public Map saveMap(Map newMap, MultipartFile imageFile) throws IOException {

        User user = userRepository.findByUsername(newMap.getOwner().getUsername()).orElseThrow(UserNotFoundException::new);
        newMap.setOwner(user);
        if(imageFile!=null && !imageFile.isEmpty()){
            // Save the image localy
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            String folder = "C:\\Users\\Pc\\IntelliJIDEAProjects\\wp_mpip_project\\map_application_frontend\\public\\images\\";
            byte[] bytes = imageFile.getBytes();
            String filename = imageFile.getOriginalFilename();
            Path path = Paths.get(folder + filename);
            Files.write(path, bytes);

            newMap.setBackground("/images/"+filename);
            newMap.setImageFile(1);
        }else{
            newMap.setImageFile(0);
        }

        return mapsRepository.save(newMap);
    }

    @Override
    public Map saveMapAndroid(MapAndroid newMap) throws IOException {
        Map map = new Map();

        User user = userRepository.findByUsername(newMap.getOwner().getUsername()).orElseThrow(UserNotFoundException::new);
        map.setOwner(user);

        if(newMap.getImageFile() == 1){
            // Save the image localy
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            String folder = "C:\\Users\\Pc\\IntelliJIDEAProjects\\wp_mpip_project\\map_application_frontend\\public\\images\\";
            byte[] bytes = newMap.getImageBytes();
            String filename = newMap.getImageName();
            Path path = Paths.get(folder + filename);
            Files.write(path, bytes);

            newMap.setBackground("/images/"+filename);
            newMap.setImageFile(1);
        }else{
            newMap.setImageFile(0);
        }


        map.setDefault_zoom(newMap.getDefault_zoom());
        map.setCenter_latitude(newMap.getCenter_latitude());
        map.setCenter_longitude(newMap.getCenter_longitude());
        map.setStyle(newMap.getStyle());
        map.setName(newMap.getName());
        map.setDescription(newMap.getDescription());
        map.setVisibility(newMap.getVisibility());
        map.setApproved(newMap.getApproved());
        map.setBackground(newMap.getBackground());
        map.setImageFile(newMap.getImageFile());

        return mapsRepository.save(map);    }

    @Override
    public List<Map> getAllPendingMaps() {
        return mapsRepository.findAllPending();
    }

    @Override
    public Map updateMap(Map updatedMap) {
        User user = userRepository.findByUsername(updatedMap.getOwner().getUsername()).orElseThrow(UserNotFoundException::new);
        updatedMap.setOwner(user);

        if(updatedMap.getBackground() == null || updatedMap.getBackground().equals("")){
            Map map = mapsRepository.findById(updatedMap.getId()).orElseThrow(MapNotFoundException::new);
            updatedMap.setBackground(map.getBackground());
        }

        return mapsRepository.save(updatedMap);
    }

    @Override
    public Map updateMapAndroid(MapAndroid updatedMap) throws IOException {
        User user = userRepository.findByUsername(updatedMap.getOwner().getUsername()).orElseThrow(UserNotFoundException::new);
        Map map = new Map();
        map.setOwner(user);

        if(updatedMap.getImageFile() == 1){
            // Save the image localy
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            String folder = "C:\\Users\\Pc\\IntelliJIDEAProjects\\wp_mpip_project\\map_application_frontend\\public\\images\\";
            byte[] bytes = updatedMap.getImageBytes();
            String filename = updatedMap.getImageName();
            Path path = Paths.get(folder + filename);
            Files.write(path, bytes);

            updatedMap.setBackground("/images/"+filename);
            updatedMap.setImageFile(1);
        }else{
            updatedMap.setImageFile(0);
        }

        map.setId(updatedMap.getId());
        map.setDefault_zoom(updatedMap.getDefault_zoom());
        map.setCenter_latitude(updatedMap.getCenter_latitude());
        map.setCenter_longitude(updatedMap.getCenter_longitude());
        map.setStyle(updatedMap.getStyle());
        map.setName(updatedMap.getName());
        map.setBackground(updatedMap.getBackground());
        map.setDescription(updatedMap.getDescription());
        map.setVisibility(updatedMap.getVisibility());
        map.setApproved(updatedMap.getApproved());
        map.setImageFile(updatedMap.getImageFile());

        return mapsRepository.save(map);
    }

    @Override
    public Map approveMap(int map_id, int decision) {
        Map map_to_approve = mapsRepository.findById(map_id).orElseThrow(MapNotFoundException::new);
        map_to_approve.setApproved(decision);
        return mapsRepository.save(map_to_approve);
    }

    @Override
    public Page<Map> getExploreMaps(Pageable pageable) {
        return mapsRepository.findAll(pageable);
    }

}
