package com.example.professt.onlineexamination.Score;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.professt.onlineexamination.R;

import java.util.ArrayList;

public class WrittenCustomAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Written> objects1;

    String subject,marks;

    public WrittenCustomAdapter(Context context, ArrayList<Written> products) {
        ctx = context;
        objects1 = products;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects1.size();
    }

    @Override
    public Object getItem(int position) {
        return objects1.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.written_list, parent, false);
        }

        Written written = getFriends(position);

        String subject,mark;

        subject = written.getSubject();
        mark = written.getMarks();

        ((TextView) view.findViewById(R.id.subjects)).setText(subject);
        ((TextView) view.findViewById(R.id.marks)).setText(mark);



        return view;
    }

    private Written getFriends(int position) {

        return ((Written) getItem(position));
    }
}
