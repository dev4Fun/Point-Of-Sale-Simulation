package lab5.avdyushkin.tru.pointofsalesimulation.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import lab5.avdyushkin.tru.pointofsalesimulation.R;
import lab5.avdyushkin.tru.pointofsalesimulation.items.Item;

public class ItemListAdapter extends ArrayAdapter<Item> {

    public ItemListAdapter(Context context, int resource, List<Item> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_row, parent, false);
        }

        TextView itemLabel = (TextView) convertView.findViewById(R.id.itemLabel);
        TextView priceLabel = (TextView) convertView.findViewById(R.id.priceLabel);
        ImageView itemIcon = (ImageView) convertView.findViewById(R.id.itemIcon);

        if (getItem(position).getImageId() == 0) { // this has path
            Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(getItem(position).getPath()), 128, 128);
            itemIcon.setImageBitmap(ThumbImage);
        } else {
            itemIcon.setImageResource(getItem(position).getImageId());
        }

        itemLabel.setText(getItem(position).getName());
        priceLabel.setText("$" + getItem(position).getPrice());

        return convertView;
    }
}
