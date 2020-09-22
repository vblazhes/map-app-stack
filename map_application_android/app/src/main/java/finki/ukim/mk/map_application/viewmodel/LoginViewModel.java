package finki.ukim.mk.map_application.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import finki.ukim.mk.map_application.asynctask.AuthenticateUserAsyncTask;
import finki.ukim.mk.map_application.asynctask.RegisterUserAsyncTask;
import finki.ukim.mk.map_application.model.User;
import finki.ukim.mk.map_application.model.auth.LoginRequest;
import finki.ukim.mk.map_application.model.auth.AuthenticatedUser;
import finki.ukim.mk.map_application.model.auth.SignUpRequest;
import finki.ukim.mk.map_application.repository.AuthUserRepository;
import finki.ukim.mk.map_application.repository.UserRepository;

import java.util.concurrent.ExecutionException;

public class LoginViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private AuthUserRepository authUserRepository;

    public LoginViewModel(@NonNull Application application){
        super(application);

        userRepository = new UserRepository(application);
        authUserRepository = new AuthUserRepository(application);
    }


    public void insert(User user){
        userRepository.insert(user);
    }

    public void update(User user){
        userRepository.update(user);
    }

    public void delete(User user){
        userRepository.delete(user);
    }

    public void deleteAll(User user){
        userRepository.deleteAll();
    }

    public AuthenticatedUser authenticateUser(LoginRequest loginRequest) throws ExecutionException, InterruptedException {
       return new AuthenticateUserAsyncTask(authUserRepository).execute(loginRequest).get();
    }

    public AuthenticatedUser registerUser(SignUpRequest signUpRequest) throws ExecutionException, InterruptedException {
        return new RegisterUserAsyncTask(authUserRepository).execute(signUpRequest).get();
    }
}
