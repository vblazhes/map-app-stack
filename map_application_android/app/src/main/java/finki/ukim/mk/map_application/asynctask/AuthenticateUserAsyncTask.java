package finki.ukim.mk.map_application.asynctask;

import android.os.AsyncTask;
import finki.ukim.mk.map_application.model.User;
import finki.ukim.mk.map_application.model.auth.LoginRequest;
import finki.ukim.mk.map_application.model.auth.AuthenticatedUser;
import finki.ukim.mk.map_application.network.RestApiClient;
import finki.ukim.mk.map_application.repository.AuthUserRepository;
import finki.ukim.mk.map_application.repository.UserRepository;

import java.io.IOException;

public class AuthenticateUserAsyncTask extends AsyncTask<LoginRequest, Void, AuthenticatedUser> {

    private AuthUserRepository authUserRepository;

    public AuthenticateUserAsyncTask(AuthUserRepository authUserRepository){
        this.authUserRepository = authUserRepository;
    }

    @Override
    protected AuthenticatedUser doInBackground(LoginRequest... loginRequests) {
        User user;

        authUserRepository.deleteAll();

        try {
            return  RestApiClient.getService().authenticateUser(loginRequests[0]).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(AuthenticatedUser authenticatedUser) {
        super.onPostExecute(authenticatedUser);
        authUserRepository.insert(authenticatedUser);
    }
}
