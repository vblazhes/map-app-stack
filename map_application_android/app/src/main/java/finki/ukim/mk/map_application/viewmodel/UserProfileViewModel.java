package finki.ukim.mk.map_application.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import finki.ukim.mk.map_application.model.Map;
import finki.ukim.mk.map_application.model.User;
import finki.ukim.mk.map_application.model.auth.AuthenticatedUser;
import finki.ukim.mk.map_application.repository.AuthUserRepository;
import finki.ukim.mk.map_application.repository.MapRepository;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserProfileViewModel extends AndroidViewModel {

    private AuthUserRepository authUserRepository;
    private MapRepository mapRepository;

    public UserProfileViewModel(@NonNull Application application) {
        super(application);

        this.authUserRepository = new AuthUserRepository(application);
        this.mapRepository = new MapRepository(application);
    }

    public LiveData<List<Map>> getUserMaps(User user){
        return mapRepository.getAllUserMaps(user);
    }

    public AuthenticatedUser getAuthUser() throws ExecutionException, InterruptedException {
        return authUserRepository.getAuthUser();
    }

    public void deleteAllAuthUsers(){
        authUserRepository.deleteAll();
    }

    public void deleteMap(Map map){
        mapRepository.delete(map);
    }
}
