package com.wanna_drink.wannadrink.http;

import com.wanna_drink.wannadrink.entities.UpdateUserData;
import com.wanna_drink.wannadrink.entities.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by redischool on 02.12.17.
 */

public interface UserService {

    @GET("users")
    Call<List<User>> getUsers();

    @POST("api/v1/Account/Register")
    Call<Object> registerUser(@Body User user);

    @Multipart
    @POST("api/v1/Account/UploadUserImage")
    Call<Object> uploadImage(@Part MultipartBody.Part image, @Part("name") RequestBody name);

    @POST("api/v1/Account/UpdateUserInfo")
    Call<Object> updateUser(@Body UpdateUserData updateUserData);

}
