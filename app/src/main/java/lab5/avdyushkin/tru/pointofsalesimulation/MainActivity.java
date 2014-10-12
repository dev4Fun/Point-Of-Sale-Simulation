package lab5.avdyushkin.tru.pointofsalesimulation;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends Activity {
    GridView itemListGrid;
    ListGridAdapter itemGridAdapter;

    String itemListArray[] = {
            "Item1", "Item2", "Item3",
            "Item3", "Item3", "Item3",
            "Item3", "Item3", "Item3"
    };
    double priceListArray[] = {
            10, 20, 30,
            20, 20, 20,
            10, 10, 10
    };
    int imageIdsArray[] = {
            R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher,
            R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher,
            R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher
    };

    ArrayList<String> itemNameList = new ArrayList<String>();
    ArrayList<Double> itemPriceList = new ArrayList<Double>();
    ArrayList<Integer> iconIdsList = new ArrayList<Integer>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemNameList.addAll(Arrays.asList(itemListArray));
        for(double item: priceListArray)
            itemPriceList.add(item);
        for(int item: imageIdsArray)
                iconIdsList.add(item);

        Log.i("MSG", itemNameList.toString());
        Log.i("MSG", itemPriceList.toString());
        Log.i("MSG", iconIdsList.toString());
        // view initialization
        itemListGrid = (GridView)findViewById(R.id.itemListGrid);
        itemGridAdapter = new ListGridAdapter(this, itemNameList, itemPriceList, iconIdsList);
        itemListGrid.setAdapter(itemGridAdapter);
        itemListGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // add item to the right list
                // update total
                // show toast
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
