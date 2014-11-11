package lab5.avdyushkin.tru.pointofsalesimulation;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import lab5.avdyushkin.tru.pointofsalesimulation.adapters.DetailHistoryAdapter;
import lab5.avdyushkin.tru.pointofsalesimulation.databases.TransactionDatabase;
import lab5.avdyushkin.tru.pointofsalesimulation.items.ItemInCart;


public class DetailedHistory extends Activity {
    ArrayList<ItemInCart> itemsInCard = new ArrayList<ItemInCart>();
    DetailHistoryAdapter cartAdapter;
    ListView itemList;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_history);
        itemList = (ListView) findViewById(R.id.itemList);
        title = (TextView) findViewById(R.id.title);


        Bundle extras = getIntent().getExtras(); // get id from prev. activity
        if (extras != null) {
            int id = extras.getInt("id");
            retriveData(id);
            title.setText("Details of transaction # " + id);
        }

        cartAdapter = new DetailHistoryAdapter(getBaseContext(), itemsInCard);
        itemList.setAdapter(cartAdapter);
    }

    private void retriveData(int id) {
        // retrive data
        Gson gson = new Gson();
        Cursor results = TransactionDatabase.DatabaseHelper.getDataByID(id, getBaseContext());
        String stringJson = results.getString(results.getColumnIndex(TransactionDatabase.DatabaseEntry.COLUMN_ITEMS));
        Type type = new TypeToken<ArrayList<ItemInCart>>() {
        }.getType();
        itemsInCard = gson.fromJson(stringJson, type);
        results.close();
    }

    public void GoBack(View v) {
        finish();
    }

}
