package finki.ukim.mk.map_application.network;

import finki.ukim.mk.map_application.service.WebService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApiClient {
    private static Retrofit retrofit = null;
    private static final String baseUrl = "http://192.168.1.101:8080/";

    private static Retrofit getRetrofit(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static WebService getService(){
        return getRetrofit().create(WebService.class);
    }

}
