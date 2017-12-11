package com.wanna_drink.wannadrink.functional;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;
import com.wanna_drink.wannadrink.entities.UpdateUserData;
import com.wanna_drink.wannadrink.entities.User;
import com.wanna_drink.wannadrink.entities.UserInformation;
import com.wanna_drink.wannadrink.http.RestClient;
import com.wanna_drink.wannadrink.http.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.wanna_drink.wannadrink.functional.SafeConversions.toDouble;

/**
 * Created by redischool on 02.12.17.
 */

public class RetainFragment extends Fragment {
    private UserService service = RestClient.getInstance().createService(UserService.class);
    private List<User> users;
    private Consumer getConsumer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void registerUser(final Consumer<Void> consumer) {
        getConsumer = consumer;
        final User user = (User) getConsumer.get();
        Call<Object> call = service.registerUser(user);
        call.enqueue(new Callback<Object>(){

            @Override
            public void onResponse(Call<Object> call, final Response<Object> response) {

                if(response.isSuccessful()){

                    double responseCode = (Double) (((Map) response.body()).get("code"));
                    boolean userAlreadyExists = ( responseCode > 0 );
                    if(userAlreadyExists){
                        updateUser(user);
                    }

//                    if (users == null) {
//                        // get users in case it is null
//                        getUsers(new Consumer<List<User>>() {
//                            @Override
//                            public void apply(List<User> users) {
//                                consumer.apply(response.body());
//                            }
//
//                            @Override
//                            public Object get() {
//                                return null;
//                            }
//                        });
//                    }
//                    else{
//                        getConsumer.apply(response.body());
//                    }

                }
                else{
                    showNetError(response.message());
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                showNetError(t.getMessage());
            }
        });
    }

    public void updateUser(User user){

        UpdateUserData updateUserData = new UpdateUserData(user.getEmail(),user.getAvailable().getLat(),user.getAvailable().getLng(),user.getAvailable().getHours(),user.getFavoriteDrinks()[0].getId(), user.getUId(), user.getDistance());
        Call<Object> call = service.updateUser(updateUserData);

        call.enqueue(new Callback<Object>(){

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()){

                }
                else{

                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

    }

    public void getUsers(User user, final Consumer<List<Map>> consumer) {
        getConsumer = consumer;
        if (users == null) {
            Call<Object> call = service.getUsers(new UserInformation(toDouble(user.getAvailable().getLat()),
                    toDouble(user.getAvailable().getLng()),
                    user.getEmail(),
                    user.getUId(),
                    5000));
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    if (response.isSuccessful()) {
                        Map responseMap = (Map) ((Map)response.body()).get("data");
                        double count = (Double) responseMap.get("count");
                        List<User> users = (ArrayList) responseMap.get("mates");
                        getConsumer.apply(users);
                    } else {
                        showNetError(response.message());
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    showNetError(t.getMessage());
                }
            });
        } else {
            getConsumer.apply(users);
        }
    }

    private void showNetError(String errorMessage){
        Toast.makeText(getActivity(), "No network available!", Toast.LENGTH_SHORT).show();
        Log.d("NetworkError", errorMessage);
    }
}
