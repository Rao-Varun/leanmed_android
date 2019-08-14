package edu.uta.leanmed.services;
import java.util.List;

import edu.uta.leanmed.pojo.Box;
import edu.uta.leanmed.pojo.BoxContent;
import edu.uta.leanmed.pojo.BoxResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BoxAPIService {

    @Headers("Auth-Key: leanmedapi")
    @POST("makeabox/addbox")
    Call<BoxResponse> addNewBox(@Header("User-ID") String userId, @Header("Authorization") String auth, @Body Box box);

    @Headers("Auth-Key: leanmedapi")
    @POST("makeabox/addboxcontent")
    Call<BoxResponse> addNewBoxContent(@Header("User-ID") String userId, @Header("Authorization") String auth, @Body BoxContent boxContent);

    @Headers("Auth-Key: leanmedapi")
    @GET("makeabox/getboxbyzone/{query}")
    Call<BoxResponse> getBoxByZone(@Header("User-ID") String userId, @Header("Authorization") String auth, @Path("query") String ZoneId);

    @Headers("Auth-Key: leanmedapi")
    @GET("makeabox/getactiveboxbyzone/{query}")
    Call<BoxResponse> getActiveBoxByZone(@Header("User-ID") String userId, @Header("Authorization") String auth, @Path("query") String ZoneId);

    @Headers("Auth-Key: leanmedapi")
    @POST("makeabox/removeboxcontent")
    Call<BoxResponse> removeBoxContent(@Header("User-ID") String userId, @Header("Authorization") String auth, @Body BoxContent boxContent);

    @Headers("Auth-Key: leanmedapi")
    @POST("makeabox/removebox")
    Call<BoxResponse> removeBox(@Header("User-ID") String userId, @Header("Authorization") String auth, @Body Box box);

    @Headers("Auth-Key: leanmedapi")
    @POST("makeabox/removeboxcontents")
    Call<BoxResponse> removeBoxContents(@Header("User-ID") String userId, @Header("Authorization") String auth, @Body List<BoxContent> boxContents);


    @Headers("Auth-Key: leanmedapi")
    @POST("makeabox/editbox")
    Call<BoxResponse> editBox(@Header("User-ID") String userId, @Header("Authorization") String auth, @Body Box box);



}
