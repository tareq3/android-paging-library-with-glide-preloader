package com.paging.com.mysample.homescreen;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PageKeyedDataSource;
import android.arch.paging.PagedList;

import com.paging.com.mysample.pojo.Image;

public class ItemViewModel extends ViewModel {

   // public LiveData<PagedList<Image>> itemPagedList;
    public LiveData<PageKeyedDataSource<Integer, Image>> liveDataSource;

    //For quries
    private MutableLiveData<String> queryLiveData = new MutableLiveData<>();

    //Applying transformation to get RepoSearchResult for the given Search Query
    private LiveData<PagedList<Image>> repoReuslt = Transformations.switchMap(queryLiveData,
            inputQuery -> producePagedList(inputQuery)
    );



    public ItemViewModel() {

    }


    LiveData<PagedList<Image>> producePagedList(String query ){
        ItemDataSourceFactory itemDataSourceFactory = new ItemDataSourceFactory(query);
        liveDataSource = itemDataSourceFactory.getItemLiveDataSource();

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(ItemDataSource.PAGE_SIZE).build();

        LiveData<PagedList<Image>> itemPagedList = (new LivePagedListBuilder(itemDataSourceFactory, pagedListConfig))
                .build();
        return itemPagedList;
    }


    public LiveData<PagedList<Image>> getRepoReuslt() {
        return repoReuslt;
    }


    /**
     * Search a repository based on a query string.
     */
    public void searchProducts(String queryString) {
        queryLiveData.postValue(queryString);

    }

}
