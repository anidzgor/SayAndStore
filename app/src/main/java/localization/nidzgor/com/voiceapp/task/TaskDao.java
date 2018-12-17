package localization.nidzgor.com.voiceapp.task;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface TaskDao {

    @Insert
    void addTask(Task task);

    @Query("select task_name from Task")
    List<String> getTasksName();

    @Query("select task_name from Task where category_id = :categoryID")
    List<String> getTasksBelongToSpecificCategory(int categoryID);

    @Query("delete from task where task_name = :taskName and category_id = :categoryID")
    void deleteSpecificTaskFromCategory(String taskName, int categoryID);
}
