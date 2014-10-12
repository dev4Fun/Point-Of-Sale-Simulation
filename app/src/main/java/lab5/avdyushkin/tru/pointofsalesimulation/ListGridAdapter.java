package lab5.avdyushkin.tru.pointofsalesimulation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListGridAdapter extends ArrayAdapter {
    Context context;
    ArrayList<String> itemNameList;
    ArrayList<Integer> iconIdsList;
    ArrayList<Double> itemPriceList;

    public ListGridAdapter(Context context, ArrayList<String> itemNameList,
                           ArrayList<Double> itemPriceList, ArrayList<Integer> iconIdsList) {
        super(context, R.layout.item_list_row);
        this.context = context;
        this.iconIdsList = iconIdsList;
        this.itemNameList = itemNameList;
        this.itemPriceList = itemPriceList;
    }

    @Override
    public int getCount() {
        return itemNameList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridCellView;
        if (convertView == null) {
            gridCellView = inflater.inflate(R.layout.item_list_row, parent, false);
            TextView itemLabel = (TextView) gridCellView.findViewById(R.id.itemLabel);
            TextView priceLabel = (TextView) gridCellView.findViewById(R.id.priceLabel);
            ImageView itemIcon = (ImageView) gridCellView.findViewById(R.id.itemIcon);

            itemIcon.setImageResource(iconIdsList.get(position));
            itemLabel.setText(itemNameList.get(position));
            priceLabel.setText("$" + itemPriceList.get(position).toString());
        } else {
            gridCellView = convertView;
        }

        return gridCellView;
    }



}
