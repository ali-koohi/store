package ir.alikdev.store.requests.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import ir.alikdev.store.models.Product;

public class ProductsResponse {
    @SerializedName("products")
    @Expose()
    private List<Product> products;

    public List<Product> getProducts() {
        return products;
    }

    @Override
    public String toString() {
        return "ProductsResponse{" +
                "products=" + products +
                '}';
    }
}
