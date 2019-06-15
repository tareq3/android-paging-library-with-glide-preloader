package com.paging.com.mysample.homescreen;

import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import com.paging.com.mysample.pojo.IMResponse;
import com.paging.com.mysample.pojo.Image;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ItemDataSource extends PageKeyedDataSource<Integer, Image> {

    public static final int PAGE_SIZE = 10;
    private static final int FIRST_PAGE = 1;

    private final String API_KEY = "8439361-5e1e53a0e1b58baa26ab398f7";

    private String query;


    public ItemDataSource(String query) {
        this.query = query;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Image> callback) {
        Log.d("tareq_test" , "load initial");
        RetrofitClient.getInstance()
                .getApi().getAllImagesWithSearch(API_KEY, FIRST_PAGE, "3",query)
                .enqueue(new Callback<IMResponse>() {
                    @Override
                    public void onResponse(Call<IMResponse> call, Response<IMResponse> response) {
                        if (response.body() != null) {
                            callback.onResult(response.body().getImages(), null, FIRST_PAGE + 1);
                            Log.d("tareq_test" , "response ok");
                        }
                    }

                    @Override
                    public void onFailure(Call<IMResponse> call, Throwable t) {

                    }
                });
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Image> callback) {
        RetrofitClient.getInstance()
                .getApi().getAllImagesWithSearch(API_KEY, params.key, "3",query)
                .enqueue(new Callback<IMResponse>() {
                    @Override
                    public void onResponse(Call<IMResponse> call, Response<IMResponse> response) {
                        Integer adjacentKey = (params.key > 1) ? params.key - 1 : null;
                        if (response.body() != null) {
                            callback.onResult(response.body().getImages(), adjacentKey);
                        }
                    }

                    @Override
                    public void onFailure(Call<IMResponse> call, Throwable t) {

                    }
                });
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Image> callback) {
        RetrofitClient.getInstance()
                .getApi()
                .getAllImagesWithSearch(API_KEY, params.key, "3",query)
                .enqueue(new Callback<IMResponse>() {
                    @Override
                    public void onResponse(Call<IMResponse> call, Response<IMResponse> response) {
                        if (response.body() != null && true) {
                            callback.onResult(response.body().getImages(), params.key + 1);
                        }
                    }

                    @Override
                    public void onFailure(Call<IMResponse> call, Throwable t) {

                    }
                });
    }
}
