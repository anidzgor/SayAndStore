package localization.nidzgor.com.voiceapp.task;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import localization.nidzgor.com.voiceapp.category.Category;

@Entity(foreignKeys = @ForeignKey(entity = Category.class,
        parentColumns = "ID",
        childColumns = "category_id",
        onDelete = ForeignKey.CASCADE))
public class Task {

    @PrimaryKey(autoGenerate = true)
    private int ID;

    @ColumnInfo(name = "task_name")
    private String taskName;

    @ColumnInfo(name = "category_id")
    private int categoryID;

    public Task(String taskName, int categoryID) {
        this.taskName = taskName;
        this.categoryID = categoryID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }
}
