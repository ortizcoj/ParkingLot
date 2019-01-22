package com.example.juanc.parkinglotdemo4;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


class CustomAdapter extends ArrayAdapter<String> {

    int[] spinnerTime;
    Context mContext;

    public CustomAdapter(@NonNull Context context, int[] Time) {
        super(context, R.layout.time_spinner);
        this.spinnerTime = Time;
        this.mContext = context;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.time_spinner, parent, false);
            mViewHolder.mTime = (TextView) convertView.findViewById(R.id.tvTime);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.mTime.setText(spinnerTime[position]);

        return convertView;
    }

    private static class ViewHolder {
        TextView mTime;
    }
}