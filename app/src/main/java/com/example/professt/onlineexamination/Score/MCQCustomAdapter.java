package com.example.professt.onlineexamination.Score;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.professt.onlineexamination.R;

import java.util.ArrayList;

public class MCQCustomAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Mcq> objects1;

    String subject,marks;

    public MCQCustomAdapter(Context context, ArrayList<Mcq> products) {
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
            view = lInflater.inflate(R.layout.mcq_list, parent, false);
        }

        Mcq mcq = getFriends(position);

        String subject,mark;

        subject = mcq.getSubject();
        mark = mcq.getMarks();

        ((TextView) view.findViewById(R.id.subjects)).setText(subject);
        ((TextView) view.findViewById(R.id.marks)).setText(mark);



        return view;
    }

    private Mcq getFriends(int position) {

        return ((Mcq) getItem(position));
    }
}
