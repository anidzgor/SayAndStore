package localization.nidzgor.com.voiceapp.category;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface CategoryDao {

    @Insert
    void addCategory(Category category);

    @Query("select category_name from Category where ID = :categoryID")
    String getCategoryNameBasedId(int categoryID);

    @Query("select category_name from Category")
    List<String> getCategoriesNames();

    @Query("select resource from Category")
    List<Integer> getResources();

    @Query("select ID from Category where category_name = :name")
    Integer getCategoryIdBasedName(String name);

    @Query("select resource from Category where ID = :id")
    Integer getImageById(Integer id);

    @Query("delete from Category where ID = :id")
    void deleteCategoryById(Integer id);
}
