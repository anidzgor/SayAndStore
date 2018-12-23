package localization.nidzgor.com.voiceapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Room;
import localization.nidzgor.com.voiceapp.AppDatabase;
import localization.nidzgor.com.voiceapp.R;

public class AdapterListTask extends ArrayAdapter<String> {

    private static final String TAG = "AdapterListTask";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    private ArrayList<String> tasks;
    private Integer categoryID;

    private AppDatabase appDatabase;

    static class ViewHolder {
        TextView name;
    }

    public AdapterListTask(@NonNull Context context, int resource, @NonNull ArrayList<String> tasks, Integer categoryID) {
        super(context, resource, tasks);
        this.mContext = context;
        this.mResource = resource;
        this.tasks = tasks;
        this.categoryID = categoryID;
        appDatabase = Room.databaseBuilder(mContext, AppDatabase.class, "categorydb").allowMainThreadQueries().build();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position);

        final View result;
        ViewHolder holder;

        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.name = convertView.findViewById(R.id.textView);
            result = convertView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        result.startAnimation(animation);
        lastPosition = position;
        holder.name.setText(name);

        ImageButton deleteImageView = convertView.findViewById(R.id.removeButton);
        deleteImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ArrayList<String> tasksFromDatabase = (ArrayList<String>) appDatabase.taskDao().getTasksBelongToSpecificCategory(categoryID);
                String taskToRemove = tasksFromDatabase.get(position);
                tasksFromDatabase.remove(taskToRemove);
                appDatabase.taskDao().deleteSpecificTaskFromCategory(taskToRemove, categoryID);
                refreshViewAfterDelete();
            }
        });
        return convertView;
    }

    public void refreshViewAfterDelete() {
        this.tasks.clear();
        ArrayList<String> updatedTasks = (ArrayList<String>) appDatabase.taskDao().getTasksBelongToSpecificCategory(categoryID);
        this.tasks.addAll(updatedTasks);
        notifyDataSetChanged();
    }
}
