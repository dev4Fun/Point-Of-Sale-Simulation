package lab5.avdyushkin.tru.pointofsalesimulation;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;


import lab5.avdyushkin.tru.pointofsalesimulation.adapters.DrawerAdapter;
import lab5.avdyushkin.tru.pointofsalesimulation.adapters.ItemListAdapter;
import lab5.avdyushkin.tru.pointofsalesimulation.adapters.ItemsCartAdapter;
import lab5.avdyushkin.tru.pointofsalesimulation.items.DrawerItem;
import lab5.avdyushkin.tru.pointofsalesimulation.items.Item;
import lab5.avdyushkin.tru.pointofsalesimulation.databases.ItemDatabase;
import lab5.avdyushkin.tru.pointofsalesimulation.items.ItemInCart;

public class MainActivity extends Activity {
    // TODO: Add tutorial at the start
    boolean applyDiscount; // if a user decided to apply discount
    boolean animate;
    double discount; // discount val
    static boolean restart;

    ArrayList <Item> items;
    ArrayList<ItemInCart> itemsInCard;
    ArrayList<DrawerItem> drawerArray;

    ItemListAdapter listAdapter;
    ItemsCartAdapter cartListAdapter;
    DrawerAdapter drawerAdapter;

    GridView itemListGrid;
    ListView itemCartView, left_drawer;
    TextView totalLabel, discountLabel;
    DrawerLayout leftDrawer;

    NumberFormat formatter; // total number formatter

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /* Initialize left side drawer*/
        leftDrawer = (DrawerLayout) findViewById(R.id.leftDrawer);
        left_drawer = (ListView) findViewById(R.id.left_drawer);

        drawerArray = new ArrayList<DrawerItem>();
        drawerArray.add(new DrawerItem("Add an Item to Database", R.drawable.ic_content_edit));
        drawerArray.add(new DrawerItem("Remove Item from Database", R.drawable.ic_action_highlight_remove));
        drawerArray.add(new DrawerItem("See Transaction History", R.drawable.ic_action_search));
        drawerAdapter = new DrawerAdapter(getBaseContext(), 0, drawerArray);
        left_drawer.setAdapter(drawerAdapter);
        left_drawer.setOnItemClickListener(new DrawerItemClickListener());
        /* end Initialize left side drawer*/

        /* for Total calculation */
        applyDiscount = false;
        restart = false;
        animate = false;
        formatter = new DecimalFormat("#0.00");

        totalLabel = (TextView)findViewById(R.id.totalLabel);
        itemListGrid = (GridView)findViewById(R.id.itemListGrid);
        itemCartView = (ListView) findViewById(R.id.itemCartView);
        discountLabel = (TextView) findViewById(R.id.discountLabel);

        totalLabel.setText("0.0");

        itemsInCard = new ArrayList<ItemInCart>();

        // add hard codded items to the Item List
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

        // load items from DB
        retriveFromDB();

        listAdapter = new ItemListAdapter(getBaseContext(), 0 , items);

        // items in card initialization
        cartListAdapter = new ItemsCartAdapter(getBaseContext(), itemsInCard);

        // available items initialization
        itemListGrid.setAdapter(listAdapter);
        itemListGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item item = listAdapter.getItem(i); // get item from Item list
                ItemInCart addItem;// create an item to put to the card list
                if (item.getImageId() == 0) { // it has path
                    addItem = new ItemInCart(item.getName(), item.getPrice(), item.getPath(), 1);
                } else {
                    addItem = new ItemInCart(item.getName(), item.getPrice(), item.getImageId(), 1);
                }


                if(applyDiscount){
                    addItem.setPrice(addItem.getPrice() * discount); // reduce price
                    applyDiscount = false;
                    discount = 1;
                    animate = true;

                    /* Animate discount label */
                    final Animation arrowAnimation = new AlphaAnimation(1, 0);
                    arrowAnimation.setDuration(1000);
                    arrowAnimation.setInterpolator(new LinearInterpolator());
                    discountLabel.setAnimation(arrowAnimation);

                    Animation.AnimationListener discountAnimationListener = new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation arrowAnimation) {
                            discountLabel.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    };

