package localization.nidzgor.com.voiceapp;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import localization.nidzgor.com.voiceapp.category.Category;
import localization.nidzgor.com.voiceapp.category.CategoryDao;
import localization.nidzgor.com.voiceapp.task.Task;
import localization.nidzgor.com.voiceapp.task.TaskDao;

@Database(entities = {Category.class, Task.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CategoryDao categoryDao();
    public abstract TaskDao taskDao();
}
