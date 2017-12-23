package com.wanna_drink.wannadrink.http;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {
    private static final String URL = "http://wannadrink.azurewebsites.net/";
    //    private static final String USERNAME = "redaktion@cineclub.de";
    //    private static final String PASSWORD = "apfelbaum";
    private static final RestClient ourInstance = new RestClient();
    //    private static final String AUTHORIZATION_KEY = "Authorization";

    private OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create());

    private RestClient() {
    }

    public static RestClient getInstance() {
        return ourInstance;
    }

    public <S> S createService(Class<S> serviceClass) {
//        final String credentials = USERNAME + ":" + PASSWORD;
//        final String basic = "Basic " + Base64.encodeToString(credentials.getBytes(),
//                Base64.NO_WRAP);

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder reqBuilder = original.newBuilder()
//                        .header(AUTHORIZATION_KEY, basic)
                        .header("X-Apikey", "NzU1NEU4RjktRjVEMC00MTk3LThFOUUtRjczRjYxMjdGMDg5")
                        .method(original.method(), original.body());

                Request request = reqBuilder.build();
                return chain.proceed(request);
            }
        });

        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }
}
