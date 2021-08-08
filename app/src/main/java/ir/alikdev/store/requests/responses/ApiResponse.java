package ir.alikdev.store.requests.responses;

import android.util.Log;

import java.io.IOException;

import retrofit2.Response;

import static ir.alikdev.store.viewmodels.CategoryProductsViewModel.EXHAUSTED;

public class ApiResponse<T> {

    private static final String TAG = "ApiResponse";
    public ApiResponse<T> create(Throwable error) {
        return new ApiErrorResponse<T>(!error.getMessage().equals("") ? error.getMessage() : "Unknown error\nCheck NetWork connection");
    }

    public ApiResponse<T> create(Response<T> response){

        if(response.isSuccessful()){
            T body =response.body();

            if(body instanceof CategoriesResponse){

                if(((CategoriesResponse) body).getCategoryProducts() != null &&
                        ((CategoriesResponse) body).getCategoryProducts().size() == 0){
                    // query is exhausted
                    return new ApiErrorResponse<>(EXHAUSTED);
                }
            }else if(body instanceof ProductsSearchResponse){

                if(((ProductsSearchResponse) body).getProducts() != null &&
                        ((ProductsSearchResponse) body).getProducts().size() == 0){
                    // query is exhausted
                    return new ApiErrorResponse<>(EXHAUSTED);
                }
            }

            if(body == null || response.code() == 204){// 204 mean empty body
                return new ApiEmptyResponse<>();
            }else {
                return new ApiSuccessResponse<>(body);
            }
        }else{
            String errorMessage = "";
            try {
                errorMessage = response.errorBody().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new ApiErrorResponse<>(errorMessage);
        }
    }

    public class ApiSuccessResponse<T> extends ApiResponse<T> {

        private T body;

        public ApiSuccessResponse(T body) {
            this.body = body;
        }

        public T getBody() {
            return body;
        }
    }

    public class ApiErrorResponse<T> extends ApiResponse<T> {
        private String errorMessage;

        public ApiErrorResponse(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

    }

    public class ApiEmptyResponse<T> extends ApiResponse<T> {
    }

    ;
}
