package finki.ukim.mk.map_application.asynctask;

import android.os.AsyncTask;
import finki.ukim.mk.map_application.model.Map;
import finki.ukim.mk.map_application.network.RestApiClient;
import finki.ukim.mk.map_application.repository.MapRepository;
import retrofit2.Call;

import java.io.IOException;

public class MapUpdateRestAsyncTask extends AsyncTask<Map, Void, Map > {

    private MapRepository mapRepository;

    public MapUpdateRestAsyncTask(MapRepository mapRepository){
        this.mapRepository = mapRepository;
    }


    @Override
    protected Map doInBackground(Map... maps) {

        final Call<Map> updateMap = RestApiClient.getService().updateMap(maps[0]);

        try {
            return updateMap.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Map map) {
        super.onPostExecute(map);

        mapRepository.update(map);

    }
}
