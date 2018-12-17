package localization.nidzgor.com.voiceapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;

import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import localization.nidzgor.com.voiceapp.XmlUtils.XmlConverter;

public class ShoppingList extends AppCompatActivity {

    private static final String TAG = "ShoppingListActivity";

    private Toolbar toolbar;

    ListView listView;
    ImageButton removeItemButton;

    SpeechRecognizer mSpeechRecognizer;
    Intent mSpeechRecognizerIntent;

    ArrayList<Product> products = null;
    PersonListAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        Log.d(TAG, "onCreate: Started");

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Lista zakupów");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        checkPermission();

        listView = findViewById(R.id.listView);
        removeItemButton = findViewById(R.id.removeButton);

        try {
            products = XmlConverter.readDataFromXmlFile(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        adapter = new PersonListAdapter(this, R.layout.adapter_view_layout, products);
        listView.setAdapter(adapter);

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
                try {
                    ArrayList<Product> productsFromFile = addProduct(matches);
                    refresh(productsFromFile);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
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
                        break;
                    case MotionEvent.ACTION_DOWN:
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        break;
                }
                return ShoppingList.super.onTouchEvent(motionEvent);
            }
        });
    }

    public void refresh(ArrayList<Product> productsToWrite)
    {
        this.products.clear();
        ArrayList<Product> newList = productsToWrite;
        this.products.addAll(newList);
        adapter.notifyDataSetChanged();
    }

    private ArrayList<Product> addProduct(ArrayList<String> matches) throws IOException, XmlPullParserException {
        ArrayList<Product> productsfromFile = XmlConverter.readDataFromXmlFile(getApplicationContext());
        productsfromFile.add(new Product(matches.get(0)));
        XmlConverter.saveDataToXmlFile(productsfromFile, getApplicationContext().getFilesDir());

        return productsfromFile;
    }

    private void checkPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        }
    }


}
