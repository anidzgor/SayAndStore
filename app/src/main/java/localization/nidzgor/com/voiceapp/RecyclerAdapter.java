package localization.nidzgor.com.voiceapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.CategoryViewHolder> {

    private static final String TAG = "RecyclerAdapter";

    private ArrayList<String> mImageNames = new ArrayList<>();
    private int[] mResources;
    private Context mContext;

    public RecyclerAdapter(Context mContext, ArrayList<String> mImageNames, int[] mResources) {
        this.mImageNames = mImageNames;
        this.mResources = mResources;
        this.mContext = mContext;
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

        holder.imageButton.setImageResource(mResources[position]);
        holder.imageName.setText(mImageNames.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: ");
                Toast.makeText(mContext, mImageNames.get(position), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(mContext, CategoryActivity.class);
                intent.putExtra("image_url", mResources[position]);
                intent.putExtra("image_name", mImageNames.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageNames.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        ImageButton imageButton;
        TextView imageName;
        RelativeLayout parentLayout;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            imageButton = itemView.findViewById(R.id.categoryItemButton);
            imageName = itemView.findViewById(R.id.categoryItemText);
            parentLayout = itemView.findViewById(R.id.relative_layout_category_item);
        }
    }

}
