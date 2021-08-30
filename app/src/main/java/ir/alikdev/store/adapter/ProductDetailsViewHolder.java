package ir.alikdev.store.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;

import ir.alikdev.store.R;
import ir.alikdev.store.models.Product;

public class ProductDetailsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    private AppCompatImageView image ;
    private TextView name;
    private TextView price;
    private TextView rate;
    private TextView create_at;
    private RequestManager requestManager;
    private OnProductListener listener;

    public ProductDetailsViewHolder(@NonNull View itemView, RequestManager requestManager, OnProductListener listener) {
        super(itemView);
        this.requestManager = requestManager;
        this.listener = listener;

        image = itemView.findViewById(R.id.product_image);
        name = itemView.findViewById(R.id.name);
        price = itemView.findViewById(R.id.price);
        rate = itemView.findViewById(R.id.rate);
        create_at = itemView.findViewById(R.id.create_at);

        this.requestManager = requestManager;

        itemView.setOnClickListener(this);
    }

    public void onBind(Product product){
        requestManager.load(product.getImage_url())
                .into(image);

        name.setText(product.getName());

        /**
         1) persian currency is rial and  its need long type so i don't need decimal
         2)but in case you are using dollar or other currency simply use this format "%1$,.2f"
         example1:  1200.23 its convert to 1,200 with this format "%1$,.0f"
         example2:  1200.23 its convert to 1,200.23 with this format "%1$,.2f"
         **/
        price.setText(String.format("%1$,d",product.getPrice()));
        rate.setText(String.valueOf(product.getRate()));
        create_at.setText(product.getCreate_at());
    }

    @Override
    public void onClick(View view) {
        listener.OnProductClick(getAdapterPosition());
    }
}
