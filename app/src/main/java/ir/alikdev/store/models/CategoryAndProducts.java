package ir.alikdev.store.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class CategoryAndProducts {
    @Embedded
    public Category category;

     @Relation(parentColumn = "id" , entityColumn = "categories_id"  )
    public List<Product> products;

    @Override
    public String toString() {
        return "CategoryAndProducts{" +
                "category=" + category +
                ", products=" + products +
                '}';
    }
}
