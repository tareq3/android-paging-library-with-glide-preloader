package com.paging.com.mysample.homescreen;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;

import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.paging.com.mysample.R;
import com.paging.com.mysample.Util.GlideApp;
import com.paging.com.mysample.pojo.Image;

import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private ShimmerFrameLayout shimmerViewContainer;
   private RecyclerView mRecyclerView;
    private  ItemViewModel mItemViewModel;
    private ItemAdapter mItemAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpUI();
        mRecyclerView= findViewById(R.id.image_list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,1));
        mRecyclerView.setHasFixedSize(true);
       mItemViewModel= ViewModelProviders.of(this).get(ItemViewModel.class);

        RequestBuilder<Drawable> gifItemRequest = GlideApp.with(this).asDrawable();

        ViewPreloadSizeProvider<Image> preloadSizeProvider = new ViewPreloadSizeProvider<>();

        mItemAdapter= new ItemAdapter(this,gifItemRequest,preloadSizeProvider);

        RecyclerViewPreloader<Image> preloader =
                new RecyclerViewPreloader<>(GlideApp.with(this), mItemAdapter, preloadSizeProvider, 20);
        mRecyclerView.addOnScrollListener(preloader);
        mRecyclerView.setRecyclerListener( (holder) -> {
            // This is an optimization to reduce the memory usage of RecyclerView's recycled view
            // pool
            // and good practice when using Glide with RecyclerView.
            ItemAdapter.ItemViewHolder gifViewHolder = (ItemAdapter.ItemViewHolder) holder;
            GlideApp.with(MainActivity.this).clear(gifViewHolder.image);
        });

        mRecyclerView.setAdapter(mItemAdapter);

        showLoadingIndicator(true);
        mItemViewModel.getRepoReuslt().observe(this, new Observer<PagedList<Image>>() {
            @Override
            public void onChanged(@Nullable final PagedList<Image> items) {

                if (shimmerViewContainer.getVisibility() == View.VISIBLE) {
                    showLoadingIndicator(false);
                }
                mRecyclerView.setVisibility(View.VISIBLE);
                mItemAdapter.submitList(items);

            }
        });



        mItemViewModel.searchProducts("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem search_item = menu.findItem(R.id.menu_search);

        SearchView searchView = (SearchView) search_item.getActionView();
        searchView.setFocusable(false);
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String s) {

                //clear the previous data in search arraylist if exist
                updateRepoListFromInput( s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


        return true;

    }


    /**
     * Updates the list with the new data when the User entered the query and hit 'enter' or
     * corresponding action to trigger the Search.
     */
    private void updateRepoListFromInput(String query) {

        String queryEntered = query.trim();
        //if (!TextUtils.isEmpty(queryEntered)) {
        mRecyclerView.scrollToPosition(0);
        //Posts the query to be searched
        mItemViewModel.searchProducts(queryEntered);
        //Resets the old list
        mItemAdapter.submitList(null);
        //}
    }


    public void showLoadingIndicator(boolean active) {
        if (active) {
            shimmerViewContainer.setVisibility(View.VISIBLE);
            shimmerViewContainer.startShimmer();
        } else {

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    shimmerViewContainer.stopShimmer();
                    shimmerViewContainer.setVisibility(View.GONE);
                }
            }, 2000);

        }
    }

    public void setUpUI() {
        Toolbar toolbar = findViewById(R.id.tabanim_toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);
        shimmerViewContainer = findViewById(R.id.shimmer_view_container);
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmerViewContainer.startShimmer();
    }

    @Override
    protected void onPause() {
        shimmerViewContainer.stopShimmer();
        super.onPause();
    }



}
