package com.example.greenspot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class Review{
    public String positive;
    public String date;
    public String written;
    public int id;
    public String photonum;

    Review(String positive, String date, String written, int id, String photonum) {
        this.positive = positive;
        this.date = date;
        this.written = written;
        this.id = id;
        this.photonum = photonum;
    }
}

public class ListViewAdapter extends BaseAdapter {
    private ArrayList<Review> items;
    private Context mContext;

    ListViewAdapter (ArrayList<Review> items, Context mContext) {
        this.mContext = mContext;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.listview_layout, viewGroup, false);

        TextView rvpercent = view.findViewById(R.id.reviewpercent);
        TextView rvdate = view.findViewById(R.id.writtendate);
        TextView rvcontents = view.findViewById(R.id.reviewcontents);
        ImageView rvphoto = view.findViewById(R.id.reviewphoto);
        ImageView rvlike = view.findViewById(R.id.likereview);
        TextView rvphotonum = view.findViewById(R.id.photonum);

        rvpercent.setText(items.get(i).positive);
        rvdate.setText(items.get(i).date);
        rvcontents.setText(items.get(i).written);
        rvphoto.setImageResource(items.get(i).id);
        rvlike.setImageResource(R.drawable.like);
        rvphotonum.setText(items.get(i).photonum);

        return view;
    }
}
