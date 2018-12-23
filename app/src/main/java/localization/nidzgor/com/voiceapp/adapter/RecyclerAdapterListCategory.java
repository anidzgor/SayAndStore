package localization.nidzgor.com.voiceapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import localization.nidzgor.com.voiceapp.AppDatabase;
import localization.nidzgor.com.voiceapp.R;
import localization.nidzgor.com.voiceapp.activity.CategoryActivity;
import localization.nidzgor.com.voiceapp.dialog.DialogCategoryRemove;

public class RecyclerAdapterListCategory extends RecyclerView.Adapter<RecyclerAdapterListCategory.CategoryViewHolder> {

    private static final String TAG = "RecyclerAdapter";

    private List<String> mImageNames;
    private ArrayList<Integer> mResources;
    private Context mContext;
    private AppDatabase appDatabase;

    public RecyclerAdapterListCategory(Context mContext, List<String> mImageNames, ArrayList<Integer> mResources) {
        this.mImageNames = mImageNames;
        this.mResources = mResources;
        this.mContext = mContext;
        this.appDatabase = Room.databaseBuilder(mContext, AppDatabase.class, "categorydb").allowMainThreadQueries().build();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_category_item_layout, parent, false);
        CategoryViewHolder holder = new CategoryViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");

        holder.imageName.setText(mImageNames.get(position));
        holder.imageButton.setImageResource(mResources.get(position));

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: ");
                Intent intent = new Intent(mContext, CategoryActivity.class);
                intent.putExtra("categoryID", appDatabase.categoryDao().getCategoryIdBasedName(mImageNames.get(position)));
                mContext.startActivity(intent);
            }
        });

        holder.imageButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                DialogCategoryRemove dialogCategoryRemove = new DialogCategoryRemove();
                FragmentManager manager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                Bundle bundle = new Bundle();
                Integer id = appDatabase.categoryDao().getCategoryIdBasedName(mImageNames.get(position));
                bundle.putInt("ID", id);
                bundle.putInt("image", appDatabase.categoryDao().getImageById(id));
                dialogCategoryRemove.setArguments(bundle);
                dialogCategoryRemove.show(manager, "category_remove_dialog");
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageNames.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageButton imageButton;
        ListView listView;
        TextView imageName;
        RelativeLayout parentLayout;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            imageButton = itemView.findViewById(R.id.categoryItemButton);
            listView = itemView.findViewById(R.id.listViewSpecificCategory);
            imageName = itemView.findViewById(R.id.categoryItemText);
            parentLayout = itemView.findViewById(R.id.relative_layout_category_item);
        }
    }

}
