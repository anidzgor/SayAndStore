package localization.nidzgor.com.voiceapp.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.room.Room;
import localization.nidzgor.com.voiceapp.AppDatabase;
import localization.nidzgor.com.voiceapp.R;

public class CategoryRemoveDialog extends DialogFragment {

    private TextView mActionOk;
    private TextView mActionCancel;
    private ImageView imageView;

    private AppDatabase appDatabase;

    public interface DeleteListener {
        void update();
    }

    DeleteListener deleteListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        deleteListener = (DeleteListener) getActivity();
        View view = inflater.inflate(R.layout.category_remove_dialog, container, false);
        mActionCancel = view.findViewById(R.id.back_to_main);
        mActionOk = view.findViewById(R.id.remove_category);
        imageView = view.findViewById(R.id.category_to_remove);
        imageView.setImageResource((Integer) getArguments().get("image"));
        appDatabase = Room.databaseBuilder(getContext(), AppDatabase.class, "categorydb").allowMainThreadQueries().build();
        mActionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer categoryID = (Integer) getArguments().get("ID");
                appDatabase.categoryDao().deleteCategoryById(categoryID);
                deleteListener.update();
                getDialog().dismiss();
            }
        });

        mActionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        return view;
    }
}
