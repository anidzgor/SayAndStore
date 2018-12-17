package localization.nidzgor.com.voiceapp.category;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Category {

    @PrimaryKey(autoGenerate = true)
    private int ID;

    @ColumnInfo(name = "category_name")
    private String categoryName;

    private Integer resource;

    public Category(String categoryName, Integer resource) {
        this.categoryName = categoryName;
        this.resource = resource;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getResource() {
        return resource;
    }

    public void setResource(Integer resource) {
        this.resource = resource;
    }
}
