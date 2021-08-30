package ir.alikdev.store.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "product")
public class Product implements Parcelable {

    @PrimaryKey
    @NonNull
    private int id;

    @ColumnInfo(name = "categories_id")
    private int categories_id;

    @ColumnInfo(name = "name")
    private String name;

    // do not use float and double data type as currency instead use BigDecimal
    @ColumnInfo(name = "price")
    private long price;

    @ColumnInfo(name = "image_url")
    private String image_url;

    @ColumnInfo(name = "rate")
    private float rate;

    @ColumnInfo(name = "create_at")
    private String create_at;

    public Product(int id, int categories_id, String name, long price, String image_url, float rate, String create_at) {
        this.id = id;
        this.categories_id = categories_id;
        this.name = name;
        this.price = price;
        this.image_url = image_url;
        this.rate = rate;
        this.create_at = create_at;
    }

    @Ignore
    public Product() {
    }

    protected Product(Parcel in) {
        id = in.readInt();
        categories_id = in.readInt();
        name = in.readString();
        price = in.readLong();
        image_url = in.readString();
        rate = in.readFloat();
        create_at = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(categories_id);
        parcel.writeString(name);
        parcel.writeLong(price);
        parcel.writeString(image_url);
        parcel.writeFloat(rate);
        parcel.writeString(create_at);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategories_id() {
        return categories_id;
    }

    public void setCategories_id(int categories_id) {
        this.categories_id = categories_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", categories_id=" + categories_id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", image_url='" + image_url + '\'' +
                ", rate=" + rate +
                ", create_at='" + create_at + '\'' +
                '}';
    }


}
