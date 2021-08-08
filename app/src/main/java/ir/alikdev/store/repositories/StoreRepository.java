package ir.alikdev.store.repositories;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import java.util.List;

import ir.alikdev.store.AppExecutors;
import ir.alikdev.store.models.Category;
import ir.alikdev.store.models.CategoryAndProducts;
import ir.alikdev.store.models.CategoryProducts;
import ir.alikdev.store.models.Product;
import ir.alikdev.store.persistence.StoreDao;
import ir.alikdev.store.persistence.StoreDatabase;
import ir.alikdev.store.requests.ServiceGenerator;
import ir.alikdev.store.requests.responses.ApiResponse;
import ir.alikdev.store.requests.responses.CategoriesResponse;
import ir.alikdev.store.requests.responses.ProductResponse;
import ir.alikdev.store.requests.responses.ProductsSearchResponse;
import ir.alikdev.store.util.NetworkBoundResource;
import ir.alikdev.store.util.Resource;

public class StoreRepository {

    private static final String TAG = "StoreRepository";

    private static StoreRepository instance;
    private StoreDao storeDao;

    public static StoreRepository getInstance(Context context) {
        if (instance == null) {
            instance = new StoreRepository(context);
        }
        return instance;
    }

    private StoreRepository(Context context) {
        storeDao = StoreDatabase.getInstance(context).getStoreDao();
    }

    public LiveData<Resource<List<Product>>> searchProductsOfCategory(int page, int category_id, String query) {
        return new NetworkBoundResource<List<Product>, ProductsSearchResponse>(AppExecutors.getInstance()) {
            @Override
            protected void saveCallResult(@NonNull ProductsSearchResponse item) {
                if (item != null){
                    Product[] products=new Product[item.getProducts().size()];
                    storeDao.insertProducts(item.getProducts().toArray(products));
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Product> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Product>> loadFromDb() {
                return storeDao.getProducts(category_id, query);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<ProductsSearchResponse>> createCall() {
                return ServiceGenerator.getStoreApi().productsSearch(
                        page,
                        category_id,
                        query
                );
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<List<CategoryAndProducts>>> getCategoriesProducts(int page) {
        return new NetworkBoundResource<List<CategoryAndProducts>, CategoriesResponse>(AppExecutors.getInstance()) {
            @Override
            protected void saveCallResult(@NonNull CategoriesResponse item) {

                if (item.getCategoryProducts() != null) {
                    Category[] categories = new Category[item.getCategoryProducts().size()];

                    storeDao.insertCategories(item.getCategoryProducts().toArray(categories));

                    for (CategoryProducts categoryProducts : item.getCategoryProducts()
                    ) {
                        Product[] products = new Product[categoryProducts.getProducts().size()];
                        storeDao.insertProducts(categoryProducts.getProducts().toArray(products));
                    }
                }

            }

            @Override
            protected boolean shouldFetch(@Nullable List<CategoryAndProducts> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<CategoryAndProducts>> loadFromDb() {
                return storeDao.getCategories(page);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<CategoriesResponse>> createCall() {
                return ServiceGenerator.getStoreApi().categoriesProducts(
                        page
                );
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<Product>> getProduct(int id){
        return new NetworkBoundResource<Product , ProductResponse>(AppExecutors.getInstance()){
            @Override
            protected void saveCallResult(@NonNull ProductResponse item) {
                if(item != null){
                    Product product  = item.getProduct();
                    if(product != null){
                        storeDao.insertProduct(product);
                    }
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable Product data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<Product> loadFromDb() {
                return storeDao.getProduct(id);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<ProductResponse>> createCall() {
                return ServiceGenerator.getStoreApi().product(id);
            }
        }.getAsLiveData();
    }
}
