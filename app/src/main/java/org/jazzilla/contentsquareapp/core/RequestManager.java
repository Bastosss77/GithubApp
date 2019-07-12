package org.jazzilla.contentsquareapp.core;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestManager {
    private static RequestManager INSTANCE;
    private static final String GITHUB_API_URL = "https://api.github.com";
    private Retrofit mRetrofit;

    private RequestManager() {
        OkHttpClient client = createClient();
        Gson gson = createGson();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(GITHUB_API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    private OkHttpClient createClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder().addInterceptor(interceptor).build();
    }

    private Gson createGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    public static RequestManager getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new RequestManager();
        }

        return INSTANCE;
    }

    public <T> T createService(Class<T> clazz) {
        return INSTANCE.mRetrofit.create(clazz);
    }
}
