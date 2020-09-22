package finki.ukim.mk.map_application.service;

import finki.ukim.mk.map_application.model.Map;
import finki.ukim.mk.map_application.model.Pin;
import finki.ukim.mk.map_application.model.auth.AuthenticatedUser;
import finki.ukim.mk.map_application.model.auth.LoginRequest;
import finki.ukim.mk.map_application.model.auth.SignUpRequest;
import finki.ukim.mk.map_application.model.auth.SignUpRespond;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface WebService {

    /**MAP Services**/

    @GET("maps")
    Call<List<Map>> getAllMaps();

    @GET("maps/owner/{owner_id}")
    Call<List<Map>> getAllUserMaps(@Path("owner_id") int owner_id);

//    @Multipart
//    @POST("maps/android")
//    Call<Map> insertMap(@Part("default_zoom") RequestBody default_zoom,
//                        @Part("center_latitude") RequestBody center_latitude,
//                        @Part("center_longitude") RequestBody  center_longitude,
//                        @Part("style") RequestBody  style,
//                        @Part("name") RequestBody  name,
//                        @Part("background") RequestBody  background,
//                        @Part("description") RequestBody  description,
//                        @Part("visibility") RequestBody  visibility,
//                        @Part("approved") RequestBody  approved,
//                        @Part MultipartBody.Part imageFile,
//                        @Part("owner") RequestBody owner);


    @POST("maps/android")
    Call<Map> insertMap(@Body Map map);
//
//    @PUT("maps/update")
//    Call<Map> updateMap(@Body Map map);

    @PUT("maps/update/android")
    Call<Map> updateMap(@Body Map map);

    @DELETE("maps/{map_id}")
    Call<Void> deleteMap(@Path("map_id") int map_id);

    /**Pin Services
     *
     * Service1: getAllMapPins - retrieve all pins of certain map
     * Service2: insertPin - marks location with marker/pin
     * **/
    @GET("maps/{map_id}/pins")
    Call<List<Pin>> getAllMapPins(@Path("map_id") int map_id);

//    @FormUrlEncoded
//    @POST("maps/{map_id}/pins")
//    Call<Pin> insertPin(@Path("map_id") int map_id, @Field("latitude") double latitude,
//                        @Field("longitude") double longitude, @Field("name") String name,
//                        @Field("description") String description, @Field("image") String image);

    @POST("maps/{map_id}/pins/android")
    Call<Pin> insertPin(@Path("map_id") int map_id, @Body Pin pin);

//    @PUT("maps/{map_id}/pins/update")
//    Call<Pin> updatePin(@Path("map_id") int map_id, @Body Pin pin);

    @PUT("maps/{map_id}/pins/update/android")
    Call<Pin> updatePin(@Path("map_id") int map_id, @Body Pin pin);

    @DELETE("maps/{map_id}/pins/{pin_id}")
    Call<Void> deletePin(@Path("pin_id") int pin_id);

    @POST("/api/auth/signin")
    Call<AuthenticatedUser> authenticateUser(@Body LoginRequest loginRequest);

    @POST("/api/auth/signup")
    Call<SignUpRespond> registerUser(@Body SignUpRequest request);
}
