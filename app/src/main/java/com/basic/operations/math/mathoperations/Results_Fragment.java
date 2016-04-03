package com.basic.operations.math.mathoperations;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Results_Fragment extends Fragment {

    View view;
    int end_result_total = 0;
    String[][] full_results_of_game = null;
    TextView[] questions = null;
    TextView[] answers = null;
    TextView[] your_answers = null;
    TextView[] heading = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        questions = new TextView[10];
        answers = new TextView[10];
        your_answers = new TextView[10];
        heading = new TextView[3];

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_game_results, container, false);

        final Typeface custom_name_font = Typeface.createFromAsset(getActivity().getAssets(), "menu_heading_font.ttf");
        final Typeface custom_heading_font = Typeface.createFromAsset(getActivity().getAssets(), "data_heading_font.ttf");

        TextView heading_results = (TextView) view.findViewById(R.id.result_heading);
        heading_results.setTypeface(custom_name_font);
        heading_results.setText("Results");

        TextView end_result = (TextView) view.findViewById(R.id.total_end_result);
        end_result.setTypeface(custom_heading_font);
        end_result.setText("Total: " + end_result_total + " / 10");

        createText_views(custom_heading_font, custom_name_font);

        return view;
    }

    public void setEnd_result_total(int total, String[][] total_array) {
        this.end_result_total = total;
        this.full_results_of_game = total_array;
    }

    private void createText_views(Typeface typeface, Typeface headingType) {

        heading[0] = (TextView) view.findViewById(R.id.heading1);
        heading[0].setTypeface(headingType);
        heading[0].setText("Question");
        heading[1] = (TextView) view.findViewById(R.id.heading2);
        heading[1].setTypeface(headingType);
        heading[1].setText("Answer");
        heading[2] = (TextView) view.findViewById(R.id.heading3);
        heading[2].setTypeface(headingType);
        heading[2].setText("Selected");

        questions[0] = (TextView) view.findViewById(R.id.question1);
        questions[1] = (TextView) view.findViewById(R.id.question2);
        questions[2] = (TextView) view.findViewById(R.id.question3);
        questions[3] = (TextView) view.findViewById(R.id.question4);
        questions[4] = (TextView) view.findViewById(R.id.question5);
        questions[5] = (TextView) view.findViewById(R.id.question6);
        questions[6] = (TextView) view.findViewById(R.id.question7);
        questions[7] = (TextView) view.findViewById(R.id.question8);
        questions[8] = (TextView) view.findViewById(R.id.question9);
        questions[9] = (TextView) view.findViewById(R.id.question10);

        answers[0] = (TextView) view.findViewById(R.id.answer1);
        answers[1] = (TextView) view.findViewById(R.id.answer2);
        answers[2] = (TextView) view.findViewById(R.id.answer3);
        answers[3] = (TextView) view.findViewById(R.id.answer4);
        answers[4] = (TextView) view.findViewById(R.id.answer5);
        answers[5] = (TextView) view.findViewById(R.id.answer6);
        answers[6] = (TextView) view.findViewById(R.id.answer7);
        answers[7] = (TextView) view.findViewById(R.id.answer8);
        answers[8] = (TextView) view.findViewById(R.id.answer9);
        answers[9] = (TextView) view.findViewById(R.id.answer10);

        your_answers[0] = (TextView) view.findViewById(R.id.your_answer1);
        your_answers[1] = (TextView) view.findViewById(R.id.your_answer2);
        your_answers[2] = (TextView) view.findViewById(R.id.your_answer3);
        your_answers[3] = (TextView) view.findViewById(R.id.your_answer4);
        your_answers[4] = (TextView) view.findViewById(R.id.your_answer5);
        your_answers[5] = (TextView) view.findViewById(R.id.your_answer6);
        your_answers[6] = (TextView) view.findViewById(R.id.your_answer7);
        your_answers[7] = (TextView) view.findViewById(R.id.your_answer8);
        your_answers[8] = (TextView) view.findViewById(R.id.your_answer9);
        your_answers[9] = (TextView) view.findViewById(R.id.your_answer10);

        for (int x = 0; x < 10; x++) {
            questions[x].setTypeface(typeface);
            questions[x].setText(full_results_of_game[x][0]);
            answers[x].setTypeface(typeface);
            answers[x].setText(full_results_of_game[x][1]);
            your_answers[x].setTypeface(typeface);
            your_answers[x].setText(full_results_of_game[x][2]);
        }
    }
}