                    arrowAnimation.setAnimationListener(discountAnimationListener);
                }
                /* end Animate discount label */

                cartListAdapter.add(addItem); // add item to the cart
                totalLabel.setText("" + formatter.format(cartListAdapter.getTotal()));
                itemCartView.setAdapter(cartListAdapter);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        discountLabel.setVisibility(View.GONE);
        applyDiscount = false;
    }

    public void PayForItems(View v) { // on click handler
        if (Double.valueOf(totalLabel.getText().toString()) > 0) { // total > 0
            Intent intent = new Intent(this, PayActivity.class);
            intent.putExtra("total", Double.valueOf(totalLabel.getText().toString())); // attach total when starting a new activity

            Gson gson = new Gson();
            String json = gson.toJson(cartListAdapter.getItems());
            intent.putExtra("items", json); // attach total when starting a new activity
            startActivity(intent);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Your cart is empty!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void ClearCart(View v) { // on Clear Card click handler
        // reset the items in card list
        ClearCart();
    }

    public void ActivateDiscount(View v){
        final Dialog dialog = new Dialog(this); // new dialog
        dialog.setContentView(R.layout.discount_item);
        dialog.setTitle("Set Discount for the Next Item");

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

                    if (val > 0 && val <= 100) { // 1-100
                        applyDiscount = true;
                        SetDiscount(val);
                        dialog.dismiss();
                        discountLabel.setText("Discount set - " + val + " %");

                        final Animation arrowAnimation = new AlphaAnimation(0, 1);
                        arrowAnimation.setDuration(500);
                        arrowAnimation.setInterpolator(new LinearInterpolator());
                        discountLabel.setAnimation(arrowAnimation);
                        discountLabel.setVisibility(View.VISIBLE);

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

    @Override
    protected void onResume() {
        super.onResume();
        if (restart) {
            ClearCart();
            restart = false;
        }
        itemListGrid.setAdapter(listAdapter);
    }

    private void SetDiscount(int percent) { // sets the discount as a value in range(1-0)
        discount = 1 - (double)percent/100;
    }

    private void ClearCart() {
        // reset the items in cart list
        itemsInCard = new ArrayList<ItemInCart>();
        cartListAdapter = new ItemsCartAdapter(getBaseContext(), itemsInCard);
        ItemsCartAdapter.setTotal(0);
        itemCartView.setAdapter(cartListAdapter);
        totalLabel.setText("0.0");
        applyDiscount = false;
    }

    public void AddItem(View v) {
        Intent intent = new Intent(getBaseContext(), AddItem_to_DB_Activity.class);
        intent.putExtra("names", getNames());
        finish();
        ClearCart();
        startActivity(intent);
    }

    /*  Returns an array of all object names*/
    private String[] getNames() {
        String[] results = new String[items.size()];
        for (int i = 0; i < items.size(); i++) {
            results[i] = items.get(i).getName();
        }
        return results;
    }

    public void RemoveItem(View v) {
        finish();
        ClearCart();
        startActivity(new Intent(getBaseContext(), RemoveItemFromDB_Activity.class));
    }

    private void retriveFromDB() {
        String name;
        String path;
        double price;
        Cursor results = ItemDatabase.DatabaseHelper.getResultsFromDB(getBaseContext());

        while (!results.isAfterLast()) {
            name = results.getString(results.getColumnIndex(ItemDatabase.DatabaseEntry.COLUMN_ITEM_NAME));
            price = results.getDouble(results.getColumnIndex(ItemDatabase.DatabaseEntry.COLUMN_ITEM_PRICE));
            path = results.getString(results.getColumnIndex(ItemDatabase.DatabaseEntry.COLUMN_ITEM_IMAGE));

            items.add(new Item(name, price, path));
            results.moveToNext();
        }
        results.close();

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            if (position == 0)
                AddItem(new View(getBaseContext()));
            else if (position == 1)
                RemoveItem(new View(getBaseContext()));
            else if (position == 2)
                startActivity(new Intent(getBaseContext(), ShowTransactionHistoryActivity.class));

        }
    }
}
