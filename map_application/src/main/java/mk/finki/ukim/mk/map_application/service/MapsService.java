package mk.finki.ukim.mk.map_application.service;

import mk.finki.ukim.mk.map_application.model.Map;
import mk.finki.ukim.mk.map_application.model.android.MapAndroid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MapsService {
    List<Map> getAllMaps();

    List<Map> getAllMapsByOwnerId(Long id);

    Map getMapById(Integer map_id);

    void deleteMapById(Integer map_id);

    Map saveMap(Map newMap, MultipartFile imageFile) throws IOException;

    Map saveMapAndroid(MapAndroid newMap) throws IOException;

    List<Map> getAllPendingMaps();

    Map updateMap(Map map);

    Map updateMapAndroid(MapAndroid map) throws IOException;

    Map approveMap(int map_id, int decision);

    Page<Map> getExploreMaps(Pageable pageable);
}
