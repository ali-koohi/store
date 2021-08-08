package ir.alikdev.store.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ir.alikdev.store.models.Category;
import ir.alikdev.store.models.Product;

@Database(entities = {Category.class, Product.class}, version = 1)
public abstract class StoreDatabase extends RoomDatabase {

    public final static String DATABASE_NAME = "store";

    private static StoreDatabase instance;

    public static StoreDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    StoreDatabase.class,
                    DATABASE_NAME
            ).build();
        }
        return instance;
    }

    public abstract StoreDao getStoreDao();
}
