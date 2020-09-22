package finki.ukim.mk.map_application.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import finki.ukim.mk.map_application.model.Pin;
import finki.ukim.mk.map_application.repository.PinRepository;

import java.util.List;

public class PinsListViewModel  extends AndroidViewModel {

    private PinRepository pinRepository;

    public PinsListViewModel(@NonNull Application application){
        super(application);
        pinRepository = new PinRepository(application);
    }

    public LiveData<List<Pin>> getMapPins(int map_id){
       return pinRepository.getAllMapPins(map_id);
    }
}
