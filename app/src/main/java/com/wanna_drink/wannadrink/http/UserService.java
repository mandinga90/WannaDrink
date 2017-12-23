package com.wanna_drink.wannadrink.http;

import com.wanna_drink.wannadrink.entities.UpdateUserData;
import com.wanna_drink.wannadrink.entities.User;
import com.wanna_drink.wannadrink.entities.UserInformation;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UserService {

    @POST("api/v1/Mates/NearByMates")
    Call<Object> getUsers(@Body UserInformation userInformation);

    @POST("api/v1/Account/Register")
    Call<Object> registerUser(@Body User user);

    @Multipart
    @POST("api/v1/Account/UploadUserImage")
    Call<Object> uploadImage(@Part MultipartBody.Part image, @Part("name") RequestBody name);

    @POST("api/v1/Account/UpdateUserInfo")
    Call<Object> updateUser(@Body UpdateUserData updateUserData);

}
