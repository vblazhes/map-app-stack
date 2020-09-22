package finki.ukim.mk.map_application.asynctask;

import android.os.AsyncTask;
import finki.ukim.mk.map_application.model.Map;
import finki.ukim.mk.map_application.network.RestApiClient;
import finki.ukim.mk.map_application.repository.MapRepository;
import retrofit2.Call;

import java.io.IOException;

public class MapDeleteRestAsyncTask extends AsyncTask<Map, Void, Map> {

    private MapRepository mapRepository;

    public MapDeleteRestAsyncTask(MapRepository mapRepository){
        this.mapRepository = mapRepository;
    }


    @Override
    protected Map doInBackground(Map... maps) {
        final Call<Void> deleteMap = RestApiClient.getService().deleteMap(maps[0].getId());
        try {
            deleteMap.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return maps[0];
    }

    @Override
    protected void onPostExecute(Map map) {
        super.onPostExecute(map);
        mapRepository.delete(map);
    }
}
