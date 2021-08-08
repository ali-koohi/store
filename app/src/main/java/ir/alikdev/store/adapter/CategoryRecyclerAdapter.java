package ir.alikdev.store.adapter;

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

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int CATEGORY_PRODUCTS_TYPE = 1;
    private static final int LOADING_TYPE = 2;
    private static final int EXHAUSTED_TYPE = 3;

    private List<CategoryAndProducts> categoryAndProducts;
    private RequestManager requestManager;
    private OnCategoryClickListener listener;

    public CategoryRecyclerAdapter(RequestManager requestManager , OnCategoryClickListener listener) {
        this.requestManager = requestManager;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case CATEGORY_PRODUCTS_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);;
                return new CategoryViewHolder(view,requestManager,listener);
            case LOADING_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_list_item,  parent, false);
                return new LoadingViewHolder(view);
            case EXHAUSTED_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_exhausted_list_item,  parent, false);
                return new ExhaustedViewHolder(view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,  parent, false);
                return new CategoryViewHolder(view,requestManager,listener);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        if(itemViewType == CATEGORY_PRODUCTS_TYPE){
            CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;
            categoryViewHolder.onBind(categoryAndProducts.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (categoryAndProducts != null) {
            return categoryAndProducts.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        CategoryAndProducts cProducts = categoryAndProducts.get(position);

        if (cProducts.category.getName().equals("LOADING...")) {
            return LOADING_TYPE;
        } else if (cProducts.category.getName().equals("EXHAUSTED...")) {
            return EXHAUSTED_TYPE;
        } else {
            return CATEGORY_PRODUCTS_TYPE;
        }
    }

    public void setCategoryAndProducts(List<CategoryAndProducts> categoryAndProducts) {
        this.categoryAndProducts = categoryAndProducts;
        notifyDataSetChanged();
    }

    public void displayOnlyLoading(){
        clearList();
        CategoryAndProducts cProducts = new CategoryAndProducts();
        cProducts.category=new Category();
        cProducts.products=new ArrayList<>();
        cProducts.category.setName("LOADING...");
        categoryAndProducts.add(cProducts);
        notifyDataSetChanged();
    }

    public void setExhausted(){
        hideLoading();
        CategoryAndProducts cProducts=new CategoryAndProducts();
        cProducts.category=new Category();
        cProducts.products=new ArrayList<>();
        cProducts.category.setName("EXHAUSTED...");
        categoryAndProducts.add(cProducts);
        notifyDataSetChanged();
    }

    public void hideLoading(){
        if(isLoading()){
            if(categoryAndProducts.get(0).category.getName().equals("LOADING...")){
                categoryAndProducts.remove(0);
            }else if(categoryAndProducts.get(categoryAndProducts.size() - 1).category.getName().equals("LOADING...")){
                categoryAndProducts.remove(categoryAndProducts.size() - 1);
            }
            notifyDataSetChanged();
        }
    }

    public void displayLoading(){
        if(categoryAndProducts == null){
            categoryAndProducts = new ArrayList<>();
        }

        if(!isLoading()){
            CategoryAndProducts cProducts = new CategoryAndProducts();
            cProducts.category=new Category();
            cProducts.products=new ArrayList<>();
            cProducts.category.setName("LOADING...");
            categoryAndProducts.add(cProducts);
            notifyDataSetChanged();
        }
    }

    private void clearList() {
        if (categoryAndProducts == null) {
            categoryAndProducts = new ArrayList<>();
        } else {
            categoryAndProducts.clear();
        }
        notifyDataSetChanged();
    }

    private boolean isLoading() {
        if(categoryAndProducts != null){
            if(categoryAndProducts.size() > 0){
                if(categoryAndProducts.get(categoryAndProducts.size() - 1).category.getName().equals("LOADING...")){
                    return true;
                }
            }
        }
        return false;
    }

    public Category getCategory(int position){
        return categoryAndProducts.get(position).category;
    }
}
