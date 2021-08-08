package ir.alikdev.store.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.List;

import ir.alikdev.store.R;
import ir.alikdev.store.models.Category;
import ir.alikdev.store.models.CategoryAndProducts;
import ir.alikdev.store.models.Product;

public class ProductsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int PRODUCTS_TYPE = 1;
    public static final int PRODUCTS_DETAILS_TYPE = 2;
    private static final int LOADING_TYPE = 3;
    private static final int EXHAUSTED_TYPE = 4;

    //here you can change default view
    private int currentType = PRODUCTS_TYPE;

    private List<Product> products;
    private RequestManager requestManager;
    private OnProductListener listener;

    public ProductsRecyclerAdapter(RequestManager requestManager, OnProductListener listener) {
        this.requestManager = requestManager;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case PRODUCTS_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
                return new ProductViewHolder(view, requestManager, listener);
            case PRODUCTS_DETAILS_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_details, parent, false);
                return new ProductDetailsViewHolder(view, requestManager, listener);
            case LOADING_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_list_item, parent, false);
                return new LoadingViewHolder(view);
            case EXHAUSTED_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_exhausted_list_item, parent, false);
                return new ExhaustedViewHolder(view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
                return new ProductViewHolder(view, requestManager, listener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == PRODUCTS_TYPE) {
            ProductViewHolder productViewHolder = (ProductViewHolder) holder;
            productViewHolder.onBind(products.get(position));
        }else if(viewType == PRODUCTS_DETAILS_TYPE){
            ((ProductDetailsViewHolder)holder).onBind(products.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (products != null) {
            return products.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        Product product = products.get(position);
        switch (product.getName()) {
            case "LOADING...":
                return LOADING_TYPE;
            case "EXHAUSTED...":
                return EXHAUSTED_TYPE;
            default: {
                if (currentType == PRODUCTS_TYPE)
                    return PRODUCTS_TYPE;
                else
                    return PRODUCTS_DETAILS_TYPE;
            }
        }
    }

    public int changeLayout(){
        currentType = currentType == PRODUCTS_TYPE ? PRODUCTS_DETAILS_TYPE : PRODUCTS_TYPE ;
        notifyDataSetChanged();
        return currentType;
    }

    public void displayOnlyLoading() {
        clearList();
        Product product = new Product();
        product.setName("LOADING...");
        products.add(product);
        notifyDataSetChanged();
    }

    public void displayLoading() {
        if (products == null) {
            products = new ArrayList<>();
        }

        if (!isLoading()) {
            Product product = new Product();
            product.setName("LOADING...");
            products.add(product);
            notifyDataSetChanged();
        }
    }

    public void setExhausted() {
        hideLoading();
        Product product = new Product();
        product.setName("EXHAUSTED...");
        products.add(product);
        notifyDataSetChanged();
    }

    public void hideLoading() {
        if (isLoading()) {
            if (products.get(0).getName().equals("LOADING...")) {
                products.remove(0);
            } else if (products.get(products.size() - 1).getName().equals("LOADING...")) {
                products.remove(products.size() - 1);
            }
            notifyDataSetChanged();
        }
    }

    private boolean isLoading() {
        if (products != null) {
            if (products.size() > 0) {
                if (products.get(products.size() - 1).getName().equals("LOADING...")) {
                    return true;
                }
            }
        }

        return false;
    }

    private void clearList() {
        if (products == null) {
            products = new ArrayList<>();
        } else {
            products.clear();
        }
        notifyDataSetChanged();
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    public Product getProduct(int position) {
        if (products == null) {
            return null;
        }
        return products.get(position);
    }
}
