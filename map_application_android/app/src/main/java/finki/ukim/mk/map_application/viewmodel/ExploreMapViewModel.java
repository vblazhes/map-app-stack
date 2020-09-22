package finki.ukim.mk.map_application.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import finki.ukim.mk.map_application.model.Map;
import finki.ukim.mk.map_application.repository.MapRepository;

import java.util.List;

public class ExploreMapViewModel extends AndroidViewModel {

    private MapRepository repository;
//    private LiveData<List<Map>> allMaps;

    public ExploreMapViewModel(@NonNull Application application) {
        super(application);

        repository = new MapRepository(application);
//        allMaps = getAllMaps();
    }

    public void insert(Map map){
        repository.insert(map);
    }

    public void update(Map map){
        repository.update(map);
    }

    public void delete(Map map){
        repository.delete(map);
    }

    public void deleteAll(){
        repository.deleteAll();
    }

    public LiveData<List<Map>> getAllMaps(){
        return repository.getAllMaps();
    }

    public LiveData<List<Map>> getAllApprovedMaps(){
        return repository.getApprovedMaps();
    }


}
