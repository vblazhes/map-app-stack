package finki.ukim.mk.map_application.asynctask;

import android.os.AsyncTask;
import finki.ukim.mk.map_application.database.dao.MapDao;
import finki.ukim.mk.map_application.model.Map;
import finki.ukim.mk.map_application.network.RestApiClient;
import finki.ukim.mk.map_application.repository.MapRepository;
import retrofit2.Call;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class MapLoadAsyncTask extends AsyncTask<Void, Void, List<Map>> {

    private MapDao mapDao;
    private MapRepository repository;
    private final int REFRESH_TIMEOUT = 5 * 60;

    public MapLoadAsyncTask(MapRepository mapRepository) {
        this.repository = mapRepository;
        this.mapDao = mapRepository.getMapDao();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        repository.deleteAll();
    }

    @Override
    protected List<Map> doInBackground(Void... voids) {

        Date current_time = new Date();
        if((mapDao.hasEntries() == 0) || (current_time.getTime() - mapDao.lastUpdated()/1000 > REFRESH_TIMEOUT)){
            final Call<List<Map>> maps = RestApiClient.getService().getAllMaps();

            try {
                return maps.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Map> maps) {
        super.onPostExecute(maps);

        if(maps != null) {
            repository.deleteAll();

            Date current_time = new Date();
            for (Map map : maps) {
                repository.insert(map);
                repository.updateLoadTimeOfRecords();
            }
        }

    }
}
