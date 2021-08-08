package ir.alikdev.store.requests.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ir.alikdev.store.models.Product;

public class ProductResponse {
    @SerializedName("product")
    @Expose()
    private Product product;

    public Product getProduct() {
        return product;
    }

    @Override
    public String toString() {
        return "ProductResponse{" +
                "product=" + product +
                '}';
    }
}
