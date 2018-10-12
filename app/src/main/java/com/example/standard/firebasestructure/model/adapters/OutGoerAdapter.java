package com.example.standard.firebasestructure.model.adapters;

import android.content.Context;
import android.support.annotation.*;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.*;

import com.example.standard.firebasestructure.*;
import com.example.standard.firebasestructure.model.*;

import java.util.List;

import static com.example.standard.firebasestructure.Utils.calculateTimeSince;

public class OutGoerAdapter extends ArrayAdapter<OutGoer> {

    private Context context;
    private List<OutGoer> outGoers;

    public OutGoerAdapter(@NonNull Context context, int resource, List<OutGoer> outGoers) {
        super(context, resource, outGoers);
        this.context = context;
        this.outGoers = outGoers;
    }

    @Override
    public int getCount() {
        return outGoers.size();
    }

    @Nullable
    @Override
    public OutGoer getItem(int position) {
        return outGoers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.view_feed_item, parent, false);

        TextView tvUserName = convertView.findViewById(R.id.userName);
        TextView tvVenueName = convertView.findViewById(R.id.venueName);
        TextView tvTimeSince = convertView.findViewById(R.id.timeSince);

        tvUserName.setText(outGoers.get(position).getUserName());
        tvVenueName.setText(outGoers.get(position).getVenueName());
        tvTimeSince.setText(calculateTimeSince(context, outGoers.get(position).getTimeMillis()));

        return convertView;
    }
}
