package finki.ukim.mk.map_application.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import finki.ukim.mk.map_application.model.Map;
import finki.ukim.mk.map_application.model.MapInsertAsyncTaskDTO;
import finki.ukim.mk.map_application.model.Pin;
import finki.ukim.mk.map_application.model.auth.AuthenticatedUser;
import finki.ukim.mk.map_application.repository.AuthUserRepository;
import finki.ukim.mk.map_application.repository.MapRepository;
import finki.ukim.mk.map_application.repository.PinRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CreateNewMapViewModel extends AndroidViewModel {

    private MapRepository mapRepository;
    private PinRepository pinRepository;
    private AuthUserRepository authUserRepository;

    public CreateNewMapViewModel(@NonNull Application application) {
        super(application);
        mapRepository = new MapRepository(application);
        pinRepository = new PinRepository(application);
        authUserRepository = new AuthUserRepository(application);
    }

    public void insert(Map map, List<Pin> pins, AuthenticatedUser authenticatedUser) {
        MapInsertAsyncTaskDTO map_pins_dto = new MapInsertAsyncTaskDTO(map,pins, authenticatedUser);
        mapRepository.insertMapWithPins(map_pins_dto);
    }

    public AuthenticatedUser getAuthUser() throws ExecutionException, InterruptedException {
        return authUserRepository.getAuthUser();
    }

}
