package finki.ukim.mk.map_application.database;


import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import finki.ukim.mk.map_application.database.dao.AuthenticatedUserDao;
import finki.ukim.mk.map_application.database.dao.MapDao;
import finki.ukim.mk.map_application.database.dao.PinDao;
import finki.ukim.mk.map_application.database.dao.UserDao;
import finki.ukim.mk.map_application.model.*;
import finki.ukim.mk.map_application.model.auth.AuthenticatedUser;


//MNC Singleton pattern
@Database(entities = {Map.class, Role.class, User.class, Pin.class, AuthenticatedUser.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase db_instance;

    public abstract MapDao mapDao();

    public abstract PinDao pinDao();

    public abstract UserDao userDao();

    public abstract AuthenticatedUserDao authUserDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (db_instance == null) {
            db_instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class,"app_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(deleteAuthUserCallback)
                    .build();
        }
        System.out.println("DB created!!");
        return db_instance;
    }

    //MNC
    /**Populate DB with dummy data for startup test only
    Callback - a function that is passed inside other function as an argument,
    which is invoked inside the outher function to complete some kind of ROUTINE or ACTION**/
    private static RoomDatabase.Callback populateDummyCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            new PopulateDBWithDummyAsyncTask(db_instance).execute();
            System.out.println("MAPS ADDED");
        }
    };

    private static RoomDatabase.Callback deleteAuthUserCallback = new RoomDatabase.Callback(){
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            new DeleteAllDBAuthUsersAsyncTask(db_instance).execute();
            System.out.println("AuthUser dummy added");
        }
    };


    private static class PopulateDBWithDummyAsyncTask extends AsyncTask<Void,Void,Void>{
        private MapDao mapDao;

        private  PopulateDBWithDummyAsyncTask(AppDatabase database){
            mapDao = database.mapDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mapDao.insertMap(new Map(10,10,41.123096, 20.801647,"mapbox://styles/vblazhes/ck62e0e7k0z1p1in0fy9cb2ke","Map of Schools in Ohrid ","https://img.theculturetrip.com/wp-content/uploads/2018/06/shutterstock_319995938.jpg","Description for Ohrid Church Map", Visibility.PUBLIC,1,0,new User(1L,"Vladimir Blazheski", "vblazhes", "vladimirblazeski@yahoo.com", "$2a$10$wWyYbomFlq8D4B2aT4AtZuChfXY8dxLJH7UwIfh4SwZMTTjXG.CIW",new Role(1L,RoleName.ROLE_USER))));
            mapDao.insertMap(new Map(20,10,41.123096, 20.801647,"mapbox://styles/vblazhes/ck6b9ro1u18ug1ikvbju56691","Map of Beaches in Ohrid","https://lh5.googleusercontent.com/p/AF1QipM8LK82bOd3WAZZef2IEhRNMOWuyBg8sjObgkD5=w493-h240-k-no","Description for Ohrid Hotel Map",Visibility.PUBLIC,1,0,new User(2L,"Admin", "admin", "admin.admin@yahoo.com", "$2a$10$wWyYbomFlq8D4B2aT4AtZuChfXY8dxLJH7UwIfh4SwZMTTjXG.CIW",new Role(2L, RoleName.ROLE_ADMIN))));
            System.out.println("MAPS ADDED");
            return null;
        }
    }

    private static class DeleteAllDBAuthUsersAsyncTask extends AsyncTask<Void,Void,Void>{
        private AuthenticatedUserDao authUserDao;

        private  DeleteAllDBAuthUsersAsyncTask(AppDatabase database){
            authUserDao = database.authUserDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            authUserDao.deleteAllAuthUsers();
            System.out.println("AuthUsers Deleted");
            return null;
        }
    }
}
