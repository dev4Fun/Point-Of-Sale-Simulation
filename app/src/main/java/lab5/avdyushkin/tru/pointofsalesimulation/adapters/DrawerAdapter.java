package lab5.avdyushkin.tru.pointofsalesimulation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import lab5.avdyushkin.tru.pointofsalesimulation.R;
import lab5.avdyushkin.tru.pointofsalesimulation.items.DrawerItem;


public class DrawerAdapter extends ArrayAdapter<DrawerItem> {

    public DrawerAdapter(Context context, int resource, List<DrawerItem> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.drawer_row, parent, false);
        }

        TextView itemLabel = (TextView) convertView.findViewById(R.id.itemLabel);
        ImageView itemIcon = (ImageView) convertView.findViewById(R.id.itemIcon);

        itemLabel.setText(getItem(position).getName());
        itemIcon.setImageResource(getItem(position).getImageId());

        return convertView;
    }
}
