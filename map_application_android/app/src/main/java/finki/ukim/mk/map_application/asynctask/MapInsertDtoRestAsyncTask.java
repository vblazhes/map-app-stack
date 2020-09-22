package finki.ukim.mk.map_application.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.room.util.FileUtil;
import finki.ukim.mk.map_application.database.dao.MapDao;
import finki.ukim.mk.map_application.model.Map;
import finki.ukim.mk.map_application.model.MapInsertAsyncTaskDTO;
import finki.ukim.mk.map_application.model.Pin;
import finki.ukim.mk.map_application.model.PinDTO;
import finki.ukim.mk.map_application.model.auth.AuthenticatedUser;
import finki.ukim.mk.map_application.network.RestApiClient;
import finki.ukim.mk.map_application.repository.MapRepository;
import finki.ukim.mk.map_application.repository.PinRepository;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import retrofit2.Call;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class MapInsertDtoRestAsyncTask extends AsyncTask<MapInsertAsyncTaskDTO, Void, MapInsertAsyncTaskDTO> {
    private MapDao mapDao;
    private PinRepository pinRepository;
    private MapRepository mapRepository;
    private Context context;

    public MapInsertDtoRestAsyncTask(MapRepository mapRepository) {
        this.mapRepository = mapRepository;
        this.pinRepository = mapRepository.getPinRepository();
        this.mapDao = mapRepository.getMapDao();
        this.context = mapRepository.getContext();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected MapInsertAsyncTaskDTO doInBackground(MapInsertAsyncTaskDTO... maps) {
        Map newMap = maps[0].getMap();
        List<Pin> newMapPins = maps[0].getPins();
        AuthenticatedUser authenticatedUser = maps[0].getAuthenticatedUser();
        newMap.setOwner(authenticatedUser.getUser());
//        File imageFile = new File(newMap.getImage().getPath());
//        RequestBody requestImgFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
//
//        MultipartBody.Part imageFileBody =
//                MultipartBody.Part.createFormData("imageFile", imageFile.getName(), requestImgFile);

//            File file = new File(newMap.getImage().getPath());
//
//            RequestBody requestFile =
//                    RequestBody.create(
//                            MediaType.parse(context.getContentResolver().getType(newMap.getImage())),
//                            file
//                    );
//
//            // MultipartBody.Part is used to send also the actual file name
//            MultipartBody.Part body =
//                    MultipartBody.Part.createFormData("imageFile", file.getName(), requestFile);
//

//        final Call<Map> insertedMap = RestApiClient.getService().insertMap(RequestBody.create(MediaType.parse("text/plain"), String.valueOf(newMap.getDefault_zoom())),
//                RequestBody.create(MediaType.parse("text/plain"), String.valueOf(newMap.getCenter_latitude())),
//                RequestBody.create(MediaType.parse("text/plain"), String.valueOf(newMap.getCenter_longitude())),
//                RequestBody.create(MediaType.parse("text/plain"), newMap.getStyle()),
//                RequestBody.create(MediaType.parse("text/plain"), newMap.getName()),
//                RequestBody.create(MediaType.parse("text/plain"), newMap.getBackground()),
//                RequestBody.create(MediaType.parse("text/plain"), newMap.getDescription()),
//                RequestBody.create(MediaType.parse("text/plain"), newMap.getVisibility().toString()),
//                RequestBody.create(MediaType.parse("text/plain"), String.valueOf(newMap.getApproved())),
//                body,
//                RequestBody.create(MediaType.parse("text/plain"), authenticatedUser.getUser().getUsername()));

        final Call<Map> insertedMap = RestApiClient.getService().insertMap(newMap);

        try {
            Map mapFromRest = insertedMap.execute().body();
            MapInsertAsyncTaskDTO mapDto = new MapInsertAsyncTaskDTO(mapFromRest, newMapPins, authenticatedUser);

            return mapDto;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(MapInsertAsyncTaskDTO mapDto) {
        super.onPostExecute(mapDto);
        Map map = mapDto.getMap();
        List<Pin> pins = mapDto.getPins();

        mapRepository.insert(map);

        for (Pin pin : pins) {
            PinDTO pinDto = new PinDTO(map.getId(), pin);
            pinRepository.insertPinRest(pinDto);
        }
    }
}