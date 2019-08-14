package edu.uta.leanmed.services;

import edu.uta.leanmed.pojo.Request;
import edu.uta.leanmed.pojo.RequestResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RequestAPIService {


    @Headers("Auth-Key: leanmedapi")
    @POST("requests/rejectrequest")
    Call<RequestResponse> rejectRequest(@Header("User-ID") String userId, @Header("Authorization") String auth, @Body Request request);


    @Headers("Auth-Key: leanmedapi")
    @POST("requests/acceptrequest")
    Call<RequestResponse> acceptRequest(@Header("User-ID") String userId, @Header("Authorization") String auth, @Body Request request);

    @Headers("Auth-Key: leanmedapi")
    @GET("requests/requestsbyaccepteduser/{query}")
    Call<RequestResponse> getRequestsByAcceptedUser(@Header("User-ID") String userId, @Header("Authorization") String auth, @Path("query") String zoneId);


    @Headers("Auth-Key: leanmedapi")
    @POST("requests/changerequeststatus")
    Call<RequestResponse> changeRequestStatus(@Header("User-ID") String userId, @Header("Authorization") String auth, @Body Request request);
}
