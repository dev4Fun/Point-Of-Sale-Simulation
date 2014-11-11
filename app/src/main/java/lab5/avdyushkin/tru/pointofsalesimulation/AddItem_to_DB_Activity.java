package lab5.avdyushkin.tru.pointofsalesimulation;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import lab5.avdyushkin.tru.pointofsalesimulation.databases.ItemDatabase;


public class AddItem_to_DB_Activity extends Activity {
    ItemDatabase.DatabaseHelper mDbHelper;
    ImageView imagePic;
    EditText itemName, itemPrice;
    String filePath;
    String[] names;
    public static int SELECT_PICTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_to__db_);
        imagePic = (ImageView) findViewById(R.id.imagePic);
        itemName = (EditText) findViewById(R.id.itemName);
        itemPrice = (EditText) findViewById(R.id.itemPrice);

        filePath = "";

        itemPrice.setText("");
        itemName.setText("");

        itemName.setHint("My Item");
        itemPrice.setHint("10.30");

        Bundle extras = getIntent().getExtras(); // get names from main activity
        if (extras != null) {
            names = extras.getStringArray("names");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri imageURI = data.getData();
                Cursor returnCursor =
                        getContentResolver().query(imageURI, null, null, null, null);
                // int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                returnCursor.moveToFirst();

                // Log.i("MSG", "fiel size " + returnCursor.getString(sizeIndex)); // size is in bytes
                filePath = getRealPAth(getBaseContext(), imageURI);
                Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(filePath), 100, 100);

                imagePic.setImageBitmap(ThumbImage);
            }
        }
    }

    public void AddItem(View v) {
        double price = 0;
        if (!itemPrice.getText().toString().equals(""))
            price = Double.valueOf(itemPrice.getText().toString());
        else {
            Toast toast = Toast.makeText(getBaseContext(), "Price has to be a number more than 0", Toast.LENGTH_SHORT);
            toast.show();
        }

        String name = itemName.getText().toString();

        if (price > 0 && name.length() > 0 && filePath.length() > 1) {
            name = cleanString(name);
            if (checkName(name)) {
                writeToDb(name, price, filePath);
                finish();
                startActivity(new Intent(getBaseContext(), MainActivity.class));
            } else {
                Toast toast = Toast.makeText(getBaseContext(), "Item named " + name +
                        " already exists", Toast.LENGTH_SHORT);
                toast.show();
            }

        } else {
            Toast toast = Toast.makeText(getBaseContext(), "Specify the name for the item", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private String cleanString(String arg) {
        arg = arg.toLowerCase();
        arg = arg.replaceAll("\\s", ""); // remove white spaces
        arg = Character.toUpperCase(arg.charAt(0)) + arg.substring(1); // capitalize first letter
        return arg;
    }

    private boolean checkName(String name) {
        for (String name1 : names) {
            if (name1.equals(name))
                return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        GoBack(new View(getBaseContext()));
    }

    public void GoBack(View v) {
        finish();
        startActivity(new Intent(getBaseContext(), MainActivity.class));
    }

    public void SelectPicture(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select a picture form device"), SELECT_PICTURE);
    }

    private void writeToDb(String name, Double price, String imagePath) {
        /*
        * Write an information about item to DB
        * */

        mDbHelper = new ItemDatabase.DatabaseHelper(getBaseContext());

        ContentValues values = new ContentValues();
        values.put(ItemDatabase.DatabaseEntry.COLUMN_ITEM_NAME, name);
        values.put(ItemDatabase.DatabaseEntry.COLUMN_ITEM_PRICE, price);
        values.put(ItemDatabase.DatabaseEntry.COLUMN_ITEM_IMAGE, imagePath);

        // Insert the new row
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.insert(
                ItemDatabase.DatabaseEntry.TABLE_NAME,
                null,
                values);

        Toast toast = Toast.makeText(getBaseContext(), "Saved " + name + " to Database", Toast.LENGTH_SHORT);
        toast.show();
    }

    public String getRealPAth(final Context context, final Uri fileURI) {
        String result = "";
        Cursor cursor = null;
        try {
            String[] projection = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(fileURI, projection, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return result;
    }

}
