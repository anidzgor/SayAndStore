package localization.nidzgor.com.voiceapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import localization.nidzgor.com.voiceapp.AppDatabase;
import localization.nidzgor.com.voiceapp.adapter.RecyclerAdapterListCategory;
import localization.nidzgor.com.voiceapp.dialog.AboutDialog;
import localization.nidzgor.com.voiceapp.dialog.CategoryCreateDialog;
import localization.nidzgor.com.voiceapp.R;
import localization.nidzgor.com.voiceapp.category.Category;
import localization.nidzgor.com.voiceapp.dialog.CategoryRemoveDialog;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MainActivity extends AppCompatActivity implements CategoryCreateDialog.OnInputListener,
        CategoryRemoveDialog.DeleteListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private RecyclerView recyclerView;
    private RecyclerAdapterListCategory recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<String> mNames;
    private ArrayList<Integer> mResources = new ArrayList<>();

    public static AppDatabase appDatabase;

    @Override
    public void sendInput(String category, Integer image) {
        Log.d(TAG, "sendInput: got the input: " + category);
        saveCategoryToDatabase(category, image);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "categorydb").allowMainThreadQueries().build();

        mNames = appDatabase.categoryDao().getCategoriesNames();
        mResources = (ArrayList<Integer>) appDatabase.categoryDao().getResources();

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new RecyclerAdapterListCategory(this, mNames, mResources);
        recyclerView.setAdapter(recyclerAdapter);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.settings_button:
                        item.setChecked(true);
                        //displayMessage("Settings selected...");
                        drawerLayout.closeDrawers();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_status_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.add_button:
                try {
                    createNewCategory(item);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.settings_button:
                return true;
            case R.id.about_app:
                AboutDialog aboutDialog = new AboutDialog();
                aboutDialog.show(getSupportFragmentManager(), "AboutDialog");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void createNewCategory(MenuItem item) throws IllegalAccessException {
        CategoryCreateDialog categoryDialog = new CategoryCreateDialog();
        Bundle bundle = new Bundle();

        Field[] fields = R.mipmap.class.getFields();
        HashMap<String, String> icons = getAllowedIcons();
        ArrayList<String> iconsNames = new ArrayList<>();
        ArrayList<Integer> iconsImage = new ArrayList<>();

        for(Field field: fields) {
            if (icons.containsValue(field.getName())) {
                iconsImage.add(field.getInt(null));
                iconsNames.add((String) getKeyFromValue(icons, field.getName()));
            }
        }

        bundle.putIntegerArrayList("icons", iconsImage);
        bundle.putStringArrayList("names", iconsNames);
        categoryDialog.setArguments(bundle);
        categoryDialog.show(getSupportFragmentManager(), "myCustomDialog");
    }

    public static Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet())
            if (hm.get(o).equals(value))
                return o;
        return null;
    }

    public void saveCategoryToDatabase(String categoryName, Integer image) {
        if(image == null)
            image = R.mipmap.ic_default_list;
        Category category = new Category(categoryName, image);
        appDatabase.categoryDao().addCategory(category);
        mNames.add(categoryName);
        mResources.add(image);
        recyclerAdapter.notifyItemInserted(mNames.size() - 1);
        Toast.makeText(this, "Category created", Toast.LENGTH_LONG).show();
    }

    private HashMap<String, String> getAllowedIcons() {
        HashMap<String, String> icons = new HashMap<>();
        icons.put("Travel", "ic_world");
        icons.put("Cinema", "ic_cinema");
        icons.put("Films", "ic_movie");
        icons.put("Shopping", "ic_shop");
        icons.put("Basket", "ic_basket");
        icons.put("Flights", "ic_plane");
        icons.put("List", "ic_default_list");
        icons.put("Accounts", "ic_coins");
        return icons;
    }

    @Override
    public void update() {
        mNames.clear();
        mResources.clear();
        ArrayList<String> categoryFromDatabase = (ArrayList<String>) appDatabase.categoryDao().getCategoriesNames();
        ArrayList<Integer> resources = (ArrayList<Integer>) appDatabase.categoryDao().getResources();
        mNames.addAll(categoryFromDatabase);
        mResources.addAll(resources);
        recyclerAdapter.notifyDataSetChanged();
    }
}
