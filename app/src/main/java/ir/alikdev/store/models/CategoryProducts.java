package ir.alikdev.store.models;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

//this class is to get category with list of its products or
// you can add list of product inside of category class but i prefer something like this
//in other word every category have some products
public class CategoryProducts extends Category {

    private List<Product> products;

    public CategoryProducts(int id, String name, int parent_id, String create_at, List<Product> products) {
        super(id, name, parent_id, create_at);
        this.products = products;
    }

    public CategoryProducts() {
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "CategoryProducts{" +
                "category=" + super.toString() +
                "products=" + products +
                '}';
    }
}
