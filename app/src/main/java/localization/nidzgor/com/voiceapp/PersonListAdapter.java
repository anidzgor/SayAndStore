package localization.nidzgor.com.voiceapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import localization.nidzgor.com.voiceapp.XmlUtils.XmlConverter;

class PersonListAdapter extends ArrayAdapter<Product> {

    private static final String TAG = "PersonListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    private ArrayList<Product> products;

    static class ViewHolder {
        TextView name;
    }

    public PersonListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Product> productsFromShoppingList) {
        super(context, resource, productsFromShoppingList);
        mContext = context;
        mResource = resource;
        products = productsFromShoppingList;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getName();
        Product product = new Product(name);

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
                try {
                    ArrayList<Product> productsFromFile = XmlConverter.readDataFromXmlFile(mContext);
                    productsFromFile.remove(position);
                    XmlConverter.saveDataToXmlFile(productsFromFile, mContext.getFilesDir());

                    refresh(productsFromFile);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        });
        return convertView;
    }

    public void refresh(ArrayList<Product> productsToWrite)
    {
        this.products.clear();
        ArrayList<Product> newList = productsToWrite;
        this.products.addAll(newList);
        this.notifyDataSetChanged();
    }
}
