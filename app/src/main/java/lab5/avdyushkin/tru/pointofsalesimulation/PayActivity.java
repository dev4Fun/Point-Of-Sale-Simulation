package lab5.avdyushkin.tru.pointofsalesimulation;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import lab5.avdyushkin.tru.pointofsalesimulation.databases.TransactionDatabase;
import lab5.avdyushkin.tru.pointofsalesimulation.items.ItemInCart;

public class PayActivity extends Activity {
    TextView totalVal, taxVal, afterTaxVal;
    double total, afterTax;
    NumberFormat formatter;
    TransactionDatabase.DatabaseHelper mDbHelper;
    ArrayList<ItemInCart> list = new ArrayList<ItemInCart>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        total = 0;
        formatter = new DecimalFormat("#0.00");

        Bundle extras = getIntent().getExtras(); // get total from main activity
        if (extras != null) {
            total = extras.getDouble("total");
            String stringJson = extras.getString("items");
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<ItemInCart>>() {
            }.getType();
            list = gson.fromJson(stringJson, type);
        }

        totalVal = (TextView) findViewById(R.id.totalVal);
        totalVal.setText(formatter.format(total));

        taxVal = (TextView) findViewById(R.id.taxVal);
        taxVal.setText("13%"); // default tax rate

        afterTax = total * (1 + Double.valueOf(taxVal.getText().toString().substring(0, 2)) / 100);
        afterTaxVal = (TextView) findViewById(R.id.afterTaxVal);
        afterTaxVal.setText("" + formatter.format(afterTax));

    }


    public void GoBack(View v) {
        finish();
    }

    public void SetTax(View v) { // create a dialog to set tax
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.discount_item);
        dialog.setTitle("Set the Tax");

        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText("Enter the Tax %");

        final EditText editText = (EditText) dialog.findViewById(R.id.editText);
        editText.setText("");
        editText.setHint("e.x15");
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText.getText().toString().equals("")) {
                    int val = Integer.valueOf(editText.getText().toString());

                    if (val > -1 && val <= 100) { // 0-100
                        taxVal.setText(val + "%");

                        // calculates after tax, disregard % value
                        afterTax = total * (1 + Double.valueOf(taxVal.getText().toString().substring(0, taxVal.getText().length() - 1)) / 100);
                        afterTaxVal = (TextView) findViewById(R.id.afterTaxVal);
                        afterTaxVal.setText("" + formatter.format(afterTax));
                        dialog.dismiss();
                        Toast toast = Toast.makeText(getApplicationContext(), "Set tax to " + val + " %", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Have to be number between 0-100", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Have to be number between 0-100", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        dialog.show();
    }

    public void CompletePayment(View v) {
        try {
            writeToDb(list, afterTax);
        } catch (JSONException ex) {

        }
        MainActivity.restart = true; // to reset the cart in main activity
        startActivity(new Intent(this, MainActivity.class)); // return to the previous activity
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void writeToDb(ArrayList<ItemInCart> items, Double total) throws JSONException {
        /*
        * Write an information about item to DB
        * */
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        String datetoDB = today.format("%F %H:%M");

        Gson gson = new Gson();
        String json = gson.toJson(items);

        mDbHelper = new TransactionDatabase.DatabaseHelper(getBaseContext());
        ContentValues values = new ContentValues();
        values.put(TransactionDatabase.DatabaseEntry.COLUMN_TIME, datetoDB);
        values.put(TransactionDatabase.DatabaseEntry.COLUMN_ITEMS, json);
        values.put(TransactionDatabase.DatabaseEntry.COLUMN_TOTAL, total);

        // Insert the new row
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long col = db.insert(
                TransactionDatabase.DatabaseEntry.TABLE_NAME,
                null,
                values);
        if (col != 0) {
            Toast toast = Toast.makeText(getBaseContext(), "Transaction Saved to Database", Toast.LENGTH_SHORT);
            toast.show();
        }

        db.close();

    }
}
