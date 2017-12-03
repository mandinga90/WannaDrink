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
//    @Headers(
//            "X-Apikey: NzU1NEU4RjktRjVEMC00MTk3LThFOUUtRjczRjYxMjdGMDg5"
//    )

    @GET("users")
    Call<List<User>> getUsers();

    @POST("api/v1/Account/Register")
    Call<Object> registerUser(@Body User user);

    @PATCH("users/{id}")
    Call<Void> updateUser(@Path("id") String id, @Body User user);

//    @DELETE("/users/{id}")
//    Call<Void> deleteVenue(@Path("id") String id);
}
