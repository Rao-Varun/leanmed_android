package edu.uta.leanmed.services;

import edu.uta.leanmed.pojo.Donor;
import edu.uta.leanmed.pojo.DonorResponse;
import edu.uta.leanmed.pojo.Inventory;
import edu.uta.leanmed.pojo.InventoryResponse;
import edu.uta.leanmed.pojo.Medicine;
import edu.uta.leanmed.pojo.MedicineResponse;
import edu.uta.leanmed.pojo.User;
import edu.uta.leanmed.pojo.UserResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Vaibhav's Console on 4/21/2019.
 */

public interface MedicineAPIService {

    @Headers("Auth-Key: leanmedapi")
    @GET("inventory")
    Call<InventoryResponse> getAllInventoryResponse(@Header("User-ID") String userId, @Header("Authorization") String auth);

    @Headers("Auth-Key: leanmedapi")
    @GET("inventory/{query}")
    Call<InventoryResponse> getInventoryResponse(@Header("User-ID") String userId, @Header("Authorization") String auth, @Path("query") String query);

    @Headers("Auth-Key: leanmedapi")
    @POST("inventory/addnewitem")
    Call<InventoryResponse> addNewInventory(@Header("User-ID") String userId, @Header("Authorization") String auth, @Body Inventory inventory);

    @Headers("Auth-Key: leanmedapi")
    @GET("medicine/getmedicine/{query}")
    Call<MedicineResponse> getMedicineResponse(@Header("User-ID") String userId, @Header("Authorization") String auth, @Path("query") String query);

    @Headers("Auth-Key: leanmedapi")
    @POST("medicine/addmedicine")
    Call<MedicineResponse> addNewMedicine(@Header("User-ID") String userId, @Header("Authorization") String auth, @Body Medicine medicine);

    @Headers("Auth-Key: leanmedapi")
    @GET("medicine/getmedicine")
    Call<MedicineResponse> getAllMedicinesResponse(@Header("User-ID") String userId, @Header("Authorization") String auth);

    @Headers("Auth-Key: leanmedapi")
    @GET("donor/getdonordetails")
    Call<DonorResponse> getAllDonorsResponse(@Header("User-ID") String userId, @Header("Authorization") String auth);

    @Headers("Auth-Key: leanmedapi")
    @GET("donor/getdonordetails/{query}")
    Call<DonorResponse> getDonorResponse(@Header("User-ID") String userId, @Header("Authorization") String auth, @Path("query") String query);

    @Headers("Auth-Key: leanmedapi")
    @POST("donor/adddonordetails")
    Call<DonorResponse> addNewDonor(@Header("User-ID") String userId, @Header("Authorization") String auth, @Body Donor donor);

}
