package lab5.avdyushkin.tru.pointofsalesimulation;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class MainActivity extends Activity {
    boolean applyDiscount;
    double discount;

    ArrayList <Item> items;
    ArrayList <ItemInCard> itemsInCard;

    ItemListAdapter listAdapter;
    ItemsCardAdapter cardListAdapter;

    GridView itemListGrid;
    ListView itemCardView;
    TextView totalLabel;

    NumberFormat formatter; // total number formatter


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        applyDiscount = false;
        formatter = new DecimalFormat("#0.00");

        totalLabel = (TextView)findViewById(R.id.totalLabel);
        itemListGrid = (GridView)findViewById(R.id.itemListGrid);
        itemCardView = (ListView)findViewById(R.id.itemCardView);

        totalLabel.setText("0.0");

        itemsInCard = new ArrayList<ItemInCard>();
        items = new ArrayList<Item>();

        items.add(new Item("Magnifier", 15,  R.drawable.ic_action_search));
        items.add(new Item("Pen", 5,  R.drawable.ic_content_edit));
        items.add(new Item("SD card", 10,  R.drawable.ic_device_access_sd_storage));
        items.add(new Item("Phone", 400,  R.drawable.ic_hardware_phone));
        items.add(new Item("Headphones", 30,  R.drawable.ic_hardware_headphones));
        items.add(new Item("Laptop", 500,  R.drawable.ic_hardware_computer));
        items.add(new Item("Lock", 10,  R.drawable.ic_device_access_secure));
        items.add(new Item("Calendar", 5,  R.drawable.ic_collections_go_to_today));
        items.add(new Item("Bread", 3,  R.drawable.ic_bread_png2318));
        items.add(new Item("Onion", 2,  R.drawable.ic_onion_png3828));
        items.add(new Item("Potatoes", 8,  R.drawable.ic_potato_png2393));
        items.add(new Item("Tomato", 1,  R.drawable.ic_tamato_png2709));
        items.add(new Item("Bananas", 3,  R.drawable.ic_banana_png845));
        items.add(new Item("Carrot", 1.20,  R.drawable.ic_carrot_png4985));
        items.add(new Item("Ice Cream", 2.20,  R.drawable.ic_ice_cream_png5097));
        items.add(new Item("Cucumber", 2.23,  R.drawable.ic_cucumber_png5213));
        items.add(new Item("Coca Cola", 1.10,  R.drawable.ic_cocacola_png16));
        items.add(new Item("Bear", 20,  R.drawable.ic_bear_png1194));
        items.add(new Item("Toyota", 20000,  R.drawable.ic_toyota_png1951));
        items.add(new Item("Cigarette", 10,  R.drawable.ic_cigarette_png4774));
        items.add(new Item("Ak-47", 1000,  R.drawable.ic_assault_rifle_png1442));

        listAdapter = new ItemListAdapter(getBaseContext(), 0 , items);

        // items in card initialization
        cardListAdapter = new ItemsCardAdapter(getBaseContext(), itemsInCard);

        // available items initialization
        itemListGrid.setAdapter(listAdapter);
        itemListGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item item = listAdapter.getItem(i);
                ItemInCard addItem = new ItemInCard(item.getName(),item.getPrice(),item.getImageId(), 1);
                if(applyDiscount){
                    addItem.setPrice(addItem.getPrice() * discount);
                    applyDiscount = false;
                    discount = 1;
                }
                cardListAdapter.add(addItem);
                totalLabel.setText("" + formatter.format(cardListAdapter.getTotal()));
                itemCardView.setAdapter(cardListAdapter);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        applyDiscount = false;
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

    public void ActivateDiscount(View v){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.discount_item);
        dialog.setTitle("Set Discount for the next Item");

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText("Enter the discount % amount");

        final EditText editText = (EditText) dialog.findViewById(R.id.editText);
        editText.setText("");
        editText.setHint("e.x15");
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editText.getText().toString().equals("")){
                    int val = Integer.valueOf(editText.getText().toString());

                    if(val > 0 && val <= 100){
                        applyDiscount = true;
                        SetDiscount(val);
                        dialog.dismiss();
                        Toast toast = Toast.makeText(getApplicationContext(), "Set discount to " + val + " %", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Have to be number between 1-100", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Have to be number between 1-100", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        dialog.show();
    }

    private void SetDiscount(int percent){
        discount = 1 - (double)percent/100;
    }



}
