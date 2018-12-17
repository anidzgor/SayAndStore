package localization.nidzgor.com.voiceapp.XmlUtils;

import android.content.Context;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

import localization.nidzgor.com.voiceapp.Product;

public class XmlConverter {

    private static String filename = "shoppingList.xml";

    public static void saveDataToXmlFile(ArrayList<Product> products, File context) throws IOException {

        File yourFile = new File(context, filename);
        yourFile.createNewFile(); // if file already exists will do nothing
        FileOutputStream fileos = new FileOutputStream(yourFile, false);
        XmlSerializer xmlSerializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        xmlSerializer.setOutput(writer);

        xmlSerializer.startDocument("UTF-8", true);
        xmlSerializer.startTag(null, "Products");

        for(Product p: products) {
            xmlSerializer.startTag(null, "Product");
            xmlSerializer.text(p.getName());
            xmlSerializer.endTag(null, "Product");
        }

        xmlSerializer.endTag(null, "Products");
        xmlSerializer.endDocument();

        xmlSerializer.flush();
        String dataWrite = writer.toString();
        fileos.write(dataWrite.getBytes());
        fileos.close();
    }

    public static ArrayList<Product> readDataFromXmlFile(Context context) throws IOException, XmlPullParserException {
        ArrayList<Product> readProductsFromFile = new ArrayList<Product>();
        File yourFile = new File(context.getFilesDir(), filename);
        if(yourFile.exists()) {
            FileInputStream fis = context.openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            char[] inputBuffer = new char[fis.available()];
            isr.read(inputBuffer);
            String data = new String(inputBuffer);
            isr.close();
            fis.close();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(data));

            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT){
                if (eventType == XmlPullParser.START_DOCUMENT) {
                    System.out.println("Start document");
                }
                else if (eventType == XmlPullParser.START_TAG) {
                    System.out.println("Start tag "+ xpp.getName());
                }
                else if (eventType == XmlPullParser.END_TAG) {
                    System.out.println("End tag "+ xpp.getName());
                }
                else if(eventType == XmlPullParser.TEXT) {
                    readProductsFromFile.add(new Product(xpp.getText()));
                }
                eventType = xpp.next();
            }
        }
        return readProductsFromFile;
    }
}
