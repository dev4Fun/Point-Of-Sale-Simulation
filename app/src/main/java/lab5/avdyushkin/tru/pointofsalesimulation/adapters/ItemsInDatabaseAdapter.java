package lab5.avdyushkin.tru.pointofsalesimulation.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import lab5.avdyushkin.tru.pointofsalesimulation.items.Item;
import lab5.avdyushkin.tru.pointofsalesimulation.databases.ItemDatabase;
import lab5.avdyushkin.tru.pointofsalesimulation.R;

public class ItemsInDatabaseAdapter extends ArrayAdapter<Item> {


    public ItemsInDatabaseAdapter(Context context, int resource, List<Item> objects) {
        super(context, resource, objects);
        retriveFromDB();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_database_list_row, parent, false);
        }

        TextView itemLabel = (TextView) convertView.findViewById(R.id.itemLabel);
        TextView priceLabel = (TextView) convertView.findViewById(R.id.priceLabel);
        ImageView itemIcon = (ImageView) convertView.findViewById(R.id.itemIcon);
        ImageButton removeBtn = (ImageButton) convertView.findViewById(R.id.removeBtn);

        if (getItem(position).getImageId() == 0) { // this has path
            Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(getItem(position).getPath()), 64, 64);
            itemIcon.setImageBitmap(ThumbImage);
        } else {
            itemIcon.setImageResource(getItem(position).getImageId());
        }

        itemLabel.setText(getItem(position).getName());
        priceLabel.setText("$" + getItem(position).getPrice());

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long rowId = getItem(position).getColumnID();
                int result = ItemDatabase.DatabaseHelper.removeItem(getContext(), rowId);
                if (result != 0) {
                    Toast toast = Toast.makeText(getContext(), "Removed " + getItem(position).getName(), Toast.LENGTH_SHORT);
                    toast.show();
                    remove(getItem(position));
                    notifyDataSetChanged();
                }
            }
        });

        return convertView;
    }

    private void retriveFromDB() {
        String name;
        String path;
        double price;
        long columnID;
        Cursor results = ItemDatabase.DatabaseHelper.getResultsFromDB(getContext());

        while (!results.isAfterLast()) {
            columnID = results.getLong(results.getColumnIndexOrThrow(ItemDatabase.DatabaseEntry._ID));
            name = results.getString(results.getColumnIndex(ItemDatabase.DatabaseEntry.COLUMN_ITEM_NAME));
            price = results.getDouble(results.getColumnIndex(ItemDatabase.DatabaseEntry.COLUMN_ITEM_PRICE));
            path = results.getString(results.getColumnIndex(ItemDatabase.DatabaseEntry.COLUMN_ITEM_IMAGE));

            add(new Item(name, price, path, columnID));

            results.moveToNext();
        }
        results.close();
    }
}
