package finki.ukim.mk.map_application.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import finki.ukim.mk.map_application.model.Map;
import finki.ukim.mk.map_application.model.Pin;
import finki.ukim.mk.map_application.repository.MapRepository;
import finki.ukim.mk.map_application.repository.PinRepository;

import java.util.List;

public class ViewMapViewModel extends AndroidViewModel {

    private MapRepository mapRepository;
    private PinRepository pinRepository;
    private LiveData<Map> map;
    private LiveData<List<Pin>> pins;

    public ViewMapViewModel(@NonNull Application application){
        super(application);

        System.out.println("VIEW MODEL CONSTRUCTOR");
        mapRepository = new MapRepository(application);
        pinRepository = new PinRepository(application);

    }

    public LiveData<List<Pin>> getAllMapPins(int map_id){
        return pinRepository.getAllMapPins(map_id);
    }

    public LiveData<Map> getMapById(int map_id){
        return mapRepository.getMapById(map_id);
    }
}
