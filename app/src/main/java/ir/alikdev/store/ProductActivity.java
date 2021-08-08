package ir.alikdev.store;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import ir.alikdev.store.models.Product;
import ir.alikdev.store.util.Resource;
import ir.alikdev.store.viewmodels.ProductViewModel;

import static ir.alikdev.store.util.Resource.Status.*;

public class ProductActivity extends BaseActivity {

    private static final String TAG = "ProductActivity";

    private AppCompatImageView image;
    private TextView name;
    private TextView price;
    private TextView rate;
    private TextView create_at;

    private ProductViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        image = findViewById(R.id.image);
        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        rate = findViewById(R.id.rate);
        create_at = findViewById(R.id.create_at);

        viewModel = ViewModelProviders.of(this).get(ProductViewModel.class);

        init();
    }

    private void init(){
        if(getIntent().hasExtra("product")){
            Product product = getIntent().getParcelableExtra("product");
            if(product != null){
                getProduct(product.getId());
            }
        }
    }

    private void getProduct(int id){
        viewModel.getProduct(id).observe(ProductActivity.this, new Observer<Resource<Product>>() {
            @Override
            public void onChanged(Resource<Product> productResource) {
                if(productResource != null){
                    if(productResource.data != null){
                        switch (productResource.status){

                            case LOADING:{
                                showProgressBar(true);
                                break;
                            }

                            case ERROR:{
                                Log.e(TAG, "onChanged: status: ERROR, Product: " + productResource.data.getName() );
                                Log.e(TAG, "onChanged: ERROR message: " + productResource.message );
                                showProgressBar(false);
                                setProperties(productResource.data);
                                break;
                            }

                            case SUCCESS:{
                                Log.d(TAG, "onChanged: cache has been refreshed.");
                                Log.d(TAG, "onChanged: status: SUCCESS, Product: " + productResource.data.getName());
                                showProgressBar(false);
                                setProperties(productResource.data);
                                break;
                            }
                        }
                    }
                }
            }
        });
    }

    private void setProperties(Product product){
        if(product != null){
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.white_background)
                    .error(R.drawable.white_background);

            Glide.with(this)
                    .setDefaultRequestOptions(requestOptions)
                    .load(product.getImage_url())
                    .into(image);

            name.setText(product.getName());
            price.setText(String.valueOf(product.getPrice()));
            rate.setText(String.valueOf(product.getRate()));
            create_at.setText(product.getCreate_at());
        }
    }

}