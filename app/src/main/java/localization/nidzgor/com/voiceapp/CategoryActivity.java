package localization.nidzgor.com.voiceapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

public class CategoryActivity extends AppCompatActivity {

    private static final String TAG = "CategoryActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Log.d(TAG, "onCreate: started.");

        getIncomingIntent();
    }

    private void getIncomingIntent() {
        Log.d(TAG, "getIncomingIntent: checking for incoming intents");
        if(getIntent().hasExtra("image_url") && getIntent().hasExtra("image_name")) {
            Log.d(TAG, "getIncomingIntent: found intent extras");

            int imageUrl = getIntent().getIntExtra("image_url", 0);
            String imageName = getIntent().getStringExtra("image_name");

            setImage(imageUrl, imageName);
        }
    }

    private void setImage(int imageUrl, String imageName) {
        Log.d(TAG, "setImage: setting the image and name to widgets");
        TextView name = findViewById(R.id.image_description);
        name.setText(imageName);

        ImageButton imageButton = findViewById(R.id.imageCategory);
        imageButton.setImageResource(imageUrl);
    }
}
