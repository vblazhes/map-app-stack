package finki.ukim.mk.map_application.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import finki.ukim.mk.map_application.model.auth.AuthenticatedUser;
import finki.ukim.mk.map_application.repository.AuthUserRepository;

import java.util.concurrent.ExecutionException;

public class MainActivityViewModel extends AndroidViewModel {

    private AuthUserRepository authUserRepository;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        authUserRepository = new AuthUserRepository(application);
    }

    public void insert(AuthenticatedUser authenticatedUser){
        authUserRepository.insert(authenticatedUser);
    }

    public AuthenticatedUser getAuthUser() throws ExecutionException, InterruptedException {
        return authUserRepository.getAuthUser();
    }

    public void deleteAllAuthUsers(){
        authUserRepository.deleteAll();
    }
}
