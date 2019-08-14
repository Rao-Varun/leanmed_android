package edu.uta.leanmed.services;

import java.util.List;

import edu.uta.leanmed.pojo.User;
import edu.uta.leanmed.pojo.UserResponse;
import edu.uta.leanmed.pojo.UsersResponse;
import edu.uta.leanmed.pojo.Zone;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Vaibhav's Console on 3/9/2019.
 */

public interface UserService {
    @Headers("Auth-Key: leanmedapi")
    @POST("login")
    Call<UserResponse> loginUser(@Body User user);

    @Headers("Auth-Key: leanmedapi")
    @POST("users/setpass")
    Call<Boolean> setPass(@Body User user);

    @Headers("Auth-Key: leanmedapi")
    @POST("users/verifyotp")
    Call<Boolean> verifyOtp(@Body User user);

    @Headers("Auth-Key: leanmedapi")
    @GET("users/forgotpass/{user}")
    Call<Boolean> forgotPass(@Path("user") String email);

    @Headers("Auth-Key: leanmedapi")
    @GET("users/zone/{zoneName}")
    Call<List<User>> getUserByZone(@Header("User-ID") String userId, @Header("Authorization") String auth, @Path("zoneName") String zone);

    @Headers("Auth-Key: leanmedapi")
    @POST("register")
    Call<String> addUser(@Body User user);

    @Headers("Auth-Key: leanmedapi")
    @GET("zones/countries/VENEZUELA")
    Call<List<Zone>> getZones();


    @Headers("Auth-Key: leanmedapi")
    @GET("zones/volunteer/recdon")
    Call<List<Zone>> getAllVenezuelaZones(@Header("User-ID") String userId, @Header("Authorization") String auth);

    @Headers("Auth-Key: leanmedapi")
    @GET("zones/volunteer/recdon/{query}")
    Call<List<Zone>> getVenezuelaZones(@Header("User-ID") String userId, @Header("Authorization") String auth, @Path("query") String query);

    @Headers("Auth-Key: leanmedapi")
    @GET("users/userstatus/{query}")
    Call<UsersResponse> getUserByStatus(@Header("User-ID") String userId, @Header("Authorization") String auth, @Path("query") int query);

    @Headers("Auth-Key: leanmedapi")
    @GET("users/userstatus/name/{query}")
    Call<UsersResponse> getUserByName(@Header("User-ID") String userId, @Header("Authorization") String auth, @Path("query") String query);

    @Headers("Auth-Key: leanmedapi")
    @POST("users/userstatus")
    Call<Boolean> updateUserStatus(@Header("User-ID") String userId, @Header("Authorization") String auth, @Body User user);
}
