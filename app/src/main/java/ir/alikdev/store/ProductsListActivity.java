package ir.alikdev.store;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import ir.alikdev.store.adapter.OnProductListener;
import ir.alikdev.store.adapter.ProductsRecyclerAdapter;
import ir.alikdev.store.models.Category;
import ir.alikdev.store.models.Product;
import ir.alikdev.store.util.Resource;
import ir.alikdev.store.util.VerticalSpacingItemDecorator;
import ir.alikdev.store.viewmodels.ProductsViewModel;

import static ir.alikdev.store.adapter.ProductsRecyclerAdapter.PRODUCTS_TYPE;
import static ir.alikdev.store.viewmodels.CategoryProductsViewModel.EXHAUSTED;

public class ProductsListActivity extends BaseActivity implements OnProductListener {

    private static final String TAG = "ProductsListActivity";

    private RecyclerView recyclerView;
    private ProductsRecyclerAdapter adapter;

    private ProductsViewModel viewModel;
    private ImageButton changeLayout;
    private androidx.appcompat.widget.SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        changeLayout = findViewById(R.id.change_layout);
        searchView = findViewById(R.id.search_view);

        recyclerView = findViewById(R.id.products_list);
        viewModel = ViewModelProviders.of(this).get(ProductsViewModel.class);

        initRecyclerView();
        subscribeObservable();
        getCategoryIntent();
        initChangeLayout();
        initSearchView();
    }

    private void initChangeLayout(){
        changeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentType=adapter.changeLayout();
                if(currentType == PRODUCTS_TYPE){
                    changeLayout.setBackgroundResource(R.drawable.ic_baseline_view_list_24);
                }else{
                    changeLayout.setBackgroundResource(R.drawable.ic_baseline_view_comfy_24);
                }
            }
        });
    }

    private  void initSearchView(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchProducts(1,viewModel.getCategoryId(),query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void getCategoryIntent() {
        if (getIntent().hasExtra("category") ) {
            Category category = getIntent().getParcelableExtra("category");
            viewModel.setCategoryId(category.getId());
            searchProducts(1, viewModel.getCategoryId(), "");
        } else {
            finish();
            Toast.makeText(this, "category type is empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void subscribeObservable() {
        viewModel.getProducts().observe(this, new Observer<Resource<List<Product>>>() {
            @Override
            public void onChanged(Resource<List<Product>> listResource) {
                if (listResource != null) {
                    Log.d(TAG, "onChanged: "+listResource.status);
                    switch (listResource.status){
                        case LOADING:
                            if(viewModel.getPage() > 1 ){
                                adapter.displayLoading();
                            }else{
                                adapter.displayOnlyLoading();
                            }
                            break;
                        case SUCCESS:
                            Log.d(TAG, "onChanged: cache has been refreshed.");
                            Log.d(TAG, "onChanged: status: SUCCESS, #Products: " + listResource.data.size());
                            adapter.hideLoading();
                            adapter.setProducts(listResource.data);
                            break;
                        case ERROR:
                            Log.e(TAG, "onChanged: cannot refresh cache.");
                            Log.e(TAG, "onChanged: ERROR message: " + listResource.message);
                            Log.e(TAG, "onChanged: status: ERROR, #Products: " + listResource.data.size());
                            adapter.hideLoading();
                            adapter.setProducts(listResource.data);
                            Toast.makeText(ProductsListActivity.this, listResource.message, Toast.LENGTH_SHORT).show();
                            if (listResource.message.equals(EXHAUSTED)) {
                                adapter.setExhausted();
                            }
                            break;
                    }
                }

            }
        });
    }

    private void searchProducts(int page, int category_id, String query) {
        recyclerView.smoothScrollToPosition(0);
        viewModel.searchProductsOfCategory(page, category_id, query);
        searchView.clearFocus();
    }

    private void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        gridLayoutManager.setSpanSizeLookup(spanSizeLookup);

        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(30);
        recyclerView.addItemDecoration(itemDecorator);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new ProductsRecyclerAdapter(initGlide(),this::OnProductClick);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!recyclerView.canScrollVertically(1)){
                    viewModel.nextPage();
                }
            }
        });
    }

    private RequestManager initGlide() {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.white_background)
                .error(R.drawable.white_background);

        return Glide.with(this).setDefaultRequestOptions(requestOptions);
    }

    @Override
    public void onBackPressed() {
        viewModel.cancelRequest();
        super.onBackPressed();
    }

    @Override
    public void OnProductClick(int position) {

        Intent intent = new Intent(ProductsListActivity.this,ProductActivity.class);
        intent.putExtra("product",adapter.getProduct(position));
        startActivity(intent);

    }

    private GridLayoutManager.SpanSizeLookup spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
        @Override
        public int getSpanSize(int position) {
            int viewType = adapter.getItemViewType(position);
            if (viewType == PRODUCTS_TYPE) {
                return 1;
            }
            return 2;
        }
    };


}