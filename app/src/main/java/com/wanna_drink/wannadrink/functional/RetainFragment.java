package com.wanna_drink.wannadrink.functional;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.wanna_drink.wannadrink.entities.User;
import com.wanna_drink.wannadrink.http.RestClient;
import com.wanna_drink.wannadrink.http.UserService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    public void createUser(final Consumer<Void> consumer) {
        getConsumer = consumer;
        Call<Void> call = service.createUser((User) getConsumer.get());
        call.enqueue(new Callback<Void>(){

            @Override
            public void onResponse(Call<Void> call, final Response<Void> response) {

                if(response.isSuccessful()){
                    if (users == null) {
                        // get users in case it is null
                        getUsers(new Consumer<List<User>>() {
                            @Override
                            public void apply(List<User> users) {
                                consumer.apply(response.body());
                            }

                            @Override
                            public Object get() {
                                return null;
                            }
                        });
                    }
                    else{
                        getConsumer.apply(response.body());
                    }

                }
                else{
                    showNetError(response.message());
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                showNetError(t.getMessage());
            }
        });
    }

    public void getUsers(final Consumer<List<User>> consumer) {
        getConsumer = consumer;
        if (users == null) {
            Call<List<User>> call = service.getUsers();
            call.enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    if (response.isSuccessful()) {
                        users = response.body();
                        getConsumer.apply(users);
                    } else {
                        showNetError(response.message());
                    }
                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {
                    showNetError(t.getMessage());
                }
            });
        } else {
            getConsumer.apply(users);
        }
    }

    private void showNetError(String errorMessage){
        Toast.makeText(getActivity(), "Kein Internetzugriff möglich!", Toast.LENGTH_SHORT).show();
        Log.d("NetworkError", errorMessage);
    }
}
