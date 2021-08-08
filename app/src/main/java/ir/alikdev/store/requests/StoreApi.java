package ir.alikdev.store.requests;

import androidx.lifecycle.LiveData;

import java.util.List;

import ir.alikdev.store.requests.responses.ApiResponse;
import ir.alikdev.store.requests.responses.CategoriesResponse;
import ir.alikdev.store.requests.responses.ProductResponse;
import ir.alikdev.store.requests.responses.ProductsResponse;
import ir.alikdev.store.requests.responses.ProductsSearchResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface StoreApi {

    @GET("store/categories_products.php")
    LiveData<ApiResponse<CategoriesResponse>> categoriesProducts(
            @Query("page") int page
    );//https://alikdev.ir/store/categories_products.php?page=1

    @GET("store/products.php")
    LiveData<ApiResponse<ProductsResponse>> products(
      @Query("page") int page,
      @Query("cat_name") String cat_name
    );//https://alikdev.ir/store/categories_products.php?page=1&cat_name=some_category

    @GET("store/products.php")
    LiveData<ApiResponse<ProductsSearchResponse>> productsSearch(
            @Query("page") int page ,
            @Query("cat_id") int category_id,
            @Query("pd_name") String pd_name
    );//https://alikdev.ir/store/products.php?page=1&cat_id=1...&pd_name=some_name

    @GET("store/product.php")
    LiveData<ApiResponse<ProductResponse>> product(
            @Query("pd_id") int id
    );//https://alikdev.ir/store/product.php?pd_id=15
}
