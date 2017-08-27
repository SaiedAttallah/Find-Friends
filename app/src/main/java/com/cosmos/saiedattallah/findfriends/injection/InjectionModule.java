package com.cosmos.saiedattallah.findfriends.injection;

import android.content.Context;

import com.cosmos.saiedattallah.findfriends.App;
import com.cosmos.saiedattallah.findfriends.rest.WebApiInterface;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(
        injects = {
                App.class,
                OkHttpClient.class,
                Retrofit.class,
                WebApiInterface.class
        },
        library = true
)

public class InjectionModule {
    private final Context application;

    public InjectionModule(Context application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Context provideApplicationContext() {
        return this.application;
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);
        return builder.build();
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://stepbystepsoftware.ddns.net:8020/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    public WebApiInterface provideWebApiInterface(Retrofit retrofit) {
        return retrofit.create(WebApiInterface.class);
    }
}
