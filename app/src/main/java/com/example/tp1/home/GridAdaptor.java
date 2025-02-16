package com.example.tp1.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tp1.R;

import java.util.ArrayList;

public class GridAdaptor extends ArrayAdapter<AppIcon> {
    ArrayList<AppIcon> birdList ;

    public GridAdaptor(Context context, int textViewResourceId, ArrayList<AppIcon> objects) {
        super(context, textViewResourceId, objects);
        birdList = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View v ;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.app_icon_wrapper, null);
        TextView textView = (TextView) v.findViewById(R.id.textView2);
        ImageView imageView = (ImageView) v.findViewById(R.id.imageView3);
        textView.setText(birdList.get(position).get_app_name());
        imageView.setImageResource(birdList.get(position).get_icon());

        return v;

    }

}
