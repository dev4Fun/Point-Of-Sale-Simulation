package lab5.avdyushkin.tru.pointofsalesimulation.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import lab5.avdyushkin.tru.pointofsalesimulation.R;
import lab5.avdyushkin.tru.pointofsalesimulation.items.ItemInCart;


public class ItemsCartAdapter extends BaseAdapter {
    Context context;

    public List<ItemInCart> getItems() {
        return items;
    }

    private List<ItemInCart> items; // all the items
    private static double total;
    NumberFormat formatter; // format total

    public static void setTotal(double total) {
        ItemsCartAdapter.total = total;
    }


    public double getTotal() {
        return total;
    }

    public ItemsCartAdapter(Context context, List<ItemInCart> objects) {
        this.context = context;
        this.items = objects;
        formatter = new DecimalFormat("#0.00");
    }

    public void add(ItemInCart item) {
        int position = getItemPosition(item); // check if item exists
        if (position == -1) { // new item
            items.add(item);
        } else if (item.getPrice() != getItem(position).getPrice()) { // discounted item added
            items.add(item);
        } else { // item exists
            int currentQuantity = getItem(position).getQuantity();
            currentQuantity++;
            getItem(position).setQuantity(currentQuantity);
        }

        total += item.getPrice();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public ItemInCart getItem(int position) {
        return items.get(position);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_list_card_row, parent, false);
        }

        TextView itemLabel = (TextView) convertView.findViewById(R.id.itemLabel);
        TextView priceLabel = (TextView) convertView.findViewById(R.id.priceLabel);
        ImageView itemIcon = (ImageView) convertView.findViewById(R.id.itemIcon);
        TextView quantityLabel = (TextView) convertView.findViewById(R.id.quantityLabel);
        final ImageButton removeBtn = (ImageButton) convertView.findViewById(R.id.removeBtn);

        if (getItem(position).getImageId() == 0) { // this has path
            Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(getItem(position).getPath()), 64, 64);
            itemIcon.setImageBitmap(ThumbImage);
        } else {
            itemIcon.setImageResource(getItem(position).getImageId());
        }

        itemLabel.setText(getItem(position).getName());
        priceLabel.setText("$" + formatter.format(getItem(position).getPrice()));
        quantityLabel.setText("x " + getItem(position).getQuantity());

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = (View) parent.getParent();
                final TextView totalLabel = (TextView) view1.findViewById(R.id.totalLabel);
                total = Double.valueOf(totalLabel.getText().toString()) - getItem(position).getPrice();
                totalLabel.setText("" + formatter.format(total));

                if (getItem(position).getQuantity() > 1) {
                    int currentQuantity = getItem(position).getQuantity();
                    currentQuantity--;
                    getItem(position).setQuantity(currentQuantity);
                } else {
                    items.remove(position);
                }

                ListView listView1 = (ListView) parent.findViewById(R.id.itemCartView);
                listView1.setAdapter(new ItemsCartAdapter(context, items));

            }
        });

        return convertView;
    }

    private int getItemPosition(ItemInCart searchItem) { // check if item exists in items list
        String name = searchItem.getName();
        for (int i = 0; i < items.size(); i++) {
            ItemInCart item = items.get(i);
            if (item.getName().equals(name) && item.getPrice() == searchItem.getPrice()) {
                return i;
            }
        }
        return -1;
    }
}
