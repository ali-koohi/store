package ir.alikdev.store.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ir.alikdev.store.AppExecutors;
import ir.alikdev.store.models.Product;
import ir.alikdev.store.repositories.StoreRepository;
import ir.alikdev.store.requests.ServiceGenerator;
import ir.alikdev.store.requests.responses.ApiResponse;
import ir.alikdev.store.requests.responses.ProductResponse;
import ir.alikdev.store.util.NetworkBoundResource;
import ir.alikdev.store.util.Resource;

public class ProductViewModel extends AndroidViewModel {

    private StoreRepository repository;

    public ProductViewModel(@NonNull Application application) {
        super(application);

        repository = StoreRepository.getInstance(application);

    }

    public LiveData<Resource<Product>> getProduct(int id){
        return repository.getProduct(id);
    }

}
