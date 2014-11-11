package lab5.avdyushkin.tru.pointofsalesimulation.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import lab5.avdyushkin.tru.pointofsalesimulation.DetailedHistory;
import lab5.avdyushkin.tru.pointofsalesimulation.R;

public class ShortResultAdapter extends ArrayAdapter<String> {
    ArrayList<String> timeList;
    ArrayList<Double> totalList;
    ArrayList<Integer> idList;
    NumberFormat formatter;

    public ShortResultAdapter(Context context, int resource, ArrayList<String> timeList,
                              ArrayList<Double> totalList, ArrayList<Integer> idList) {
        super(context, resource);
        this.timeList = timeList;
        this.totalList = totalList;
        this.idList = idList;
        formatter = new DecimalFormat("#0.00");
    }

    @Override
    public int getCount() {
        return timeList.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.min_hisory_row, parent, false);
        }

        TextView time = (TextView) convertView.findViewById(R.id.time);
        TextView total = (TextView) convertView.findViewById(R.id.total);
        TextView idLabel = (TextView) convertView.findViewById(R.id.idLabel);

        Button details = (Button) convertView.findViewById(R.id.details);
        time.setText(timeList.get(position));
        total.setText("$" + formatter.format(totalList.get(position)));
        idLabel.setText(idList.get(position).toString());
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DetailedHistory.class);
                intent.putExtra("id", idList.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);

            }
        });

        return convertView;
    }

}
