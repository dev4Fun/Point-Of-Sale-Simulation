package lab5.avdyushkin.tru.pointofsalesimulation;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;


import java.util.ArrayList;

import lab5.avdyushkin.tru.pointofsalesimulation.adapters.ShortResultAdapter;
import lab5.avdyushkin.tru.pointofsalesimulation.databases.TransactionDatabase;

public class ShowTransactionHistoryActivity extends Activity {
    ArrayList<String> time_list = new ArrayList<String>();
    ArrayList<Double> total_list = new ArrayList<Double>();
    ArrayList<Integer> id_list = new ArrayList<Integer>();

    ListView transaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_transaction_history);


        transaction = (ListView) findViewById(R.id.transaction);
        retriveMinData();
        ShortResultAdapter resultAdapter = new ShortResultAdapter(getBaseContext(), 0, time_list, total_list, id_list);
        transaction.setAdapter(resultAdapter);
    }

    private void retriveMinData() {
        // retrive data
        Cursor results = TransactionDatabase.DatabaseHelper.getResultsFromDB(getBaseContext());
        while (!results.isAfterLast()) {
            int id = results.getInt(results.getColumnIndex(TransactionDatabase.DatabaseEntry._ID));
            String time = results.getString(results.getColumnIndex(TransactionDatabase.DatabaseEntry.COLUMN_TIME));
            double total = results.getDouble(results.getColumnIndex(TransactionDatabase.DatabaseEntry.COLUMN_TOTAL));
            time_list.add(time);
            total_list.add(total);
            id_list.add(id);
            results.moveToNext();
        }
        results.close();
    }

    public void GoBack(View v) {
        finish();
        startActivity(new Intent(getBaseContext(), MainActivity.class));
    }


}
