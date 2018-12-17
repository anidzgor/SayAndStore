package localization.nidzgor.com.voiceapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import localization.nidzgor.com.voiceapp.R;

public class RecyclerAdapterListNewCategories extends RecyclerView.Adapter<RecyclerAdapterListNewCategories.Viewholder> {

    private static final String TAG = "RecyclerViewCategory";

    private ArrayList<String> mNames;
    private ArrayList<Integer> mImageUrls;
    private Context mContext;

    List<CardView> cardViewList = new ArrayList<>();

    public interface SendImageListener {
        void sendImage(Integer image);
    }

    private SendImageListener sendImageListener;

    public RecyclerAdapterListNewCategories(Context context, ArrayList<String> mNames, ArrayList<Integer> mImageUrls, SendImageListener mSendImageListener) {
        this.mContext = context;
        this.mNames = mNames;
        this.mImageUrls = mImageUrls;
        this.sendImageListener = mSendImageListener;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_category_dialog_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");
        holder.textView.setText(mNames.get(position));
        holder.circleImageView.setImageResource(mImageUrls.get(position));
        holder.cardView.setCardBackgroundColor(Color.WHITE);
        cardViewList.add(holder.cardView);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on an image: " + mNames.get(position));
                Toast.makeText(mContext, mNames.get(position), Toast.LENGTH_SHORT).show();
                for(CardView cardView : cardViewList)
                    cardView.setBackgroundResource(R.drawable.object_reset_border);
                holder.cardView.setBackgroundResource(R.drawable.object_in_border);
                sendImageListener.sendImage(mImageUrls.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView circleImageView;
        TextView textView;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardViewCategoryItem);
            circleImageView = itemView.findViewById(R.id.image_category);
            textView = itemView.findViewById(R.id.category_name);
        }
    }
}
