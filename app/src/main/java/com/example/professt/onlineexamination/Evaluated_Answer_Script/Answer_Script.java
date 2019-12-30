package com.example.professt.onlineexamination.Evaluated_Answer_Script;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.professt.onlineexamination.R;
import com.example.professt.onlineexamination.Score.Mcq;

import java.util.ArrayList;

public class Answer_Script extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Answer> objects1;

    String answerId,mid;

    public Answer_Script(Context context, ArrayList<Answer> products) {
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
            view = lInflater.inflate(R.layout.answer, parent, false);
        }

        Answer answer = getFriends(position);

        String answers,mid;

        answers = answer.getAnswerId();
        mid = answer.getMid();

        ((TextView) view.findViewById(R.id.answer)).setText(answers);



        return view;
    }

    private Answer getFriends(int position) {

        return ((Answer) getItem(position));
    }
}
