package com.wanna_drink.wannadrink.http;

import com.wanna_drink.wannadrink.entities.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by redischool on 02.12.17.
 */

public interface UserService {
    @Headers({
            "Content-Type: application/json;charset=utf-8",
            "Accept: application/json"
    })

    @GET("/users")
    Call<List<User>> getUsers();

    @POST("/users")
    Call<Void> createUser(@Body User user);

    @PATCH("/users/{id}")
    Call<Void> updateUser(@Path("id") String id, @Body User user);

//    @DELETE("/users/{id}")
//    Call<Void> deleteVenue(@Path("id") String id);
}
