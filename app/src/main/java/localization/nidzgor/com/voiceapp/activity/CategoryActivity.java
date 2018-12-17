package localization.nidzgor.com.voiceapp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;
import localization.nidzgor.com.voiceapp.AppDatabase;
import localization.nidzgor.com.voiceapp.R;
import localization.nidzgor.com.voiceapp.adapter.AdapterListTask;
import localization.nidzgor.com.voiceapp.task.Task;

public class CategoryActivity extends AppCompatActivity {

    private static final String TAG = "CategoryActivity";

    private Toolbar toolbar;
    private ListView listView;
    private ImageButton removeItemButton;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private SpeechRecognizer mSpeechRecognizer;
    private Intent mSpeechRecognizerIntent;

    private ArrayList<String> tasks = null;
    private AdapterListTask adapter = null;
    private AppDatabase appDatabase;

    private Integer categoryID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Log.d(TAG, "onCreate: started.");

        listView = findViewById(R.id.listViewSpecificCategory);
        removeItemButton = findViewById(R.id.removeButton);

        categoryID = getIntent().getIntExtra("categoryID", 1);
        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "categorydb").allowMainThreadQueries().build();
        this.tasks = (ArrayList<String>) appDatabase.taskDao().getTasksBelongToSpecificCategory(categoryID);

        adapter = new AdapterListTask(this, R.layout.adapter_view_layout, this.tasks, categoryID);
        listView.setAdapter(adapter);

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(appDatabase.categoryDao().getCategoryNameBasedId(categoryID));

        //checkPermission();
        verifyPermissions();

        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
            }

            @Override
            public void onBeginningOfSpeech() {
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {
            }

            @Override
            public void onEndOfSpeech() {
            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                appDatabase.taskDao().addTask(new Task(matches.get(0), categoryID));
                refreshView();
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        findViewById(R.id.buttonSpeak).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer.stopListening();
                        mSpeechRecognizer.cancel();
                        break;
                    case MotionEvent.ACTION_DOWN:
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        break;
                }
                return CategoryActivity.super.onTouchEvent(motionEvent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mSpeechRecognizer != null)
            mSpeechRecognizer.destroy();
    }

    private void refreshView() {
        this.tasks.clear();
        ArrayList<String> updatedTasks = (ArrayList<String>) appDatabase.taskDao().getTasksBelongToSpecificCategory(categoryID);
        this.tasks.addAll(updatedTasks);
        adapter.notifyDataSetChanged();
    }


    private void verifyPermissions() {
        String [] permissions = {Manifest.permission.RECORD_AUDIO};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(CategoryActivity.this,
                    permissions,
                    REQUEST_RECORD_AUDIO_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        verifyPermissions();
    }
}
