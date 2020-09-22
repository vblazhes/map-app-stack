package finki.ukim.mk.map_application.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import finki.ukim.mk.map_application.model.Map;
import finki.ukim.mk.map_application.model.Pin;
import finki.ukim.mk.map_application.model.PinDTO;
import finki.ukim.mk.map_application.model.auth.AuthenticatedUser;
import finki.ukim.mk.map_application.repository.AuthUserRepository;
import finki.ukim.mk.map_application.repository.MapRepository;
import finki.ukim.mk.map_application.repository.PinRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class EditMapViewModel extends AndroidViewModel {

    private MapRepository mapRepository;
    private PinRepository pinRepository;
    private AuthUserRepository authUserRepository;
    private LiveData<List<Pin>> mapPins;
    private int mapId;


    public EditMapViewModel(@NonNull Application application, int mapId) {
        super(application);

        this.mapRepository = new MapRepository(application);
        this.pinRepository = new PinRepository(application);
        this.authUserRepository = new AuthUserRepository(application);
        this.mapId = mapId;

        mapPins = pinRepository.getAllMapPins(this.mapId);

    }

    public LiveData<List<Pin>>  getAllMapPins(){
        return mapPins;
    }

//    public LiveData<List<Pin>> getAllMapPins(int map_id){
//        return pinRepository.getAllMapPins(map_id);
//    }

    public void insertPin(PinDTO pinDto){
        pinRepository.insertPinRest(pinDto);
    }

    public void updatePin(PinDTO pinDto){
        pinRepository.updatePinRest(pinDto);
    }

    public void deletePin(Pin pin){
        pinRepository.deletePinRest(pin);
    }

    public void updateMap(Map map){
        mapRepository.updateMapRest(map);
    }

    public AuthenticatedUser getAuthUser() throws ExecutionException, InterruptedException {
        return authUserRepository.getAuthUser();
    }

}
