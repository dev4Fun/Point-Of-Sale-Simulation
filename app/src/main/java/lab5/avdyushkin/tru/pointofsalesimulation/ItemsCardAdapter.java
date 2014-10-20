package lab5.avdyushkin.tru.pointofsalesimulation;

import android.content.Context;
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


public class ItemsCardAdapter extends BaseAdapter {
    Context context;
    private List<ItemInCard> items;
    private static double total;
    NumberFormat formatter;

    public double getTotal() {
        return total;
    }

    public ItemsCardAdapter(Context context, List<ItemInCard> objects) {
        this.context = context;
        this.items = objects;
        formatter = new DecimalFormat("#0.00");
    }

    public void add(ItemInCard item) {
        int position = getItemPosition(item);
        if (position == -1) {
            items.add(item);
        } else if(item.getPrice() != getItem(position).getPrice()){ // discounted item added
            items.add(item);
        } else {
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
    public ItemInCard getItem(int position) {
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
        ImageButton removeBtn = (ImageButton) convertView.findViewById(R.id.removeBtn);

        itemIcon.setImageResource(getItem(position).getImageId());
        itemLabel.setText(getItem(position).getName());
        priceLabel.setText("$" + getItem(position).getPrice());
        quantityLabel.setText("x " + getItem(position).getQuantity());

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 =  (View) parent.getParent();
                final TextView totalLabel = (TextView)view1.findViewById(R.id.totalLabel);
                total = Double.valueOf(totalLabel.getText().toString()) - getItem(position).getPrice();
                totalLabel.setText("" + formatter.format(total));

                if (getItem(position).getQuantity() > 1) {
                    int currentQuantity = getItem(position).getQuantity();
                    currentQuantity--;
                    getItem(position).setQuantity(currentQuantity);
                } else {
                    items.remove(position);
                }

                ListView listView1 = (ListView) parent.findViewById(R.id.itemCardView);
                listView1.setAdapter(new ItemsCardAdapter(context, items));

            }
        });

        return convertView;
    }

    private int getItemPosition(ItemInCard searchItem) {
        String name = searchItem.getName();
        for (int i = 0; i < items.size(); i++) {
            ItemInCard item = items.get(i);
            if (item.getName().equals(name) && item.getPrice() == searchItem.getPrice()) {
                return i;
            }
        }
        return -1;
    }
}
