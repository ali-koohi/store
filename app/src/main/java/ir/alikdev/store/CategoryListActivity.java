package ir.alikdev.store;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import ir.alikdev.store.adapter.CategoryRecyclerAdapter;
import ir.alikdev.store.adapter.OnCategoryClickListener;
import ir.alikdev.store.models.Category;
import ir.alikdev.store.models.CategoryAndProducts;
import ir.alikdev.store.util.Resource;
import ir.alikdev.store.util.VerticalSpacingItemDecorator;
import ir.alikdev.store.viewmodels.CategoryProductsViewModel;

import static ir.alikdev.store.viewmodels.CategoryProductsViewModel.EXHAUSTED;

public class CategoryListActivity extends BaseActivity implements OnCategoryClickListener {

    private static final String TAG = "CategoryListActivity";

    private CategoryProductsViewModel viewModel;

    private RecyclerView mRecyclerView;
    private CategoryRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        mRecyclerView = findViewById(R.id.store_list);

        viewModel = ViewModelProviders.of(this).get(CategoryProductsViewModel.class);

        initRecyclerView();
        subscribeObservers();
        getCategoryProducts(1);
    }

    private void subscribeObservers() {
        viewModel.getCategoryAndProducts().observe(this, new Observer<Resource<List<CategoryAndProducts>>>() {
            @Override
            public void onChanged(Resource<List<CategoryAndProducts>> listResource) {
                if (listResource != null) {
                    Log.d(TAG, "onChanged: status: " + listResource.status);
                    if (listResource.data != null){
                        switch (listResource.status) {
                            case LOADING: {
                                if (viewModel.getPage() > 1) {
                                    mAdapter.displayLoading();
                                } else {
                                    mAdapter.displayOnlyLoading();
                                }
                                break;
                            }
                            case SUCCESS: {
                                Log.d(TAG, "onChanged: cache has been refreshed.");
                                Log.d(TAG, "onChanged: status: SUCCESS, #Categories: " + listResource.data.size());
                                mAdapter.hideLoading();
                                mAdapter.setCategoryAndProducts(listResource.data);
                                break;
                            }
                            case ERROR: {
                                Log.e(TAG, "onChanged: cannot refresh cache.");
                                Log.e(TAG, "onChanged: ERROR message: " + listResource.message);
                                Log.e(TAG, "onChanged: status: ERROR, #Categories: " + listResource.data.size());
                                mAdapter.hideLoading();
                                mAdapter.setCategoryAndProducts(listResource.data);
                                Toast.makeText(CategoryListActivity.this, listResource.message, Toast.LENGTH_SHORT).show();

                                if (listResource.message.equals(EXHAUSTED)) {
                                    mAdapter.setExhausted();
                                }
                                break;
                            }
                        }
                    }

                }
            }
        });
    }

    private void initRecyclerView(){
        mAdapter = new CategoryRecyclerAdapter(initGlide() , this);

        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(30);
        mRecyclerView.addItemDecoration(itemDecorator);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!recyclerView.canScrollVertically(1)){
                    viewModel.nextPage();
                }
            }
        });

        mRecyclerView.setAdapter(mAdapter);
    }

    private RequestManager initGlide(){
        RequestOptions requestOptions=new RequestOptions()
                .placeholder(R.drawable.white_background)
                .error(R.drawable.white_background);

        return Glide.with(this)
                .setDefaultRequestOptions(requestOptions);
    }

    private void getCategoryProducts(int page){
        viewModel.getCategoriesProducts(page);
    }

    @Override
    public void onBackPressed() {
        viewModel.cancelRequest();
        super.onBackPressed();
    }

    @Override
    public void onAllClick(int position) {
        Category category = mAdapter.getCategory(position);
        Intent intent = new Intent(CategoryListActivity.this , ProductsListActivity.class);
        intent.putExtra("category" , category);
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position) {
        Category category = mAdapter.getCategory(position);
        Intent intent = new Intent(CategoryListActivity.this , ProductsListActivity.class);
        intent.putExtra("category" , category);
        startActivity(intent);
    }
}