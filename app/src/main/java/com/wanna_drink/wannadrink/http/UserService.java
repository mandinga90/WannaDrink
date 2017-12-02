package com.wanna_drink.wannadrink.http;

import com.wanna_drink.wannadrink.entities.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by redischool on 02.12.17.
 */

public interface UserService {
    @Headers({
            "Content-Type: application/json;charset=utf-8",
            "Accept: application/json"
    })

    @GET("/users.json")
    Call<List<User>> getUsers();
}
