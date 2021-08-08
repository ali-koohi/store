package ir.alikdev.store.requests;

import java.util.concurrent.TimeUnit;

import ir.alikdev.store.util.LiveDataCallAdapterFactory;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static ir.alikdev.store.util.Constants.BASE_URL;
import static ir.alikdev.store.util.Constants.*;

public class ServiceGenerator {

    private static OkHttpClient okHttpClient=new OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT , TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT , TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .build();

    private static Retrofit.Builder retrofitBuilder=new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(new LiveDataCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit=retrofitBuilder.build();
    private static StoreApi storeApi = retrofit.create(StoreApi.class);

    public static StoreApi getStoreApi(){
        return storeApi;
    }
}
