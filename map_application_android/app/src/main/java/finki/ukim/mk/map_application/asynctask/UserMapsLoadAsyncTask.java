package finki.ukim.mk.map_application.asynctask;

import android.os.AsyncTask;
import finki.ukim.mk.map_application.model.Map;
import finki.ukim.mk.map_application.network.RestApiClient;
import finki.ukim.mk.map_application.repository.MapRepository;
import retrofit2.Call;

import java.io.IOException;
import java.util.List;

public class UserMapsLoadAsyncTask extends AsyncTask<Integer, Void, List<Map>> {
    private MapRepository repository;

    public UserMapsLoadAsyncTask(MapRepository mapRepository) {
        this.repository = mapRepository;
    }

    @Override
    protected List<Map> doInBackground(Integer... integers) {
        final Call<List<Map>> userMaps = RestApiClient.getService().getAllUserMaps(integers[0]);
        try {
            return userMaps.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Map> maps) {
        super.onPostExecute(maps);

        if (maps != null) {
            for (Map map : maps) {
                repository.insert(map);
            }
        }
    }
}
