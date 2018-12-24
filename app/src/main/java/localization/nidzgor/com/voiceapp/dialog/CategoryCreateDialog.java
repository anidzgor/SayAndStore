package localization.nidzgor.com.voiceapp.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import localization.nidzgor.com.voiceapp.AppDatabase;
import localization.nidzgor.com.voiceapp.R;
import localization.nidzgor.com.voiceapp.adapter.RecyclerAdapterListNewCategories;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CategoryCreateDialog extends DialogFragment implements RecyclerAdapterListNewCategories.SendImageListener {

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Integer> mImageUrls = new ArrayList<>();
    private AppDatabase appDatabase;

    private Integer image;

    @Override
    public void sendImage(Integer image) {
        this.image = image;
    }

    public interface OnInputListener {
        void sendInput(String input, Integer image);
    }

    public OnInputListener mOnInputListener;

    private View view;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnInputListener = (OnInputListener) getActivity();
            appDatabase = Room.databaseBuilder(getActivity(), AppDatabase.class, "categorydb").allowMainThreadQueries().build();
        } catch (ClassCastException e) {
            Log.e(TAG,"onAttach: ClassCastException: " + e.getMessage());
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mImageUrls = this.getArguments().getIntegerArrayList("icons");
        mNames = this.getArguments().getStringArrayList("names");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_create_category, null);
        builder.setView(view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewForIcons);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerAdapterListNewCategories recyclerViewAdapterDialog = new RecyclerAdapterListNewCategories(this.getActivity(), mNames, mImageUrls, this);
        recyclerView.setAdapter(recyclerViewAdapterDialog);

        builder.setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                TextView editText = view.findViewById(R.id.new_category_name);
//                String nameOfCategory = editText.getText().toString();
//                Toast.makeText(getContext(), "Utworzono kategorie", Toast.LENGTH_LONG).show();
//                mOnInputListener.sendInput(nameOfCategory, image);
//                getDialog().dismiss();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        return builder.create();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        AlertDialog d = (AlertDialog)getDialog();
        if(d != null)
        {
            Button positiveButton = d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    TextView editText = view.findViewById(R.id.new_category_name);
                    String nameOfCategory = editText.getText().toString();
                    nameOfCategory = nameOfCategory.trim();
                    if(appDatabase.categoryDao().getCategoriesNames().contains(nameOfCategory)) {
                        Toast.makeText(getActivity(), R.string.category_occupied, Toast.LENGTH_SHORT).show();
                    } else if(nameOfCategory.isEmpty()) {
                        Toast.makeText(getActivity(), R.string.category_enter, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getContext(), R.string.category_created, Toast.LENGTH_LONG).show();
                        mOnInputListener.sendInput(nameOfCategory, image);
                        getDialog().dismiss();
                    }
                }
            });
        }
    }
}
