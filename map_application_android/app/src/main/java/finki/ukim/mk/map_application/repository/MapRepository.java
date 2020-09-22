package finki.ukim.mk.map_application.repository;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import finki.ukim.mk.map_application.asynctask.*;
import finki.ukim.mk.map_application.database.AppDatabase;
import finki.ukim.mk.map_application.database.dao.MapDao;
import finki.ukim.mk.map_application.model.Map;
import finki.ukim.mk.map_application.model.MapInsertAsyncTaskDTO;
import finki.ukim.mk.map_application.model.User;
import finki.ukim.mk.map_application.model.Visibility;
import finki.ukim.mk.map_application.network.RestApiClient;
import retrofit2.Call;

import java.io.IOException;
import java.util.Date;
import java.util.List;


public class MapRepository {
    private MapDao mapDao;
    private PinRepository pinRepository;
    private Context context;

    public MapRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        pinRepository = new PinRepository(application);
        mapDao = database.mapDao();
        context = application.getApplicationContext();
    }

    public MapDao getMapDao() {
        return mapDao;
    }

    public Context getContext(){
        return this.context;
    }

    public PinRepository getPinRepository() {
        return pinRepository;
    }

    public void insert(Map map) {
        new InsertMapAsyncTask(mapDao).execute(map);
    }

    public void insertMapWithPins(MapInsertAsyncTaskDTO mapDto) {
        new MapInsertDtoRestAsyncTask(this).execute(mapDto);
    }

    public void update(Map map) {
        new UpdateMapAsyncTask(mapDao).execute(map);
    }

    public void updateMapRest(Map map){
        new MapUpdateRestAsyncTask(this).execute(map);
    }

    public void delete(Map map) {
        new DeleteMapAsyncTask(mapDao).execute(map);
    }

    public void deleteAll() {
        new DeleteAllMapsAsyncTask(mapDao).execute();
    }

    /**
     * DB operations that return LiveData are by default executed on background thread
     * So we do not need to write a async task for this
     **/
    public LiveData<List<Map>> getAllMaps() {
        new MapLoadAsyncTask(this).execute();
        return mapDao.getAllMaps();
    }

    public LiveData<List<Map>> getApprovedMaps(){
        new MapLoadAsyncTask(this).execute();
        return mapDao.getApprovedMaps();
    }

//    public LiveData<List<Map>> getAllAdminApprovedMaps(){
//        new ApprovedMapsLoadAsyncTask(this).execute();
//        return mapDao.getApprovedMaps(Visibility.PUBLIC);
//    }

    public LiveData<List<Map>> getAllUserMaps(User user) {
        new UserMapsLoadAsyncTask(this).execute(user.getId().intValue());
        return mapDao.getAllUsersMaps(user.getUsername());
    }

    public LiveData<Map> getMapById(int id) {
        return mapDao.getMapById(id);
    }

    public void updateLoadTimeOfRecords() {
        new UpdateLoadTimeOfRecordsAsyncTask(mapDao).execute();
    }


    //MNC

    /**
     * DB Operations are not allowed to be executed on the main thread it could freeze our app or potentially crash
     * We will use an async task in order to execute this DB operations on Background thread
     * Async task it has to be static so it doesnt have ref to the repository itself
     * otherwise this could cause memory leak
     **/

    private static class InsertMapAsyncTask extends AsyncTask<Map, Void, Void> {

        private MapDao mapDao;

        private InsertMapAsyncTask(MapDao mapDao) {
            this.mapDao = mapDao;
        }

        @Override
        protected Void doInBackground(Map... maps) {
            mapDao.insertMap(maps[0]);
            return null;
        }
    }


    private static class UpdateMapAsyncTask extends AsyncTask<Map, Void, Void> {
        private MapDao mapDao;

        private UpdateMapAsyncTask(MapDao mapDao) {
            this.mapDao = mapDao;
        }

        @Override
        protected Void doInBackground(Map... maps) {
            mapDao.updateMap(maps[0]);
            return null;
        }
    }

    private static class DeleteMapAsyncTask extends AsyncTask<Map, Void, Void> {
        private MapDao mapDao;

        private DeleteMapAsyncTask(MapDao mapDao) {
            this.mapDao = mapDao;
        }

        @Override
        protected Void doInBackground(Map... maps) {
            final Call<Void> deleteMap = RestApiClient.getService().deleteMap(maps[0].getId());
            try {
                deleteMap.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mapDao.deleteMap(maps[0]);
            return null;
        }
    }

    private static class DeleteAllMapsAsyncTask extends AsyncTask<Void, Void, Void> {
        private MapDao mapDao;

        private DeleteAllMapsAsyncTask(MapDao mapDao) {
            this.mapDao = mapDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mapDao.deleteAllMaps();
            return null;
        }
    }

    private static class UpdateLoadTimeOfRecordsAsyncTask extends AsyncTask<Void, Void, Void> {

        private MapDao mapDao;

        private UpdateLoadTimeOfRecordsAsyncTask(MapDao mapDao) {
            this.mapDao = mapDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mapDao.updateLoadTimeOfRecords(new Date().getTime());
            return null;
        }
    }

}
