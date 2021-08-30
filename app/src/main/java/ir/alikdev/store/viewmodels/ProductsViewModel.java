package ir.alikdev.store.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import ir.alikdev.store.models.Product;
import ir.alikdev.store.repositories.StoreRepository;
import ir.alikdev.store.util.Resource;

import static ir.alikdev.store.viewmodels.CategoryProductsViewModel.EXHAUSTED;

public class ProductsViewModel extends AndroidViewModel {

    private static final String TAG = "ProductsViewModel";

    private StoreRepository repository;

    private CompositeDisposable disposable = new CompositeDisposable();

    private MediatorLiveData<Resource<List<Product>>> products=new MediatorLiveData<>();

    private int mCategoryId;
    private int mPage;
    private boolean isPerformingQuery;
    private boolean isExhausted;
    private String mQuery;
    private boolean cancelRequest;

    public ProductsViewModel(@NonNull Application application) {
        super(application);
        repository = StoreRepository.getInstance(application);
    }

    public LiveData<Resource<List<Product>>> getProducts(){
        return products;
    }

    public void setCategoryId(int mCategoryId) {
        this.mCategoryId = mCategoryId;
    }

    public int getCategoryId() {
        return mCategoryId;
    }

    public int getPage() {
        return mPage;
    }

    public void searchProductsOfCategory(int page , int category_id , String query){
       if(!isPerformingQuery){
           if(page == 0){
               page = 1;
           }
           this.mPage = page;
           mCategoryId = category_id;
           isExhausted = false;
           mQuery = query;
           executeSearch();
       }
    }

    public void nextPage(){
        if(!isPerformingQuery && !isExhausted){
            mPage++;
            executeSearch();
        }
    }

    private void executeSearch(){
        isPerformingQuery = true;
        cancelRequest = false;

        LiveData<Resource<List<Product>>> dbResource=repository.searchProductsOfCategory(mPage, mCategoryId, mQuery);
        products.addSource(dbResource, new Observer<Resource<List<Product>>>() {
            @Override
            public void onChanged(Resource<List<Product>> listResource) {
                if(!cancelRequest){

                    products.setValue(listResource);

                    if(listResource.status.equals(Resource.Status.SUCCESS)){
                        isPerformingQuery = false;

                        if(listResource.data != null){
                            if(listResource.data.size() == 0){
                                products.setValue(new Resource<>(
                                        Resource.Status.ERROR,
                                        listResource.data,
                                        EXHAUSTED
                                ));

                                isExhausted = true;
                            }
                        }
                        products.removeSource(dbResource);
                    }else if(listResource.status.equals(Resource.Status.ERROR)){
                        isPerformingQuery = false;

                        if(listResource.message.equals(EXHAUSTED)){
                            isExhausted = true;
                        }else{
                            //if network was not success and was not exhausted then page must get back to previous page
                            mPage -- ;
                        }

                        products.removeSource(dbResource);
                    }

                }else{
                    products.removeSource(dbResource);
                }
            }
        });
    }

    public void cancelRequest(){
        if(isPerformingQuery){
            Log.d(TAG, "cancelRequest: ");
            isPerformingQuery = false;
            cancelRequest = true;
            mPage = 1;
        }
    }

    public void addDisposable(Disposable d){
        disposable.add(d);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
