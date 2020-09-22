package finki.ukim.mk.map_application.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import finki.ukim.mk.map_application.database.AppDatabase;
import finki.ukim.mk.map_application.database.dao.AuthenticatedUserDao;
import finki.ukim.mk.map_application.model.auth.AuthenticatedUser;

import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;

public class AuthUserRepository {

    private AuthenticatedUserDao authUserDao;

    public AuthUserRepository(Application application){
        AppDatabase database = AppDatabase.getInstance(application);
        this.authUserDao = database.authUserDao();
    }

    public void insert(AuthenticatedUser authUser){
        new InsertAuthUserAsyncTask(authUserDao).execute(authUser);
    }

    public void update(AuthenticatedUser authUser){
        new UpdateAuthUserAsyncTask(authUserDao).execute(authUser);
    }

    public void delete(AuthenticatedUser authUser){
        new DeleteAuthUserAsyncTask(authUserDao).execute(authUser);
    }

    public void deleteAll(){
        new DeleteAllAuthUsersAsyncTask(authUserDao).execute();
    }

    public AuthenticatedUser getAuthUser() throws ExecutionException, InterruptedException {
       return new GetAuthUserAsyncTask(authUserDao).execute().get();
    }


    public static class InsertAuthUserAsyncTask extends AsyncTask<AuthenticatedUser, Void, Void>{
        private AuthenticatedUserDao authenticatedUserDao;

        private InsertAuthUserAsyncTask(AuthenticatedUserDao authenticatedUserDao){
            this.authenticatedUserDao = authenticatedUserDao;
        }


        @Override
        protected Void doInBackground(AuthenticatedUser... authenticatedUsers) {
            try {
                authenticatedUserDao.insertAuthUser(authenticatedUsers[0]);
            }catch (NullPointerException exception){
                Log.e(TAG, "doInBackground: Authentication Failed - Invalid username or password");
            }
            return null;
        }
    }

    public static class UpdateAuthUserAsyncTask extends AsyncTask<AuthenticatedUser, Void, Void>{
        private AuthenticatedUserDao authenticatedUserDao;

        private UpdateAuthUserAsyncTask(AuthenticatedUserDao authenticatedUserDao){
            this.authenticatedUserDao = authenticatedUserDao;
        }


        @Override
        protected Void doInBackground(AuthenticatedUser... authenticatedUsers) {
            authenticatedUserDao.updateAuthUser(authenticatedUsers[0]);
            return null;
        }
    }


    public static class DeleteAuthUserAsyncTask extends AsyncTask<AuthenticatedUser, Void, Void>{
        private AuthenticatedUserDao authenticatedUserDao;

        private DeleteAuthUserAsyncTask(AuthenticatedUserDao authenticatedUserDao){
            this.authenticatedUserDao = authenticatedUserDao;
        }


        @Override
        protected Void doInBackground(AuthenticatedUser... authenticatedUsers) {
            authenticatedUserDao.deleteAuthUser(authenticatedUsers[0]);
            return null;
        }
    }

    public static class DeleteAllAuthUsersAsyncTask extends AsyncTask<Void,Void,Void>{
        private AuthenticatedUserDao authenticatedUserDao;

        private DeleteAllAuthUsersAsyncTask(AuthenticatedUserDao authenticatedUserDao){
            this.authenticatedUserDao = authenticatedUserDao;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            authenticatedUserDao.deleteAllAuthUsers();
            return null;
        }
    }


    private static class GetAuthUserAsyncTask extends AsyncTask<Void,Void,AuthenticatedUser>{
        private AuthenticatedUserDao authenticatedUserDao;

        private GetAuthUserAsyncTask(AuthenticatedUserDao authenticatedUserDao){
            this.authenticatedUserDao = authenticatedUserDao;
        }


        @Override
        protected AuthenticatedUser doInBackground(Void... voids) {
            return authenticatedUserDao.getAuthenticatedUser();
        }
    }

}
