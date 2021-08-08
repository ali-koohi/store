package ir.alikdev.store.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import ir.alikdev.store.models.Category;
import ir.alikdev.store.models.CategoryAndProducts;
import ir.alikdev.store.models.Product;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface StoreDao {

    @Insert(onConflict = REPLACE)
    long[] insertCategories(Category... categories);

    @Insert(onConflict = REPLACE)
    void insertCategory(Category category);

    @Transaction
    @Query("select * from category order by id limit (:page * 4)")
    LiveData<List<CategoryAndProducts>> getCategories(int page);

//    @Transaction
//    @Query("select c.* " +
//            ", p.id as P_id" +
//            ", p.categories_id as P_categories_id" +
//            ", p.name as P_name" +
//            ", p.price as P_price" +
//            ", p.image_url as P_image_url" +
//            ", p.rate as P_rate" +
//            ", p.create_at as P_create_at" +
//            " from category as c join product as p on c.id = p.categories_id order by id limit (:page * 4)")
//    LiveData<List<CategoryAndProducts>> getCategories(int page);

    @Insert(onConflict = REPLACE)
    long[] insertProducts(Product... products);

    @Insert(onConflict = REPLACE)
    void insertProduct(Product product);

    @Query("select * from product where categories_id =:categories_id")
    LiveData<List<Product>> getProducts(int categories_id);

    @Query("select * from product where categories_id =:categories_id and name like '%' || :query || '%' order by rate desc ")
    LiveData<List<Product>> getProducts(int categories_id,String query);

    @Query("select * from product where id =:id")
    LiveData<Product> getProduct(int id);


}
