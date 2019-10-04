package com.example.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterWeather extends BaseAdapter {

    Context context;
    List<Weather> listDay;

    public AdapterWeather(Context context, List<Weather> listDay) {
        this.context = context;
        this.listDay = listDay;
    }

    @Override
    public int getCount() {
        return listDay.size();
    }

    @Override
    public Object getItem(int position) {
        return listDay.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.day_item, parent,false);
        TextView tvDay= convertView.findViewById(R.id.tvDay);
        TextView tvStatus= convertView.findViewById(R.id.tvStatus2);
        TextView tvMaxTemp= convertView.findViewById(R.id.tvMaxTemp);
        TextView tvMinTemp= convertView.findViewById(R.id.tvMinTemp);
        ImageView imgIcon2=convertView.findViewById(R.id.imgIcon2);

        Weather weather = listDay.get(position);
        tvDay.setText(weather.getDay());
        tvStatus.setText(weather.getStatus());
        tvMaxTemp.setText(weather.getMaxTemp()+"°C");
        tvMinTemp.setText(weather.getMinTemp()+"°C");
        Picasso.with(context).load("http://openweathermap.org/img/wn/" + weather.getImage() + ".png").into(imgIcon2);
        return convertView;
    }
}
