package ir.alikdev.store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import ir.alikdev.store.R;
import ir.alikdev.store.models.Category;
import ir.alikdev.store.models.CategoryAndProducts;
import ir.alikdev.store.models.CategoryProducts;
import ir.alikdev.store.models.Product;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private LinearLayout layout;
    private TextView view_all;
    private TextView product_type;
    private View view;
    private RequestManager requestManager;
    private OnCategoryClickListener listener;

    public CategoryViewHolder(@NonNull View itemView,RequestManager requestManager,OnCategoryClickListener listener) {
        super(itemView);
        layout = itemView.findViewById(R.id.category_item_layout);
        view_all = itemView.findViewById(R.id.view_all);
        product_type = itemView.findViewById(R.id.product_type);
        this.requestManager = requestManager;
        this.listener = listener;
        view_all.setOnClickListener(this);
        layout.setOnClickListener(this);
    }

    public void onBind(CategoryAndProducts categoryAndProducts){
        layout.removeAllViews();
        Category category= categoryAndProducts.category;
        List<Product> products = categoryAndProducts.products;

        product_type.setText(category.getName());

        for (Product product:products
             ) {
            view = LayoutInflater.from(itemView.getContext()).inflate(R.layout.product_item,layout , false);
            AppCompatImageView image = view.findViewById(R.id.product_image);
            TextView name = view.findViewById(R.id.name);
            TextView price = view.findViewById(R.id.price);
            TextView rate = view.findViewById(R.id.rate);

            setImage(product.getImage_url(),image);
            name.setText(product.getName());
            /**
             1) persian currency is rial and  its need long type so i don't need decimal
             2)but in case you are using dollar or other currency simply use this format "%1$,.2f"
             example1:  1200.23 its convert to 1,200 with this format "%1$,.0f"
             example2:  1200.23 its convert to 1,200.23 with this format "%1$,.2f"
             **/
            price.setText(String.format("%1$,d",product.getPrice()));
            rate.setText(String.valueOf(product.getRate()));

            layout.addView(view);
        }


    }

    private void setImage(String url , AppCompatImageView image){
       requestManager
                .load(url)
                .into(image);
    }

    @Override
    public void onClick(View view) {
        //right now onItemClick and onAllClick they do a same work but we can use them on different purpose
        switch (view.getId()){
            case R.id.category_item_layout:
                listener.onItemClick(getAdapterPosition());
                break;
            case R.id.view_all:
                listener.onAllClick(getAdapterPosition());
                break;

        }

    }
}
