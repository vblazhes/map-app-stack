package finki.ukim.mk.map_application.asynctask;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import finki.ukim.mk.map_application.database.dao.PinDao;
import finki.ukim.mk.map_application.model.Map;
import finki.ukim.mk.map_application.model.Pin;
import finki.ukim.mk.map_application.network.RestApiClient;
import finki.ukim.mk.map_application.repository.PinRepository;
import retrofit2.Call;

import java.io.IOException;
import java.util.List;

import static android.content.ContentValues.TAG;

public class PinLoadAsyncTask extends AsyncTask<Integer ,Void, List<Pin>> {
    private PinDao pinDao;
    private PinRepository pinRepository;

    public PinLoadAsyncTask(PinRepository repository){
        this.pinRepository = repository;
    }


    //MNC
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        System.out.println("PINS DELETED");
//        pinRepository.deleteAll();
    }

    @Override
    protected List<Pin> doInBackground(Integer... integers) {

        final Call<List<Pin>> pins = RestApiClient.getService().getAllMapPins(integers[0]);

        try {
            List<Pin> fetchedPins =  pins.execute().body();
            assert fetchedPins != null;
            for(Pin pin : fetchedPins){
                pin.setMap_id(integers[0]);
            }
            return fetchedPins;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Pin> pins) {
        super.onPostExecute(pins);

        if(pins != null) {
            if(pins.size() > 0) {
                pinRepository.deleteAllByMapID(pins.get(0).getMap_id());
            }

            for (Pin pin : pins) {
//                System.out.println("PIN ID: " + pin.getId());
                Log.d(TAG, "onPostExecute: - Load Pin - pin_id:"+pin.getId());
                pinRepository.insert(pin);
            }
        }

//        for (Pin pin : pins) {
//            System.out.println("PIN ID: " + pin.getId());
//            pinRepository.insert(pin);
//        }
    }
}
