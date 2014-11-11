package lab5.avdyushkin.tru.pointofsalesimulation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import lab5.avdyushkin.tru.pointofsalesimulation.adapters.ItemsInDatabaseAdapter;
import lab5.avdyushkin.tru.pointofsalesimulation.items.Item;


public class RemoveItemFromDB_Activity extends Activity {
    ArrayList<Item> items;
    ItemsInDatabaseAdapter listAdapter;
    ListView itemsInDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_item_from_db_);

        itemsInDatabase = (ListView) findViewById(R.id.itemsInDatabase);

        items = new ArrayList<Item>();
        listAdapter = new ItemsInDatabaseAdapter(getBaseContext(), 0, items);
        itemsInDatabase.setAdapter(listAdapter);

        TextView noItems = (TextView) findViewById(R.id.noItems);
        if (listAdapter.isEmpty()) {
            noItems.setVisibility(View.VISIBLE);
            itemsInDatabase.setVisibility(View.GONE);
        } else {
            noItems.setVisibility(View.GONE);
            itemsInDatabase.setVisibility(View.VISIBLE);
        }
    }

    public void GoBack(View v) {
        finish();
        startActivity(new Intent(getBaseContext(), MainActivity.class));
    }


}
