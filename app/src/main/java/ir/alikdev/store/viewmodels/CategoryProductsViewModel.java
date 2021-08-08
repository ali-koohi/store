package ir.alikdev.store.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import ir.alikdev.store.models.CategoryAndProducts;
import ir.alikdev.store.repositories.StoreRepository;
import ir.alikdev.store.util.Resource;

public class CategoryProductsViewModel extends AndroidViewModel {

    private static final String TAG = "CategoryProductsViewMod";

    public static final String EXHAUSTED = "No more results.";

    private MediatorLiveData<Resource<List<CategoryAndProducts>>> categoryAndProducts =new MediatorLiveData<>();

    private boolean isExhausted;
    private boolean isPerforming;
    private int page;
    private boolean cancelRequest;

    private StoreRepository repository;
    public CategoryProductsViewModel(@NonNull Application application) {
        super(application);
        repository = StoreRepository.getInstance(application);
    }

    public LiveData<Resource<List<CategoryAndProducts>>> getCategoryAndProducts() {
        return categoryAndProducts;
    }

    public int getPage() {
        return page;
    }

    public void getCategoriesProducts(int pageNumber){
        if(!isPerforming){
            if(pageNumber == 0){
                pageNumber = 1;
            }
            this.page = pageNumber;
            isExhausted = false;
            executeSearch();
        }
    }

    public void nextPage(){
        if(!isExhausted && !isPerforming){
            page++;
            executeSearch();
        }
    }

    private void executeSearch(){
        isPerforming = true;
        cancelRequest = false;

        final LiveData<Resource<List<CategoryAndProducts>>> repositorySource = repository.getCategoriesProducts(page);
        categoryAndProducts.addSource(repositorySource, new Observer<Resource<List<CategoryAndProducts>>>() {
            @Override
            public void onChanged(Resource<List<CategoryAndProducts>> listResource) {
                if(!cancelRequest){
                    if(listResource != null){

                        categoryAndProducts.setValue(listResource);
                        if(listResource.status == Resource.Status.SUCCESS ){
                            isPerforming = false;

                            if(listResource.data != null) {
                                if (listResource.data.size() == 0) {
                                    Log.d(TAG, "onChanged: query is EXHAUSTED...");
                                    categoryAndProducts.setValue(new Resource<List<CategoryAndProducts>>(
                                            Resource.Status.ERROR,
                                            listResource.data,
                                            EXHAUSTED
                                    ));

                                    isExhausted = true;
                                }
                            }
                            // must remove or it will keep listening to repository
                            categoryAndProducts.removeSource(repositorySource);
                        }
                        else if(listResource.status == Resource.Status.ERROR ){
                            isPerforming = false;

                            if(listResource.message.equals(EXHAUSTED)){
                                isExhausted = true;
                            }else{
                                //if network was not success and was not exhausted then page must get back to previous page
                                page -- ;
                            }
                            categoryAndProducts.removeSource(repositorySource);
                        }
                        //categoryAndProducts.setValue(listResource);
                    }
                else{
                        categoryAndProducts.removeSource(repositorySource);
                    }
                }else{
                    categoryAndProducts.removeSource(repositorySource);
                }
            }
        });
    }

    public void cancelRequest(){
        if(isPerforming){
            Log.d(TAG, "cancelRequest: ");
            isPerforming = false;
            cancelRequest = true;
            page = 1;
        }
    }
}
