package finki.ukim.mk.map_application.asynctask;

import android.os.AsyncTask;
import android.util.Log;
import finki.ukim.mk.map_application.model.auth.AuthenticatedUser;
import finki.ukim.mk.map_application.model.auth.LoginRequest;
import finki.ukim.mk.map_application.model.auth.SignUpRequest;
import finki.ukim.mk.map_application.model.auth.SignUpRespond;
import finki.ukim.mk.map_application.network.RestApiClient;
import finki.ukim.mk.map_application.repository.AuthUserRepository;

import java.io.IOException;

public class RegisterUserAsyncTask extends AsyncTask<SignUpRequest, Void, AuthenticatedUser> {

    private AuthUserRepository authUserRepository;

    public RegisterUserAsyncTask(AuthUserRepository authUserRepository){
        this.authUserRepository = authUserRepository;
    }

    @Override
    protected AuthenticatedUser doInBackground(SignUpRequest... signUpRequests) {
        SignUpRespond signUpRespond = null;

        authUserRepository.deleteAll();

        try {
            signUpRespond = RestApiClient.getService().registerUser(signUpRequests[0]).execute().body();
            if(signUpRespond != null){
                if(signUpRespond.getSuccess().equals(true)){
                    LoginRequest loginRequest = new LoginRequest(signUpRequests[0].getUsername(),signUpRequests[0].getPassword());
                    return RestApiClient.getService().authenticateUser(loginRequest).execute().body();
                }
            }
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
