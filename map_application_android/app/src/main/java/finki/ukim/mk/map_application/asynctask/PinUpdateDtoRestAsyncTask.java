package finki.ukim.mk.map_application.asynctask;

import android.os.AsyncTask;
import finki.ukim.mk.map_application.model.Pin;
import finki.ukim.mk.map_application.model.PinDTO;
import finki.ukim.mk.map_application.network.RestApiClient;
import finki.ukim.mk.map_application.repository.PinRepository;
import retrofit2.Call;

import java.io.IOException;

public class PinUpdateDtoRestAsyncTask extends AsyncTask<PinDTO, Void, Pin> {
    private PinRepository pinRepository;

    public PinUpdateDtoRestAsyncTask(PinRepository pinRepository){
        this.pinRepository = pinRepository;
    }

    @Override
    protected Pin doInBackground(PinDTO... pinDTOs) {
        int map_id = pinDTOs[0].getMap_id();
        Pin pin = pinDTOs[0].getPin();
        pin.setMap_id(map_id);
        final Call<Pin> updatePin = RestApiClient.getService().updatePin(map_id,pin);

        try {
            Pin pin_updated = updatePin.execute().body();
            assert pin_updated != null;
            pin_updated.setMap_id(map_id);
            return pin_updated;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Pin pin) {
        super.onPostExecute(pin);
        pinRepository.update(pin);
    }
}
