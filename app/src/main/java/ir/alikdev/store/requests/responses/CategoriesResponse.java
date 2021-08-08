package ir.alikdev.store.requests.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import ir.alikdev.store.models.CategoryProducts;

public class CategoriesResponse {
    @SerializedName("categories")
    @Expose()// serialize to json and deserialize from json
    private List<CategoryProducts> getCategoryProducts;

    public List<CategoryProducts> getCategoryProducts() {
        return getCategoryProducts;
    }

    @Override
    public String toString() {
        return "CategoriesResponse{" +
                "getCategoryProducts=" + getCategoryProducts +
                '}';
    }
}
