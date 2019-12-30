package com.example.professt.onlineexamination.MCQ;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.professt.onlineexamination.R;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Questions> objects1;
    public static ArrayList<String> selectedAnswers;

    public CustomAdapter(Context ctx,ArrayList<Questions> objects1) {
        this.ctx = ctx;
        this.objects1 = objects1;
        // initialize arraylist and add static string for all the questions
        selectedAnswers = new ArrayList<>();
        for (int i = 0; i < objects1.size(); i++) {
            selectedAnswers.add("Not Attempted");
        }
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.questions_paper, parent, false);
        }


        Questions questions = getQuestions(position);

        String ques = questions.getQuestion();
        String op1 = questions.getOp1();
        String op2 = questions.getOp2();
        String op3 = questions.getOp3();
        String op4 = questions.getOp4();
        String answer = questions.getAnswer();
        String teacherId = questions.getTeacherId();
        String names = questions.getNames();

        ((TextView) view.findViewById(R.id.question)).setText(ques);
        ((RadioButton) view.findViewById(R.id.option1)).setText(op1);
        ((RadioButton) view.findViewById(R.id.option2)).setText(op2);
        ((RadioButton) view.findViewById(R.id.option3)).setText(op3);
        ((RadioButton) view.findViewById(R.id.option4)).setText(op4);

        RadioButton option1 = (RadioButton)view.findViewById(R.id.option1);
        RadioButton option2 = (RadioButton)view.findViewById(R.id.option2);
        RadioButton option3 = (RadioButton)view.findViewById(R.id.option3);
        RadioButton option4 = (RadioButton)view.findViewById(R.id.option4);

        option1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    selectedAnswers.set(position, "1");
            }
        });

        option2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    selectedAnswers.set(position, "2");
            }
        });

        option3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    selectedAnswers.set(position, "3");
            }
        });

        option4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    selectedAnswers.set(position, "4");
            }
        });

        return view;
    }

    private Questions getQuestions(int position) {
        return ((Questions) getItem(position));
    }
}
