package com.threenitas.basicnavigator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class LocInfoAdapter extends ArrayAdapter<LocInfo> {

    private Context mContext;
    private int mResource;

    public LocInfoAdapter(Context context, int resource, List<LocInfo> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String addressFrom = getItem(position).getAddressFrom();
        String nameFrom = getItem(position).getNameFrom();

        String addressTo = getItem(position).getAddressTo();
        String nameTo = getItem(position).getNamePlaceTo();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);


        TextView tvAddressFrom = (TextView)convertView.findViewById(R.id.addressFrom);
        TextView tvNameFrom = (TextView)convertView.findViewById(R.id.nameFrom);

        TextView tvAddressTo = (TextView)convertView.findViewById(R.id.addressTo);
        TextView tvNameTo = (TextView)convertView.findViewById(R.id.nameTo);


        tvAddressFrom.setText(addressFrom);
        tvNameFrom.setText(nameFrom);

        tvAddressTo.setText(addressTo);
        tvNameTo.setText(nameTo);

        return convertView;
    }
}

